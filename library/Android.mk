LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := gpuimage-library
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_LDLIBS := \
	-llog \

LOCAL_SRC_FILES := \
	jni/yuv-decoder.c \

LOCAL_C_INCLUDES += jni
LOCAL_C_INCLUDES += src/debug/jni

include $(BUILD_SHARED_LIBRARY)