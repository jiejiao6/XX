LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_CFLAGS := -Wno-pointer-to-int-cast -Wno-int-to-pointer-cast
LOCAL_C_INCLUDES += $(LOCAL_PATH)/../lua
LOCAL_CFLAGS += -std=c99

LOCAL_MODULE     := luajava
LOCAL_SRC_FILES  := luajava.c
LOCAL_LDLIBS += -L$(SYSROOT)/usr/lib -llog -ldl
LOCAL_STATIC_LIBRARIES := liblua
APP_STL         := gnustl_shared

include $(BUILD_SHARED_LIBRARY)
