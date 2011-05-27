#LOCAL_PATH := $(call my-dir) 

LOCAL_PATH := ./jni

include $(CLEAR_VARS)
LOCAL_MODULE    := libpcap-t

LOCAL_C_INCLUDES := $(NDK_ROOT)/external/libpcap 

LOCAL_STATIC_LIBRARIES := libpcap 

LOCAL_LDLIBS := -ldl -llog

include $(BUILD_SHARED_LIBRARY) 

include $(NDK_ROOT)/external/libpcap/Android.mk