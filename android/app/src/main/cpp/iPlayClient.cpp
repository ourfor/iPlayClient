#include <jni.h>

#include <iostream>
#include <vector>
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
    auto ctx = get_java_mpv_context(env, thiz);
    auto value = false;
    const char* prop = env->GetStringUTFChars(name, 0);
    mpv_get_property(ctx, prop, MPV_FORMAT_FLAG, &value);
    return value;
}

extern "C"
JNIEXPORT void JNICALL
Java_top_ourfor_lib_mpv_MPV_init(JNIEnv *env, jobject thiz) {
    auto ctx = mpv_create();
    auto clazz = env->GetObjectClass(thiz);
    auto fieldId = env->GetFieldID(clazz, "handle", "J");
    mpv_set_option_string(ctx, "gpu-context", "android");
    mpv_initialize(ctx);
    env->SetLongField(thiz, fieldId, reinterpret_cast<jlong>(ctx));
}

extern "C"
JNIEXPORT void JNICALL
Java_top_ourfor_lib_mpv_MPV_destroy(JNIEnv *env, jobject thiz) {
    auto ptr = get_java_mpv_context(env, thiz);
    mpv_destroy(ptr);
}

extern "C"
JNIEXPORT void JNICALL
Java_top_ourfor_lib_mpv_MPV_setDrawable(JNIEnv *env, jobject thiz, jobject surface) {
    auto wid = reinterpret_cast<int64_t>(surface);
    auto ctx = get_java_mpv_context(env, thiz);
    mpv_set_option(ctx, "wid", MPV_FORMAT_INT64, (void*) &wid);
}
extern "C"
JNIEXPORT void JNICALL
Java_top_ourfor_lib_mpv_MPV_command(JNIEnv *env, jobject thiz, jobjectArray args) {
    auto ctx = get_java_mpv_context(env, thiz);
    jsize length = env->GetArrayLength(args);
    std::vector<const char*> cstr_vec;
    std::vector<std::string> str_vec;
    cstr_vec.reserve(length + 1);
    str_vec.reserve(length);
    for (jsize i = 0; i < length; ++i) {
        jstring jstr = (jstring) env->GetObjectArrayElement(args, i);
        const char* cstr = env->GetStringUTFChars(jstr, 0);
        str_vec.push_back(std::string(cstr));
        cstr_vec.push_back(str_vec.back().c_str());
        env->ReleaseStringUTFChars(jstr, cstr);
    }
    cstr_vec.push_back(nullptr);
    mpv_command(ctx, cstr_vec.data());
}