#include <jni.h>
#include <magic.h>
#include <file.h>

struct magic_set *g_magic;

static void ensure_open() {
    if (g_magic != NULL) {
        magic_close(g_magic);
    }
    g_magic = magic_open(MAGIC_MIME_TYPE);
}

static void ensure_close() {
    if (g_magic != NULL) {
        magic_close(g_magic);
        g_magic = NULL;
    }
}

JNIEXPORT jint JNICALL
Java_com_hzy_libmagic_MagicApi_getMagicVersion(JNIEnv *env, jclass type) {
    return magic_version();
}

JNIEXPORT jint JNICALL
Java_com_hzy_libmagic_MagicApi_loadFromFile(JNIEnv *env, jclass type, jstring magicPath_) {
    const char *magicPath = (*env)->GetStringUTFChars(env, magicPath_, 0);
    ensure_open();
    int ret = magic_load(g_magic, magicPath);
    (*env)->ReleaseStringUTFChars(env, magicPath_, magicPath);
    return ret;
}

JNIEXPORT jint JNICALL
Java_com_hzy_libmagic_MagicApi_loadFromBytes(JNIEnv *env, jclass type, jbyteArray magicBytes_) {
    jsize bufferLength = ((*env)->GetArrayLength(env, magicBytes_));
    jbyte *cBuffer = malloc((size_t) bufferLength);
    (*env)->GetByteArrayRegion(env, magicBytes_, 0, bufferLength, cBuffer);
    ensure_open();
    int ret = magic_load_buffers(g_magic, (void **) &cBuffer, (size_t *) &bufferLength, 1);
    return ret;
}

JNIEXPORT jstring JNICALL
Java_com_hzy_libmagic_MagicApi_magicFile(JNIEnv *env, jclass type, jstring filePath_) {
    const char *filePath = (*env)->GetStringUTFChars(env, filePath_, 0);
    const char *ret = magic_file(g_magic, filePath);
    (*env)->ReleaseStringUTFChars(env, filePath_, filePath);
    return (*env)->NewStringUTF(env, ret);
}

JNIEXPORT jint JNICALL
Java_com_hzy_libmagic_MagicApi_close(JNIEnv *env, jclass type) {
    ensure_close();
    return 0;
}
