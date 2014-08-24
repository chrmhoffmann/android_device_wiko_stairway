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

/* taken from wpa_supplicant code as it doesn't export this function */
int mtk_wpa_driver_nl80211_set_country(void *priv, const char *alpha2_arg)
{
	struct i802_bss *bss = priv;
	struct wpa_driver_nl80211_data *drv = bss->drv;
	char alpha2[3];
	struct nl_msg *msg;

	msg = nlmsg_alloc();
	if (!msg)
		return -ENOMEM;

	alpha2[0] = alpha2_arg[0];
	alpha2[1] = alpha2_arg[1];
	alpha2[2] = '\0';

	genlmsg_put(msg, 0, 0, drv->global->nl80211_id,
		    0, 0, NL80211_CMD_REQ_SET_REG, 0);

	NLA_PUT_STRING(msg, NL80211_ATTR_REG_ALPHA2, alpha2);
	if (send_and_recv_msgs(drv, msg, NULL, NULL))
		return -EINVAL;
	return 0;
nla_put_failure:
	nlmsg_free(msg);
	return -EINVAL;
}

int wpa_driver_nl80211_driver_cmd(void *priv, char *cmd, char *buf, size_t buf_len)
{
	struct i802_bss *bss = priv;
	struct wpa_driver_nl80211_data *drv = bss->drv;
	int ret = - 1;

	ALOGI("%s: %s", __func__, cmd);

	if (os_strcasecmp(cmd, "start") == 0) {
		if ((ret = linux_set_iface_flags(drv->global->ioctl_sock,
						 drv->first_bss.ifname, 1)))
			ALOGE("nl80211: Could not set interface UP\n");
		wpa_msg(drv->ctx, MSG_INFO, WPA_EVENT_DRIVER_STATE "STARTED");
		wpa_printf(MSG_DEBUG,"DRIVER-START: %d", ret);
	} else if (os_strcasecmp(cmd, "stop") == 0) {
		if ((ret = linux_set_iface_flags(drv->global->ioctl_sock,
						 drv->first_bss.ifname, 0)))
			ALOGE("nl80211: Could not set interface DOWN\n");
		wpa_msg(drv->ctx, MSG_INFO, WPA_EVENT_DRIVER_STATE "STOPPED");
		wpa_printf(MSG_DEBUG,"DRIVER-STOP: %d", ret);
	} else if (os_strcasecmp(cmd, "macaddr") == 0) {
		u8 macaddr[ETH_ALEN] = {};

		ret = linux_get_ifhwaddr(drv->global->ioctl_sock, bss->ifname, macaddr);
		if (!ret)
			ret = os_snprintf(buf, buf_len,
					  "Macaddr = " MACSTR "\n",
					  MAC2STR(macaddr));
	} else if (os_strncasecmp(cmd, "country ", 8) == 0) {
		return mtk_wpa_driver_nl80211_set_country(priv, cmd + 8);
	}

	return ret;
}
