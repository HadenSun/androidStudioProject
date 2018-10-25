/*****************************************************************************
 *                                                                            *
 *  OpenNI 2.x Alpha                                                          *
 *  Copyright (C) 2012 PrimeSense Ltd.                                        *
 *                                                                            *
 *  This file is part of OpenNI.                                              *
 *                                                                            *
 *  Licensed under the Apache License, Version 2.0 (the "License");           *
 *  you may not use this file except in compliance with the License.          *
 *  You may obtain a copy of the License at                                   *
 *                                                                            *
 *      http://www.apache.org/licenses/LICENSE-2.0                            *
 *                                                                            *
 *  Unless required by applicable law or agreed to in writing, software       *
 *  distributed under the License is distributed on an "AS IS" BASIS,         *
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *
 *  See the License for the specific language governing permissions and       *
 *  limitations under the License.                                            *
 *                                                                            *
 *****************************************************************************/
package org.openni.android.tools.niviewer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.openni.Device;
import org.openni.OpenNI;
import org.openni.SensorType;
import org.openni.VideoFrameRef;
import org.openni.VideoMode;
import org.openni.VideoStream;
import org.openni.android.OpenNIView;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
//文件的操作
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;
import java.io.*;
import java.util.*;
public class StreamView extends RelativeLayout {

	static {
		System.loadLibrary("native-lib");
	}

	private static final String TAG = "StreamView";
	private Thread mMainLoopThread;
	private boolean mShouldRun = true;
	private Device mDevice;
	private VideoStream mStream;
	private Spinner mSensorSpinner;
	private List<SensorType> mDeviceSensors;
	private List<VideoMode> mStreamVideoModes;
	private Spinner mVideoModeSpinner;
	private OpenNIView mFrameView;
	private TextView mStatusLine;
	private Button mRemoveButton;
	private ImageView mImageView;
	private int sensorTypeInt;

	private static SensorType[] SENSORS = { SensorType.DEPTH, SensorType.COLOR, SensorType.IR };
	private static CharSequence[] SENSOR_NAMES = { "Depth", "Color", "IR" };

	public StreamView(Context context) {
		super(context);
		initialize(context);
	}

	public StreamView(Context context,int pos) {
		super(context);
		initialize(context);
		onSensorSelected(pos);
	}

	public StreamView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	public StreamView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context);
	}

	public VideoStream getStream() {
		return mStream;
	}

	private void initialize(Context context) {
		View.inflate(context, R.layout.stream_view, this);

		if (!isInEditMode()) {
			mSensorSpinner = (Spinner) findViewById(R.id.spinnerSensor);
			mVideoModeSpinner = (Spinner) findViewById(R.id.spinnerVideoMode);
			mFrameView = (OpenNIView) findViewById(R.id.frameView);
			mStatusLine = (TextView) findViewById(R.id.status_line);
			mRemoveButton = (Button) findViewById(R.id.button_remove);
			mImageView = (ImageView)findViewById(R.id.imageView2);



			//产生下拉框 可以选择
			mSensorSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				//监听着
				public void onItemSelected(AdapterView<?> parent, View view,
										   int position, long id) {
					onSensorSelected(position);//获取选择的项值
				}

				@Override
				//什么都没选
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});

			mVideoModeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
										   int position, long id) {
					onVideoModeSelected(position);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});

			mRemoveButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onRemove();
				}
			});
		}
	}

	public void setDevice(Device device) {
		mDevice = device;
		mDeviceSensors = new ArrayList<SensorType>();

		List<CharSequence> sensors = new ArrayList<CharSequence>();

		for (int i = 0; i < SENSORS.length; ++i) {
			if (mDevice.hasSensor(SENSORS[i])) {
				sensors.add(SENSOR_NAMES[i]);
				mDeviceSensors.add(SENSORS[i]);
			}
		}

		ArrayAdapter<CharSequence> sensorsAdapter = new ArrayAdapter<CharSequence>(getContext(),
				android.R.layout.simple_spinner_item, sensors);
		sensorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSensorSpinner.setAdapter(sensorsAdapter);

		mSensorSpinner.setSelection(1);
	}

	public void setDevice(Device device,int pos) {
		mDevice = device;
		mDeviceSensors = new ArrayList<SensorType>();

		List<CharSequence> sensors = new ArrayList<CharSequence>();

		for (int i = 0; i < SENSORS.length; ++i) {
			if (mDevice.hasSensor(SENSORS[i])) {
				sensors.add(SENSOR_NAMES[i]);
				mDeviceSensors.add(SENSORS[i]);
			}
		}

		ArrayAdapter<CharSequence> sensorsAdapter = new ArrayAdapter<CharSequence>(getContext(),
				android.R.layout.simple_spinner_item, sensors);
		sensorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSensorSpinner.setAdapter(sensorsAdapter);

		mSensorSpinner.setSelection(pos);
	}

	public void stop() {
		mShouldRun = false;

		while (mMainLoopThread != null) {
			try {
				mMainLoopThread.join();
				mMainLoopThread = null;
				break;
			} catch (InterruptedException e) {
			}
		}

		if (mStream != null) {
			mStream.stop();
		}

		mFrameView.clear();
		mStatusLine.setText(R.string.waiting_for_frames);
	}
	public void destroy()
	{
		if (mStream != null) {
			mStream.destroy();
			mStream = null;
		}
	}
	private void onSensorSelected(int pos) {
		try {

            sensorTypeInt = pos;

			stop();

			if (mStream != null) {
				mStream.destroy();
				mStream = null;
			}

			SensorType sensor = mDeviceSensors.get(pos);
			mStream = VideoStream.create(mDevice, sensor);
			List<CharSequence> videoModesNames = new ArrayList<CharSequence>();

			mStreamVideoModes = mStream.getSensorInfo().getSupportedVideoModes();
			for (int i = 0; i < mStreamVideoModes.size(); ++i) {
				VideoMode mode = mStreamVideoModes.get(i);

				videoModesNames.add(String.format("%d x %d @ %d FPS (%s)",
						mode.getResolutionX(),
						mode.getResolutionY(),
						mode.getFps(),
						pixelFormatToName(mode.getPixelFormat())));
			}

			ArrayAdapter<CharSequence> videoModesAdapter = new ArrayAdapter<CharSequence>(getContext(),
					android.R.layout.simple_spinner_item, videoModesNames);
			videoModesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mVideoModeSpinner.setAdapter(videoModesAdapter);

			// use default mode
			VideoMode currentMode = mStream.getVideoMode();
			int selected = mStreamVideoModes.indexOf(currentMode);
			if (selected == -1) {
				selected = 0;
			}

			mVideoModeSpinner.setSelection(selected);

			if (sensor == SensorType.DEPTH) {
				mFrameView.setBaseColor(Color.YELLOW);
			} else {
				mFrameView.setBaseColor(Color.WHITE);
			}

		} catch (RuntimeException e) {
			showAlert("Failed to switch to stream: " + e.getMessage());
		}
	}

	private CharSequence pixelFormatToName(org.openni.PixelFormat format) {
		switch (format) {
			case DEPTH_1_MM:    return "1 mm";
			case DEPTH_100_UM:  return "100 um";
			case DEPTH_1_3_MM: 	return "1/3 mm";
			case SHIFT_9_2:     return "9.2";
			case SHIFT_9_3:     return "9.3";
			case RGB888:        return "RGB";
			case GRAY8:         return "Gray8";
			case GRAY16:        return "Gray16";
			case YUV422:		return "YUV422";
			case YUYV:			return "YUYV";
			default:            return "UNKNOWN";
		}
	}
//////////////////////////////////////////////////////////////////////////////////////////////////


	/**
	 * 把数据写入文件
	 */


	private void WriteFile() throws IOException {
		File newFile = new File(Environment.getExternalStorageDirectory(),"Text.xml");//

		String data ="你好,Android.2018";

		FileWriter write = new FileWriter(newFile,true);

		BufferedWriter bufferedWriter = new BufferedWriter(write);

		bufferedWriter.write(data);bufferedWriter.newLine();//换行



		 /* 加了这行才能将数据写入目的地。 * */

		bufferedWriter.flush();
		write.close();
		bufferedWriter.close();


		/*newFile.createNewFile();
		FileOutputStream ouStream = new FileOutputStream(newFile);
		ouStream.write(data.getBytes());
		ouStream.close();*/

	}





////////////////////////////////////////////////////////////////////////////////////////////////////



	private void onVideoModeSelected(int pos) {
		try {
			stop();

			if (!mStream.getVideoMode().equals(mStreamVideoModes.get(pos))) {
				mStream.setVideoMode(mStreamVideoModes.get(pos));
			}
			mStream.start();
		} catch (RuntimeException e) {
			showAlert("Failed to switch to video mode: " + e.getMessage());
		}

		mShouldRun = true;

		if(sensorTypeInt == 0)
		{
			Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.test);
			//mImageView.setImageBitmap(bm);
		}



			mMainLoopThread = new Thread(new Runnable() {
				@Override
				public void run() {
					List<VideoStream> streams = new ArrayList<VideoStream>();
					streams.add(mStream);
					long lastTime = System.nanoTime();
					long frameCount = 0;
					int fps = 0;
					int frame_index=0;

					while (mShouldRun) {
						VideoFrameRef frame = null;

						try {
							OpenNI.waitForAnyStream(streams, 100);
							frame = mStream.readFrame();

							////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							frame_index=frame.getFrameIndex();
							int index_image=2;//mStream.getVideoMode().equals(mStreamVideoModes.get(2))
							if(sensorTypeInt == 0) {
								ByteBuffer byteBuffer = frame.getData();
								//
								// WriteFile();
//								File newFile = new File(Environment.getExternalStorageDirectory(),"Text1.txt");//
//
//								//String data ="你好,Android.2018";
//                             // byte[] data= new byte[640 * 480*2];
//							// byteBuffer.get(data);
//
//							 String dataString = new String(data, "ISO-8859-1");//byte 数组  转换成字符串
//
//								FileWriter write = new FileWriter(newFile,true);
//								BufferedWriter bufferedWriter = new BufferedWriter(write);
//
//								bufferedWriter.write(dataString);
//
//								bufferedWriter.newLine();//换行
//								bufferedWriter.flush();
//								write.close();
//								bufferedWriter.close();


//								//保存文件/
//
//								byte[] by1 = new byte[640 * 480*2];
//								byteBuffer.get(by1);
//
//								Log.i(TAG, "run: "+by1);
//
//								FileOutputStream fos = null;
//								//File file=new File("C:\\Users\\Administrator\\Desktop");
//
//								File  file = new File("newfile.txt");
//								try {
//									fos = new FileOutputStream(file);
//								} catch (FileNotFoundException e) {
//									e.printStackTrace();
//								}
//								// 用FileOutputStream 的write方法写入字节数组
//								try {
//									fos.write(by1);
//								} catch (IOException e) {
//									e.printStackTrace();
//								}
//								Log.i(TAG, "run: ");
//								System.out.println("写入成功");
//								// 为了节省IO流的开销，需要关闭
//								try {
//									fos.close();
//								} catch (IOException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}

								///////////////////////////////////////////////////////////////////////////////////////////////////
								byte[] by = new byte[640 * 480*index_image];
								byteBuffer.get(by, 0, 640 * 480*index_image);
								int rstData[] = bitmap2Gray(by, 640, 480);
								Bitmap resultImage = Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888);
								resultImage.setPixels(rstData, 0, 640, 0, 0, 640, 480);

								//Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.test);
								//mImageView.setImageBitmap(bm);
								mImageView.setImageBitmap(resultImage);
								//mFrameView.setVisibility(VISIBLE);
							}
							else {
								mFrameView.setVisibility(VISIBLE);
							}
							////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							// Request rendering of the current OpenNI frame
							mFrameView.update(frame);
							++frameCount;
							if (frameCount == 30) {
								long now = System.nanoTime();
								long diff = now - lastTime;
								fps = (int)(1e9 * 30 / diff);
								frameCount = 0;
								lastTime = now;
							}

							updateLabel(String.format("Frame Index: %,d | Timestamp: %,d | FPS: %d", frame.getFrameIndex(), frame.getTimestamp(), fps));

						} catch (TimeoutException e) {
							Log.e(TAG,"Timeout: " + e);
						} catch (Exception e) {
							Log.e(TAG, "Failed reading frame: " + e);
						}
					}
				}
			});



		mMainLoopThread.setName("SimpleViewer MainLoop Thread");
		mMainLoopThread.start();
	}

	private void onRemove() {
		stop();
		mStream.destroy();
		ViewGroup parent = (ViewGroup)this.getParent();
		parent.removeView(this);

		//Update layout for all other children (we re-add each one because forceLayout() doesn't work)
		for(int i=0; i < parent.getChildCount(); i++){
			View currView = parent.getChildAt(i);
			parent.removeView(currView);
			parent.addView(currView);
		}
	}

	private void showAlert(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setMessage(message);
		builder.show();
	}

	private void updateLabel(final String message) {
		post(new Runnable() {
			public void run() {
				mStatusLine.setText(message);
			}
		});
	}

	public native int[] bitmap2Gray(byte[] pixels, int w,int h);

}
