#include <jni.h>
#include <magic.h>
#include "ndkhelper.h"

JNIEXPORT jint JNICALL
Java_com_hzy_libmagic_MagicApi_getMagicVersion(JNIEnv *env, jclass type) {
    return magic_version();
}