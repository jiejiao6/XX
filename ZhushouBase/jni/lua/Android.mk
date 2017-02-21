LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := lua
LOCAL_CFLAGS += -std=c99
LOCAL_SRC_FILES := \
	lapi.c \
	lauxlib.c \
	lbaselib.c \
	lbitlib.c \
	lcode.c \
	lcorolib.c \
	lctype.c \
	ldblib.c \
	ldebug.c \
	ldo.c \
	ldump.c \
	lfunc.c \
	lgc.c \
	linit.c \
	liolib.c \
	llex.c \
	lmathlib.c \
	lmem.c \
	loadlib.c \
	lobject.c \
	lopcodes.c \
	loslib.c \
	lparser.c \
	lstate.c \
	lstring.c \
	lstrlib.c \
	ltable.c \
	ltablib.c \
	ltm.c \
	lua.c \
	luac.c \
	lundump.c \
	lutf8lib.c \
	lvm.c \
	lzio.c 

# Auxiliary lua user defined file
# LOCAL_SRC_FILES += luauser.c
# LOCAL_CFLAGS := -DLUA_DL_DLOPEN -DLUA_USER_H='"luauser.h"'

LOCAL_CFLAGS := -DLUA_DL_DLOPEN
include $(BUILD_STATIC_LIBRARY) 
