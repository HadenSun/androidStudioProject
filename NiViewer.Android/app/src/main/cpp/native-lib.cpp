#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_org_openni_android_tools_niviewer_NiViewerActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {

    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());

}
