LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := Beacontest
LOCAL_SRC_FILES := Beacontest.cpp

include $(BUILD_SHARED_LIBRARY)
