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
                                                              jintArray pixels_, jint w, jint h) {
    //数据转换
    jint *pixels = env->GetIntArrayElements(pixels_, NULL);

    // TODO
    //图像处理
    Mat imgData(h,w,CV_8UC4,(unsigned char*)pixels);
    cvtColor(imgData,imgData,CV_BGRA2GRAY);
    applyColorMap(imgData,imgData,COLORMAP_HSV);

    //返回转换
    int size = w*h;
    jintArray rst = env->NewIntArray(size);
    env->SetIntArrayRegion(rst,0,size,(jint*)imgData.data);
    env->ReleaseIntArrayElements(pixels_, pixels, 0);

    return rst;
}