
LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

WPA_SUPPL_DIR = external/wpa_supplicant_8/wpa_supplicant

INCLUDES = $(WPA_SUPPL_DIR) \
    $(WPA_SUPPL_DIR)/src \
    $(WPA_SUPPL_DIR)/src/common \
    $(WPA_SUPPL_DIR)/src/drivers \
    $(WPA_SUPPL_DIR)/src/l2_packet \
    $(WPA_SUPPL_DIR)/src/utils \
    $(WPA_SUPPL_DIR)/src/wps \
    external/libnl-headers

include $(CLEAR_VARS)
LOCAL_MODULE := lib_driver_cmd_mtk
LOCAL_MODULE_TAGS := eng
LOCAL_SHARED_LIBRARIES := libc libcutils
LOCAL_STATIC_LIBRARIES := libnl_2
LOCAL_CFLAGS := $(L_CFLAGS)
LOCAL_SRC_FILES := driver_mtk_nl80211.c
LOCAL_C_INCLUDES := $(INCLUDES)
include $(BUILD_STATIC_LIBRARY)

WIFI_DRIVER_SOCKET_IFACE := wlan0
include external/wpa_supplicant_8/wpa_supplicant/wpa_supplicant_conf.mk
