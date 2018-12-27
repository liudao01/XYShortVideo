#include <jni.h>
#include <string>


extern "C"
JNIEXPORT void JNICALL
Java_com_xy_www_xylib_XYUtil_n_1addMark(JNIEnv *env, jobject instance) {

    // TODO

}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_xy_www_xylib_XYUtil_n_1helloWord(JNIEnv *env, jobject instance) {

    // TODO
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}