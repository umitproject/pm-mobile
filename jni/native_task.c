#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/system_properties.h>
#include <jni.h>
JNIEXPORT jint JNICALL Java_com_test_NativeTask_runCommand(JNIEnv *env, jclass class, jstring command)
{
	const char *commandString;
	  commandString = (*env)->GetStringUTFChars(env, command, 0);
	  int exitcode = system(commandString);
	  (*env)->ReleaseStringUTFChars(env, command, commandString);
	  return (jint)exitcode;
}
