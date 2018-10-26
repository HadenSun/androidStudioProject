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
    Mat imgData(h,w,CV_16UC1,(unsigned char*)pixels);
    Mat image;
    imgData.convertTo(image,CV_8U,1.0/6,-170);       //图像压缩
    applyColorMap(image,image,COLORMAP_HSV);        //伪彩色图像
    cvtColor(image,image,CV_RGB2BGRA);              //转RGBA四通道

    //返回转换
    int size = w*h;
    jintArray rst = env->NewIntArray(size);
    env->SetIntArrayRegion(rst,0,size,(jint*)image.data);
    env->ReleaseByteArrayElements(pixels_, pixels, 0);

    return rst;
}