#include <jni.h>
#include <stdio.h>
#include <string.h>

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jbyteArray JNICALL Java_com_streamingopentok_Native_convertRGBAToARGB(JNIEnv *env, jobject jobject, jbyteArray rgbaByteArray);

#ifdef __cplusplus
}
#endif

JNIEXPORT jbyteArray Java_com_streamingopentok_Native_convertRGBAToARGB(JNIEnv *env, jobject jobject, jbyteArray rgbaByteArray) {
    int lenght = (*env)->GetArrayLenght(env, rgbaByteArray);
    for (int int i = 0; i < lenght; i++) {

    }
}