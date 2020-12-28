LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := magic

LOCAL_C_INCLUDES := \
    $(LOCAL_PATH) \
    $(LOCAL_PATH)/file

LOCAL_SRC_FILES := \
	$(wildcard $(LOCAL_PATH)/file/*.c) \
	$(wildcard $(LOCAL_PATH)/*.c)


LOCAL_LDLIBS := -llog -lz
include $(BUILD_SHARED_LIBRARY)
