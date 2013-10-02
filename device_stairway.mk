# Copyright (C) 2013 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

$(call inherit-product-if-exists, vendor/wiko/stairway/stairway-vendor.mk)

# prebuilt kernel modules
MOD_TGT := /system/lib/modules
MOD_SRC := $(LOCAL_PATH)/prebuilt/modules
PRODUCT_COPY_FILES += \
	$(MOD_SRC)/ccci.ko:$(MOD_TGT)/ccci.ko \
	$(MOD_SRC)/ccci_plat.ko:$(MOD_TGT)/ccci_plat.ko \
	$(MOD_SRC)/devapc.ko:$(MOD_TGT)/devapc.ko \
	$(MOD_SRC)/devinfo.ko:$(MOD_TGT)/devinfo.ko \
	$(MOD_SRC)/hid-logitech-dj.ko:$(MOD_TGT)/hid-logitech-dj.ko \
	$(MOD_SRC)/mtk_fm_drv.ko:$(MOD_TGT)/mtk_fm_drv.ko \
	$(MOD_SRC)/mtk_hif_sdio.ko:$(MOD_TGT)/mtk_hif_sdio.ko \
	$(MOD_SRC)/mtk_stp_bt.ko:$(MOD_TGT)/mtk_stp_bt.ko \
	$(MOD_SRC)/mtk_stp_gps.ko:$(MOD_TGT)/mtk_stp_gps.ko \
	$(MOD_SRC)/mtk_stp_uart.ko:$(MOD_TGT)/mtk_stp_uart.ko \
	$(MOD_SRC)/mtk_stp_wmt.ko:$(MOD_TGT)/mtk_stp_wmt.ko \
	$(MOD_SRC)/mtk_wmt_wifi.ko:$(MOD_TGT)/mtk_wmt_wifi.ko \
	$(MOD_SRC)/mtklfb.ko:$(MOD_TGT)/mtklfb.ko \
	$(MOD_SRC)/pvrsrvkm.ko:$(MOD_TGT)/pvrsrvkm.ko \
	$(MOD_SRC)/scsi_tgt.ko:$(MOD_TGT)/scsi_tgt.ko \
	$(MOD_SRC)/scsi_wait_scan.ko:$(MOD_TGT)/scsi_wait_scan.ko \
	$(MOD_SRC)/sec.ko:$(MOD_TGT)/sec.ko \
	$(MOD_SRC)/vcodec_kernel_driver.ko:$(MOD_TGT)/vcodec_kernel_driver.ko \
	$(MOD_SRC)/wlan_mt6628.ko:$(MOD_TGT)/wlan_mt6628.ko

PRODUCT_COPY_FILES += \
	$(LOCAL_PATH)/root/fstab.mt6589:root/fstab.mt6589

PRODUCT_COPY_FILES += \
	$(LOCAL_PATH)/root/ueventd.mt6589.rc:root/ueventd.mt6589.rc \
	$(LOCAL_PATH)/root/init.mt6589.rc:root/init.mt6589.rc

# Propertys spacific for this device
PRODUCT_PROPERTY_OVERRIDES := \
	ro.opengles.version=131072 \
	ro.mediatek.version.release=ALPS.JB2.MP.V1.2 \
	ro.mediatek.platform=MT6589 \
	ro.mediatek.chip_ver=S01 \
	ro.mediatek.version.branch=ALPS.JB2.MP \
	ro.mediatek.version.sdk=1

# This device have enough room for precise davick
PRODUCT_TAGS += dalvik.gc.type-precise

# Inherit tablet dalvik settings
$(call inherit-product, frameworks/native/build/phone-xhdpi-1024-dalvik-heap.mk)
