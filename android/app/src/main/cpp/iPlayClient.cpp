#include <jni.h>

// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("iPlayClient");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("iPlayClient")
//      }
//    }

#include <iostream>

extern "C"
JNIEXPORT void JNICALL
Java_top_ourfor_lib_mpv_MPV_init(JNIEnv *env, jclass clazz) {
    std::cout << "Hello World" << std::endl;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_top_ourfor_lib_mpv_MPV_getBoolTypeProperty(JNIEnv *env, jclass clazz, jstring name) {
    return false;
}