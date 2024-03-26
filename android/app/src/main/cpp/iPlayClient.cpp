#include <jni.h>

#include <iostream>
#include "mpv/client.h"

inline static mpv_handle * get_java_mpv_context(JNIEnv *&env, jobject &thiz) {
    auto clazz = env->GetObjectClass(thiz);
    auto fieldId = env->GetFieldID(clazz, "handle", "J");
    auto ptr = reinterpret_cast<mpv_handle *>(env->GetLongField(thiz, fieldId));
    return ptr;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_top_ourfor_lib_mpv_MPV_getBoolTypeProperty(JNIEnv *env, jobject thiz, jstring name) {
    return true;
}

extern "C"
JNIEXPORT void JNICALL
Java_top_ourfor_lib_mpv_MPV_init(JNIEnv *env, jobject thiz) {
    auto ptr = mpv_create();
    auto clazz = env->GetObjectClass(thiz);
    auto fieldId = env->GetFieldID(clazz, "handle", "J");
    env->SetLongField(thiz, fieldId, reinterpret_cast<jlong>(ptr));
}

extern "C"
JNIEXPORT void JNICALL
Java_top_ourfor_lib_mpv_MPV_destroy(JNIEnv *env, jobject thiz) {
    auto ptr = get_java_mpv_context(env, thiz);
    mpv_destroy(ptr);
}