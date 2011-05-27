#include<jni.h>
#include<stdio.h>
#include "Helloworld.h"
JNIEXPORT void JNICALL 
Java_Helloworld_print(JNIEnv *env, jobject obj)
{
	printf("Hello World from native\n");
	return;
}
void main()
{
	return;
}
