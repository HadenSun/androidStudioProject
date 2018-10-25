#include <jni.h>
#include <string>
#include <opencv2/opencv.hpp>
#include <iostream>

using namespace std;
using namespace cv;

extern "C" JNIEXPORT jstring

JNICALL
Java_org_openni_android_tools_niviewer_NiViewerActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {

    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());

}

extern "C"
JNIEXPORT jintArray JNICALL
Java_org_openni_android_tools_niviewer_StreamView_bitmap2Gray(JNIEnv *env, jobject instance,
                                                              jbyteArray pixels_, jint w, jint h) {
    //数据转换
    jbyte *pixels = env->GetByteArrayElements(pixels_, NULL);//jbyteArray 转 c++中的BYTE[]

    // TODO
    //图像处理
    Mat imgData(h,w,CV_8UC1,(unsigned char*)pixels);
    //cvtColor(imgData,imgData,CV_BGRA2GRAY);              //转灰度图
    cvtColor(imgData,imgData,CV_GRAY2BGRA);              //需要显示，再转回彩色图
    //applyColorMap(imgData,imgData,COLORMAP_HSV);

    //返回转换
    int size = w*h;
    jintArray rst = env->NewIntArray(size);
    env->SetIntArrayRegion(rst,0,size,(jint*)imgData.data);
    env->ReleaseByteArrayElements(pixels_, pixels, 0);

    return rst;
}