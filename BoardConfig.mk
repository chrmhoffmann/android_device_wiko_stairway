# Copyright (C) 2013 The CyanogenMod Project
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

DEVICE_FOLDER := device/wiko/stairway

TARGET_ARCH := arm
TARGET_CPU_ABI := armeabi-v7a
TARGET_CPU_ABI2 := armeabi
TARGET_CPU_SMP := true
TARGET_ARCH_VARIANT := armv7-a-neon

TARGET_NO_BOOTLOADER := true

BOARD_BOOTIMAGE_PARTITION_SIZE := 6291456
BOARD_KERNEL_PAGESIZE := 2048
BOARD_FLASH_BLOCK_SIZE := 4096
# TODO: temporarily point to recovery to avoid build break
TARGET_PREBUILT_KERNEL := $(DEVICE_FOLDER)/recovery/kernel

BOARD_RECOVERYIMAGE_PARTITION_SIZE := 6291456
TARGET_RECOVERY_FSTAB := $(DEVICE_FOLDER)/recovery/recovery.fstab
TARGET_PREBUILT_RECOVERY_KERNEL := $(DEVICE_FOLDER)/recovery/kernel
BOARD_HAS_NO_SELECT_BUTTON := true

BOARD_CUSTOM_BOOTIMG_MK := $(DEVICE_FOLDER)/boot.mk
