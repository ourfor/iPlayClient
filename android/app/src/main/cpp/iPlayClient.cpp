#include <jni.h>

#include <iostream>
#include <vector>
#include "mpv/client.h"
#include <android/log.h>

#define LOG_TAG "MYMPV"
#define aerror(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define alog(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

static mpv_handle *globalCtx;

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
    int value = false;
    const char* prop = env->GetStringUTFChars(name, 0);
    alog("get name %s", prop);
    mpv_get_property(ctx, prop, MPV_FORMAT_FLAG, &value);
    return value == 1;
}

extern "C"
JNIEXPORT void JNICALL
Java_top_ourfor_lib_mpv_MPV_setBoolTypeProperty(JNIEnv *env, jobject thiz, jstring name,
                                                jboolean flag) {
    auto ctx = get_java_mpv_context(env, thiz);
    int value = flag;
    const char* prop = env->GetStringUTFChars(name, 0);
    alog("set name %s", prop);
    mpv_set_property(ctx, prop, MPV_FORMAT_FLAG, &value);
}

extern "C"
JNIEXPORT void JNICALL
Java_top_ourfor_lib_mpv_MPV_setStringTypeProperty(JNIEnv *env, jobject thiz, jstring name,
                                                  jstring value) {
    auto ctx = get_java_mpv_context(env, thiz);
    const char *prop = env->GetStringUTFChars(name, 0);
    const char *flag = env->GetStringUTFChars(value, 0);
    alog("set name %s = %s", prop, value);
    mpv_set_property(ctx, prop, MPV_FORMAT_STRING, &flag);
}

extern "C"
JNIEXPORT void JNICALL
Java_top_ourfor_lib_mpv_MPV_init(JNIEnv *env, jobject thiz) {
    auto ctx = mpv_create();
    auto clazz = env->GetObjectClass(thiz);
    auto fieldId = env->GetFieldID(clazz, "handle", "J");
    mpv_request_log_messages(ctx, "debug");
    mpv_set_option_string(ctx, "gpu-context", "android");
    mpv_set_option_string(ctx, "subs-fallback", "yes");
    mpv_set_option_string(ctx, "vo", "gpu-next");
    mpv_set_option_string(ctx, "gpu-api", "vulkan");
    mpv_set_option_string(ctx, "hwdec", "auto");
    int state = mpv_initialize(ctx);
    if (state < 0) {
        aerror("%s", "mpv init failed!");
    } else {
        alog("%s", "mpv init success!");
    }
    alog("mpv addr = %p", ctx);
    globalCtx = ctx;
    env->SetLongField(thiz, fieldId, reinterpret_cast<jlong>(ctx));
}

extern "C"
JNIEXPORT void JNICALL
Java_top_ourfor_lib_mpv_MPV_destroy(JNIEnv *env, jobject thiz) {
    alog("destroy");
    auto ctx = get_java_mpv_context(env, thiz);
    mpv_destroy(ctx);
}

extern "C"
JNIEXPORT void JNICALL
Java_top_ourfor_lib_mpv_MPV_setDrawable(JNIEnv *env, jobject thiz, jobject surface) {
    alog("set drawable");
    auto drawable = env->NewGlobalRef(surface);
    auto wid = (int64_t)(intptr_t)drawable;
    auto ctx = get_java_mpv_context(env, thiz);
    mpv_set_option(ctx, "wid", MPV_FORMAT_INT64, (void*) &wid);
}
extern "C"
JNIEXPORT void JNICALL
Java_top_ourfor_lib_mpv_MPV_command(JNIEnv *env, jobject thiz, jobjectArray args) {
    alog("command: %s", args[0]);
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