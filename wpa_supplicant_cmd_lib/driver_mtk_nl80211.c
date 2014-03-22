#include <net/if_arp.h>
#include <net/if.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/types.h>

#define LOG_TAG "wpa_supplicant_mtk"
#include <cutils/log.h>

#include "common.h"
#include "wpa_debug.h"
#include "linux_ioctl.h"
#include "hardware_legacy/driver_nl80211.h"

#define WPA_EVENT_DRIVER_STATE "CTRL-EVENT-DRIVER-STATE "

int wpa_driver_nl80211_driver_cmd(void *priv, char *cmd, char *buf, size_t buf_len)
{
	struct i802_bss *bss = priv;
	struct wpa_driver_nl80211_data *drv = bss->drv;
	struct nl_msg *msg, *cqm = NULL;
	int ret = - 1;

	ALOGE("%s: %s", __func__, cmd);

	if (os_strcasecmp(cmd, "start") == 0) {
		if ((ret = linux_set_iface_flags(drv->global->ioctl_sock,
						 drv->first_bss.ifname, 1)))
			ALOGE("nl80211: Could not set interface UP \n");

		wpa_msg(drv->ctx, MSG_INFO, WPA_EVENT_DRIVER_STATE "STARTED");
		wpa_printf(MSG_DEBUG,"DRIVER-START: %d", ret);
	} else if (os_strcasecmp(cmd, "stop") == 0) {
		if ((ret = linux_set_iface_flags(drv->global->ioctl_sock,
						 drv->first_bss.ifname, 0)))
			ALOGE("nl80211: Could not set interface Down \n");
		wpa_msg(drv->ctx, MSG_INFO, WPA_EVENT_DRIVER_STATE "STOPPED");
		wpa_printf(MSG_DEBUG,"DRIVER-STOP: %d", ret);
	}
	return ret;
}
