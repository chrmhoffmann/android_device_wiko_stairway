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

DEVICE_PACKAGE_OVERLAYS += $(LOCAL_PATH)/overlay/

# prebuilt kernel modules
MOD_TGT := /system/lib/modules
MOD_SRC := $(LOCAL_PATH)/prebuilt/modules

PRODUCT_COPY_FILES += \
	$(LOCAL_PATH)/root/fstab.mt6589:root/fstab.mt6589

PRODUCT_COPY_FILES += \
	$(LOCAL_PATH)/root/ueventd.mt6589.rc:root/ueventd.mt6589.rc \
	$(LOCAL_PATH)/root/init.mt6589.rc:root/init.mt6589.rc \
	$(LOCAL_PATH)/root/init.modem.rc:root/init.modem.rc \
	$(LOCAL_PATH)/root/init.protect.rc:root/init.protect.rc \
	$(LOCAL_PATH)/root/init.mt6589.usb.rc:/root/init.mt6589.usb.rc

PRODUCT_COPY_FILES += \
	frameworks/native/data/etc/handheld_core_hardware.xml:system/etc/permissions/handheld_core_hardware.xml \
	frameworks/native/data/etc/android.hardware.wifi.direct.xml:system/etc/permissions/android.hardware.wifi.direct.xml \
	frameworks/native/data/etc/android.hardware.wifi.xml:system/etc/permissions/android.hardware.wifi.xml \
	frameworks/native/data/etc/android.hardware.touchscreen.multitouch.jazzhand.xml:system/etc/permissions/android.hardware.touchscreen.multitouch.jazzhand.xml \
	frameworks/native/data/etc/android.hardware.telephony.gsm.xml:system/etc/permissions/android.hardware.telephony.gsm.xml \
	frameworks/native/data/etc/android.hardware.sensor.light.xml:system/etc/permissions/android.hardware.sensor.light.xml \
	frameworks/native/data/etc/android.hardware.sensor.proximity.xml:system/etc/permissions/android.hardware.sensor.proximity.xml \
	frameworks/native/data/etc/android.hardware.location.gps.xml:system/etc/permissions/android.hardware.location.gps.xml \
	frameworks/native/data/etc/android.hardware.camera.flash-autofocus.xml:system/etc/permissions/android.hardware.camera.flash-autofocus.xml \
	frameworks/native/data/etc/android.hardware.camera.front.xml:system/etc/permissions/android.hardware.camera.front.xml

PRODUCT_COPY_FILES += \
	$(LOCAL_PATH)/media_codecs.xml:system/etc/media_codecs.xml \
	$(LOCAL_PATH)/media_profiles.xml:system/etc/media_profile.xml

PRODUCT_PROPERTY_OVERRIDES := \
	fmradio.driver.chip=3 \
	gps.solution.combo.chip=1 \
	mediatek.wlan.chip=MT6628 \
	mediatek.wlan.ctia=0 \
	mediatek.wlan.module.postfix=_mt6628 \
	persist.mtk.wcn.combo.chipid=0x6628 \
	persist.radio.fd.counter=15 \
	persist.radio.fd.off.counter=5 \
	persist.radio.fd.off.r8.counter=5 \
	persist.radio.fd.r8.counter=15 \
	persist.radio.multisim.config=dsds \
	persist.sys.usb.config=adb \
	ril.current.share_modem=2 \
	ril.external.md=0 \
	ril.first.md=1 \
	ril.flightmode.poweroffMD=1 \
	ril.radiooff.poweroffMD=0 \
	ril.specific.sm_cause=0 \
	ril.telephony.mode=1 \
	rild.libpath=/system/lib/mtk-ril.so \
	ro.gemini.smart_3g_switch=1 \
	ro.mediatek.chip_ver=S01 \
	ro.mediatek.gemini_support=true \
	ro.mediatek.platform=MT6589 \
	ro.mediatek.version.branch=ALPS.JB2.MP \
	ro.mediatek.version.release=ALPS.JB2.MP.V1.2 \
	ro.mediatek.version.sdk=1 \
	ro.mediatek.wlan.p2p=1 \
	ro.mediatek.wlan.wsc=1 \
	ro.opengles.version=131072 \
	ro.sf.lcd_density=320 \
	ro.telephony.ril_class=MediaTekRIL \
	wifi.direct.interface=p2p0 \
	wifi.interface=wlan0 \
	wifi.tethering.interface=ap0

PRODUCT_TAGS += dalvik.gc.type-precise

PRODUCT_PACKAGES += \
	gsm0710muxd

# audio
PRODUCT_PACKAGES += \
	audio.r_submix.default \
	audio.a2dp.default \
	libblisrc
# wifi
PRODUCT_PACKAGES += \
	lib_driver_cmd_mtk

PRODUCT_PACKAGES += \
	Torch

PRODUCT_COPY_FILES += \
	$(LOCAL_PATH)/wpa_supplicant_overlay.conf:system/etc/wifi/wpa_supplicant_overlay.conf \
	$(LOCAL_PATH)/p2p_supplicant_overlay.conf:system/etc/wifi/p2p_supplicant_overlay.conf \
	$(LOCAL_PATH)/wpa_supplicant.conf:system/etc/wifi/wpa_supplicant.conf

$(call inherit-product, frameworks/native/build/phone-xhdpi-1024-dalvik-heap.mk)

