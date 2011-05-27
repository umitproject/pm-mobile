#include<stdio.h>
#include<jni.h>
#include "print.h"
JNIEXPORT jstring JNICALL Java_print_prints
  (JNIEnv *env, jobject obj)
{
	return (*env)->NewStringUTF(env, "Passed from native");
}
void main()
{
	return;
}
