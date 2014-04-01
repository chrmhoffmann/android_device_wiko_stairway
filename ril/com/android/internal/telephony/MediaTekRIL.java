/*
 * Copyright (C) 2014 The OmniROM Project <http://www.omnirom.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.telephony;

import static com.android.internal.telephony.RILConstants.*;

import android.content.Context;
import android.os.AsyncResult;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.SignalStrength;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.UiccController;

public class MediaTekRIL extends RIL implements CommandsInterface {

    // MediaTek Custom States
    static final int RIL_REQUEST_MTK_BASE = 2000;
    static final int RIL_REQUEST_HANGUP_ALL = (RIL_REQUEST_MTK_BASE + 0);
    static final int RIL_REQUEST_GET_COLP = (RIL_REQUEST_MTK_BASE + 1);
    static final int RIL_REQUEST_SET_COLP = (RIL_REQUEST_MTK_BASE + 2);
    static final int RIL_REQUEST_GET_COLR = (RIL_REQUEST_MTK_BASE + 3);
    static final int RIL_REQUEST_GET_CCM = (RIL_REQUEST_MTK_BASE + 4);
    static final int RIL_REQUEST_GET_ACM = (RIL_REQUEST_MTK_BASE + 5);
    static final int RIL_REQUEST_GET_ACMMAX = (RIL_REQUEST_MTK_BASE + 6);
    static final int RIL_REQUEST_GET_PPU_AND_CURRENCY = (RIL_REQUEST_MTK_BASE + 7);
    static final int RIL_REQUEST_SET_ACMMAX = (RIL_REQUEST_MTK_BASE + 8);
    static final int RIL_REQUEST_RESET_ACM = (RIL_REQUEST_MTK_BASE + 9);
    static final int RIL_REQUEST_SET_PPU_AND_CURRENCY = (RIL_REQUEST_MTK_BASE + 10);
    static final int RIL_REQUEST_RADIO_POWEROFF = (RIL_REQUEST_MTK_BASE + 11);       
    static final int RIL_REQUEST_DUAL_SIM_MODE_SWITCH = (RIL_REQUEST_MTK_BASE + 12); 
    static final int RIL_REQUEST_QUERY_PHB_STORAGE_INFO = (RIL_REQUEST_MTK_BASE + 13);       
    static final int RIL_REQUEST_WRITE_PHB_ENTRY = (RIL_REQUEST_MTK_BASE + 14);      
    static final int RIL_REQUEST_READ_PHB_ENTRY = (RIL_REQUEST_MTK_BASE + 15);       
    static final int RIL_REQUEST_SET_GPRS_CONNECT_TYPE = (RIL_REQUEST_MTK_BASE + 16);
    static final int RIL_REQUEST_SET_GPRS_TRANSFER_TYPE = (RIL_REQUEST_MTK_BASE + 17);
    static final int RIL_REQUEST_MOBILEREVISION_AND_IMEI = (RIL_REQUEST_MTK_BASE + 18);//Add by mtk80372 for Barcode Number
    static final int RIL_REQUEST_QUERY_SIM_NETWORK_LOCK = (RIL_REQUEST_MTK_BASE + 19);
    static final int RIL_REQUEST_SET_SIM_NETWORK_LOCK = (RIL_REQUEST_MTK_BASE + 20);
    static final int RIL_REQUEST_SET_SCRI = (RIL_REQUEST_MTK_BASE + 21);   
    /* cage_vt start */
    static final int RIL_REQUEST_VT_DIAL = (RIL_REQUEST_MTK_BASE + 22);
    static final int RIL_REQUEST_VOICE_ACCEPT = (RIL_REQUEST_MTK_BASE + 32);
    /* cage_vt end */
    static final int RIL_REQUEST_BTSIM_CONNECT = (RIL_REQUEST_MTK_BASE + 23);
    static final int RIL_REQUEST_BTSIM_DISCONNECT_OR_POWEROFF = (RIL_REQUEST_MTK_BASE + 24);
    static final int RIL_REQUEST_BTSIM_POWERON_OR_RESETSIM = (RIL_REQUEST_MTK_BASE + 25);
    static final int RIL_REQUEST_BTSIM_TRANSFERAPDU = (RIL_REQUEST_MTK_BASE + 26);
    static final int RIL_REQUEST_EMERGENCY_DIAL = (RIL_REQUEST_MTK_BASE + 27);
    static final int RIL_REQUEST_SET_NETWORK_SELECTION_MANUAL_WITH_ACT = (RIL_REQUEST_MTK_BASE + 28);
    static final int RIL_REQUEST_QUERY_ICCID = (RIL_REQUEST_MTK_BASE + 29);
    static final int RIL_REQUEST_SIM_AUTHENTICATION = (RIL_REQUEST_MTK_BASE + 30);   
    static final int RIL_REQUEST_USIM_AUTHENTICATION = (RIL_REQUEST_MTK_BASE + 31); 
    static final int RIL_REQUEST_RADIO_POWERON = (RIL_REQUEST_MTK_BASE + 33);
    static final int RIL_REQUEST_GET_SMS_SIM_MEM_STATUS = (RIL_REQUEST_MTK_BASE + 34);
    static final int RIL_REQUEST_FORCE_RELEASE_CALL = (RIL_REQUEST_MTK_BASE + 35);
    static final int RIL_REQUEST_SET_CALL_INDICATION = (RIL_REQUEST_MTK_BASE + 36);
    static final int RIL_REQUEST_REPLACE_VT_CALL = (RIL_REQUEST_MTK_BASE + 37);
    /* 3G switch start */
    static final int RIL_REQUEST_GET_3G_CAPABILITY = (RIL_REQUEST_MTK_BASE + 38);
    static final int RIL_REQUEST_SET_3G_CAPABILITY = (RIL_REQUEST_MTK_BASE + 39);
    /* 3G switch end */
    /* User controlled PLMN selector with Access Technology  begin */
    static final int RIL_REQUEST_GET_POL_CAPABILITY = (RIL_REQUEST_MTK_BASE + 40);
    static final int RIL_REQUEST_GET_POL_LIST = (RIL_REQUEST_MTK_BASE + 41);
    static final int RIL_REQUEST_SET_POL_ENTRY = (RIL_REQUEST_MTK_BASE + 42);
    /* User controlled PLMN selector with Access Technology  end */
    /* UPB start */
    static final int RIL_REQUEST_QUERY_UPB_CAPABILITY = (RIL_REQUEST_MTK_BASE + 43);
    static final int RIL_REQUEST_EDIT_UPB_ENTRY = (RIL_REQUEST_MTK_BASE + 44);
    static final int RIL_REQUEST_DELETE_UPB_ENTRY = (RIL_REQUEST_MTK_BASE + 45);
    static final int RIL_REQUEST_READ_UPB_GAS_LIST = (RIL_REQUEST_MTK_BASE + 46);
    static final int RIL_REQUEST_READ_UPB_GRP = (RIL_REQUEST_MTK_BASE + 47);
    static final int RIL_REQUEST_WRITE_UPB_GRP = (RIL_REQUEST_MTK_BASE + 48);
    /* UPB end */
    static final int RIL_REQUEST_DISABLE_VT_CAPABILITY = (RIL_REQUEST_MTK_BASE + 49);
    static final int RIL_REQUEST_HANGUP_ALL_EX = (RIL_REQUEST_MTK_BASE + 50);
    static final int RIL_REQUEST_SET_SIM_RECOVERY_ON = (RIL_REQUEST_MTK_BASE + 51);
    static final int RIL_REQUEST_GET_SIM_RECOVERY_ON = (RIL_REQUEST_MTK_BASE + 52);
    static final int RIL_REQUEST_SET_TRM = (RIL_REQUEST_MTK_BASE + 53);
    static final int RIL_REQUEST_DETECT_SIM_MISSING = (RIL_REQUEST_MTK_BASE + 54);
    static final int RIL_REQUEST_GET_CALIBRATION_DATA = (RIL_REQUEST_MTK_BASE + 55);

     //For LGE APIs start
    static final int RIL_REQUEST_GET_PHB_STRING_LENGTH = (RIL_REQUEST_MTK_BASE + 56);
    static final int RIL_REQUEST_GET_PHB_MEM_STORAGE = (RIL_REQUEST_MTK_BASE + 57);
    static final int RIL_REQUEST_SET_PHB_MEM_STORAGE = (RIL_REQUEST_MTK_BASE + 58);
    static final int RIL_REQUEST_READ_PHB_ENTRY_EXT = (RIL_REQUEST_MTK_BASE + 59);
    static final int RIL_REQUEST_WRITE_PHB_ENTRY_EXT = (RIL_REQUEST_MTK_BASE + 60);
    
    // requests for read/write EFsmsp
    static final int RIL_REQUEST_GET_SMS_PARAMS = (RIL_REQUEST_MTK_BASE + 61);
    static final int RIL_REQUEST_SET_SMS_PARAMS = (RIL_REQUEST_MTK_BASE + 62);

    // NFC SEEK start
    static final int RIL_REQUEST_SIM_TRANSMIT_BASIC = (RIL_REQUEST_MTK_BASE + 63);
    static final int RIL_REQUEST_SIM_OPEN_CHANNEL = (RIL_REQUEST_MTK_BASE + 64);
    static final int RIL_REQUEST_SIM_CLOSE_CHANNEL = (RIL_REQUEST_MTK_BASE + 65);
    static final int RIL_REQUEST_SIM_TRANSMIT_CHANNEL = (RIL_REQUEST_MTK_BASE + 66);
    static final int RIL_REQUEST_SIM_GET_ATR = (RIL_REQUEST_MTK_BASE + 67);
    // NFC SEEK end

    // CB extension
    static final int RIL_REQUEST_SET_CB_CHANNEL_CONFIG_INFO = (RIL_REQUEST_MTK_BASE + 68);
    static final int RIL_REQUEST_SET_CB_LANGUAGE_CONFIG_INFO = (RIL_REQUEST_MTK_BASE + 69);
    static final int RIL_REQUEST_GET_CB_CONFIG_INFO = (RIL_REQUEST_MTK_BASE + 70);
    static final int RIL_REQUEST_SET_ALL_CB_LANGUAGE_ON = (RIL_REQUEST_MTK_BASE + 71);
    // CB extension
    
    static final int RIL_REQUEST_SET_ETWS = (RIL_REQUEST_MTK_BASE + 72);

    // [New R8 modem FD]
    static final int RIL_REQUEST_SET_FD_MODE = (RIL_REQUEST_MTK_BASE + 73);

    static final int RIL_REQUEST_SIM_OPEN_CHANNEL_WITH_SW = (RIL_REQUEST_MTK_BASE + 74); // NFC SEEK

    static final int RIL_REQUEST_SET_CLIP = (RIL_REQUEST_MTK_BASE + 75);

    //MTK-START [mtk80776] WiFi Calling
    static final int RIL_REQUEST_UICC_SELECT_APPLICATION = (RIL_REQUEST_MTK_BASE + 76);
    static final int RIL_REQUEST_UICC_DEACTIVATE_APPLICATION = (RIL_REQUEST_MTK_BASE + 77);
    static final int RIL_REQUEST_UICC_APPLICATION_IO = (RIL_REQUEST_MTK_BASE + 78);
    static final int RIL_REQUEST_UICC_AKA_AUTHENTICATE = (RIL_REQUEST_MTK_BASE + 79);
    static final int RIL_REQUEST_UICC_GBA_AUTHENTICATE_BOOTSTRAP = (RIL_REQUEST_MTK_BASE + 80);
    static final int RIL_REQUEST_UICC_GBA_AUTHENTICATE_NAF = (RIL_REQUEST_MTK_BASE + 81);
    //MTK-END [mtk80776] WiFi Calling
    static final int RIL_REQUEST_STK_EVDL_CALL_BY_AP = (RIL_REQUEST_MTK_BASE + 82);

    static final int RIL_UNSOL_MTK_BASE = 3000; 
    static final int RIL_UNSOL_NEIGHBORING_CELL_INFO = (RIL_UNSOL_MTK_BASE + 0);
    static final int RIL_UNSOL_NETWORK_INFO = (RIL_UNSOL_MTK_BASE + 1);
    static final int RIL_UNSOL_CALL_FORWARDING = (RIL_UNSOL_MTK_BASE + 2);
    static final int RIL_UNSOL_CRSS_NOTIFICATION = (RIL_UNSOL_MTK_BASE + 3);
    static final int RIL_UNSOL_CALL_PROGRESS_INFO = (RIL_UNSOL_MTK_BASE + 4);
    static final int RIL_UNSOL_PHB_READY_NOTIFICATION = (RIL_UNSOL_MTK_BASE + 5);
    static final int RIL_UNSOL_SPEECH_INFO = (RIL_UNSOL_MTK_BASE + 6);
    static final int RIL_UNSOL_SIM_INSERTED_STATUS = (RIL_UNSOL_MTK_BASE + 7);
    static final int RIL_UNSOL_RADIO_TEMPORARILY_UNAVAILABLE = (RIL_UNSOL_MTK_BASE + 8);
    static final int RIL_UNSOL_ME_SMS_STORAGE_FULL = (RIL_UNSOL_MTK_BASE + 9);
    static final int RIL_UNSOL_SMS_READY_NOTIFICATION = (RIL_UNSOL_MTK_BASE + 10);
    static final int RIL_UNSOL_SCRI_RESULT = (RIL_UNSOL_MTK_BASE + 11);
    /* cage_vt start */
    static final int RIL_UNSOL_VT_STATUS_INFO = (RIL_UNSOL_MTK_BASE + 12);
    static final int RIL_UNSOL_VT_RING_INFO = (RIL_UNSOL_MTK_BASE + 13);
    /* cage_vt end */
    static final int RIL_UNSOL_INCOMING_CALL_INDICATION = (RIL_UNSOL_MTK_BASE + 14);
    static final int RIL_UNSOL_SIM_MISSING = (RIL_UNSOL_MTK_BASE + 15);
    static final int RIL_UNSOL_GPRS_DETACH = (RIL_UNSOL_MTK_BASE + 16);
    //MTK-START [mtk04070][120208][ALPS00233196] ATCI for unsolicited response
    static final int RIL_UNSOL_ATCI_RESPONSE = (RIL_UNSOL_MTK_BASE + 17);
    //MTK-END [mtk04070][120208][ALPS00233196] ATCI for unsolicited response
    static final int RIL_UNSOL_SIM_RECOVERY= (RIL_UNSOL_MTK_BASE + 18);
    static final int RIL_UNSOL_VIRTUAL_SIM_ON = (RIL_UNSOL_MTK_BASE + 19);
    static final int RIL_UNSOL_VIRTUAL_SIM_OFF = (RIL_UNSOL_MTK_BASE + 20);
    static final int RIL_UNSOL_INVALID_SIM = (RIL_UNSOL_MTK_BASE + 21); 
    static final int RIL_UNSOL_RESPONSE_PS_NETWORK_STATE_CHANGED = (RIL_UNSOL_MTK_BASE + 22);
    static final int RIL_UNSOL_RESPONSE_ACMT = (RIL_UNSOL_MTK_BASE + 23);
    static final int RIL_UNSOL_EF_CSP_PLMN_MODE_BIT = (RIL_UNSOL_MTK_BASE + 24);
    static final int RIL_UNSOL_IMEI_LOCK = (RIL_UNSOL_MTK_BASE + 25);
    static final int RIL_UNSOL_RESPONSE_MMRR_STATUS_CHANGED = (RIL_UNSOL_MTK_BASE + 26);
    static final int RIL_UNSOL_SIM_PLUG_OUT = (RIL_UNSOL_MTK_BASE + 27);
    static final int RIL_UNSOL_SIM_PLUG_IN = (RIL_UNSOL_MTK_BASE + 28);
    static final int RIL_UNSOL_RESPONSE_ETWS_NOTIFICATION = (RIL_UNSOL_MTK_BASE + 29);
    static final int RIL_UNSOL_CNAP = (RIL_UNSOL_MTK_BASE + 30);
    static final int RIL_UNSOL_STK_EVDL_CALL = (RIL_UNSOL_MTK_BASE + 31);

    // TODO: Support multiSIM
    // Sim IDs are 0 / 1
    int mSimId = 0;


    public MediaTekRIL(Context context, int networkMode, int cdmaSubscription) {
	    super(context, networkMode, cdmaSubscription, null);
    }

    public MediaTekRIL(Context context, int networkMode, int cdmaSubscription, Integer instanceId) {
	    super(context, networkMode, cdmaSubscription, instanceId);
    }

    public static byte[] hexStringToBytes(String s) {
        byte[] ret;

        if (s == null) return null;

        int len = s.length();
        ret = new byte[len/2];

        for (int i=0 ; i <len ; i+=2) {
            ret[i/2] = (byte) ((hexCharToInt(s.charAt(i)) << 4)
                                | hexCharToInt(s.charAt(i+1)));
        }

        return ret;
    }

    static int hexCharToInt(char c) {
         if (c >= '0' && c <= '9') return (c - '0');
         if (c >= 'A' && c <= 'F') return (c - 'A' + 10);
         if (c >= 'a' && c <= 'f') return (c - 'a' + 10);

         throw new RuntimeException ("invalid hex char '" + c + "'");
    }

    protected Object
    responseOperatorInfos(Parcel p) {
        String strings[] = (String [])responseStrings(p);
        ArrayList<OperatorInfo> ret;

        if (strings.length % 5 != 0) {
            throw new RuntimeException(
                "RIL_REQUEST_QUERY_AVAILABLE_NETWORKS: invalid response. Got "
                + strings.length + " strings, expected multible of 5");
        }

        String lacStr = SystemProperties.get("gsm.cops.lac");
        boolean lacValid = false;
        int lacIndex=0;

        Rlog.d(LOG_TAG, "lacStr = " + lacStr+" lacStr.length="+lacStr.length()+" strings.length="+strings.length);
        if((lacStr.length() > 0) && (lacStr.length()%4 == 0) && ((lacStr.length()/4) == (strings.length/5 ))){
            Rlog.d(LOG_TAG, "lacValid set to true");
            lacValid = true;
        }

        SystemProperties.set("gsm.cops.lac","");

        ret = new ArrayList<OperatorInfo>(strings.length / 5);

        for (int i = 0 ; i < strings.length ; i += 5) {
            if((strings[i+0] != null) && (strings[i+0].startsWith("uCs2") == true)) {        
                riljLog("responseOperatorInfos handling UCS2 format name");

                try {
                    strings[i+0] = new String(hexStringToBytes(strings[i+0].substring(4)), "UTF-16");
                } catch(UnsupportedEncodingException ex) {
                    riljLog("responseOperatorInfos UnsupportedEncodingException");
                }
            }

            if ((lacValid == true) && (strings[i] != null)) {
                UiccController uiccController = UiccController.getInstance();
                IccRecords iccRecords = uiccController.getIccRecords(UiccController.APP_FAM_3GPP);
                int lacValue = -1;
                String sEons = null;
                String lac = lacStr.substring(lacIndex,lacIndex+4);
                Rlog.d(LOG_TAG, "lacIndex="+lacIndex+" lacValue="+lacValue+" lac="+lac+" plmn numeric="+strings[i+2]+" plmn name"+strings[i+0]);

                if(lac != "") {
                    lacValue = Integer.parseInt(lac, 16);
                    lacIndex += 4;
                    if(lacValue != 0xfffe) {
                        /*sEons = iccRecords.getEonsIfExist(strings[i+2],lacValue,true);
                        if(sEons != null) {
                            strings[i] = sEons;           
                            Rlog.d(LOG_TAG, "plmn name update to Eons: "+strings[i]);
                        }*/
                    } else {
                        Rlog.d(LOG_TAG, "invalid lac ignored");
                    }
                }
            }

            if (strings[i] != null && (strings[i].equals("") || strings[i].equals(strings[i+2]))) {
                riljLog("lookup RIL responseOperatorInfos()");
                // Operator MCC/MNC is given here for the current simcard. Too lazy to convert for now.
                strings[i] = strings[i+2]; // long name
                strings[i+1] = strings[i+2]; // short name
            }

            // 1, 2 = 2G
            // > 2 = 3G
            String property_name = "gsm.baseband.capability";
            if(mSimId > 0) {
                property_name = property_name + (mSimId+1);
            }

            int basebandCapability = SystemProperties.getInt(property_name, 3);
            Rlog.d(LOG_TAG, "property_name="+property_name+", basebandCapability=" + basebandCapability);
            if (3 < basebandCapability) {
                strings[i+0] = strings[i+0].concat(" " + strings[i+4]);
                strings[i+1] = strings[i+1].concat(" " + strings[i+4]);
            }

            ret.add(
                new OperatorInfo(
                    strings[i+0],
                    strings[i+1],
                    strings[i+2],
                    strings[i+3]));
        }

        return ret;
    }

    private Object
    responseCrssNotification(Parcel p) {
        /*SuppCrssNotification notification = new SuppCrssNotification();

        notification.code = p.readInt();
        notification.type = p.readInt();
        notification.number = p.readString();
        notification.alphaid = p.readString();
        notification.cli_validity = p.readInt();

        return notification;*/

        Rlog.e(LOG_TAG, "NOT PROCESSING CRSS NOTIFICATION");
        return null;
    }

    private Object responseEtwsNotification(Parcel p) {
        /*EtwsNotification response = new EtwsNotification();
        
        response.warningType = p.readInt();
        response.messageId = p.readInt();
        response.serialNumber = p.readInt();
        response.plmnId = p.readString();
        response.securityInfo = p.readString();
        
        return response;*/
        Rlog.e(LOG_TAG, "NOT PROCESSING ETWS NOTIFICATION");

        return null;
    }

    @Override
    protected void
    processUnsolicited (Parcel p) {
        Object ret;
        int dataPosition = p.dataPosition(); // save off position within the Parcel
        int response = p.readInt();

        switch(response) {
            case RIL_UNSOL_NEIGHBORING_CELL_INFO: ret = responseStrings(p); break;          
            case RIL_UNSOL_NETWORK_INFO: ret = responseStrings(p); break;           
            case RIL_UNSOL_CALL_FORWARDING: ret = responseInts(p); break;
            case RIL_UNSOL_CRSS_NOTIFICATION: ret = responseCrssNotification(p); break;
            case RIL_UNSOL_CALL_PROGRESS_INFO: ret = responseStrings(p); break;         
            case RIL_UNSOL_PHB_READY_NOTIFICATION: ret = responseVoid(p); break;
            case RIL_UNSOL_SIM_INSERTED_STATUS: ret = responseInts(p); break;            
            case RIL_UNSOL_SIM_MISSING: ret = responseInts(p); break;   
            case RIL_UNSOL_SIM_RECOVERY: ret = responseInts(p); break;         
            case RIL_UNSOL_VIRTUAL_SIM_ON: ret = responseInts(p); break; 
            case RIL_UNSOL_VIRTUAL_SIM_OFF: ret = responseInts(p); break; 
            case RIL_UNSOL_SPEECH_INFO: ret = responseInts(p); break;           
            case RIL_UNSOL_RADIO_TEMPORARILY_UNAVAILABLE: ret = responseInts(p); break; 
            case RIL_UNSOL_ME_SMS_STORAGE_FULL: ret =  responseVoid(p); break;
            case RIL_UNSOL_SMS_READY_NOTIFICATION: ret = responseVoid(p); break;
            case RIL_UNSOL_VT_STATUS_INFO: ret = responseInts(p); break;
            case RIL_UNSOL_VT_RING_INFO: ret = responseVoid(p); break;
            case RIL_UNSOL_SCRI_RESULT: ret = responseInts(p); break;
            case RIL_UNSOL_GPRS_DETACH: ret = responseVoid(p); break;
            case RIL_UNSOL_INCOMING_CALL_INDICATION: ret = responseStrings(p); break;
            case RIL_UNSOL_EF_CSP_PLMN_MODE_BIT: ret = responseInts(p); break;
            case RIL_UNSOL_RESPONSE_PS_NETWORK_STATE_CHANGED: ret =  responseVoid(p); break;
            case RIL_UNSOL_INVALID_SIM:  ret = responseStrings(p); break;
            case RIL_UNSOL_RESPONSE_ACMT: ret = responseInts(p); break;
            case RIL_UNSOL_IMEI_LOCK: ret = responseVoid(p); break;
            case RIL_UNSOL_RESPONSE_MMRR_STATUS_CHANGED: ret = responseInts(p); break;
            case RIL_UNSOL_SIM_PLUG_OUT: ret = responseInts(p); break;
            case RIL_UNSOL_SIM_PLUG_IN: ret = responseInts(p); break;
            case RIL_UNSOL_RESPONSE_ETWS_NOTIFICATION: ret = responseEtwsNotification(p); break;
            case RIL_UNSOL_CNAP: ret = responseStrings(p); break;
            case RIL_UNSOL_STK_EVDL_CALL: ret = responseInts(p); break;            
            case RIL_UNSOL_RESPONSE_RADIO_STATE_CHANGED: ret =  responseVoid(p); break;
            default:
                // Rewind the Parcel
                p.setDataPosition(dataPosition);

                // Forward responses that we are not overriding to the super class
                super.processUnsolicited(p);
                return;
        }

        // To avoid duplicating code from RIL.java, we rewrite some response codes to fit
        // AOSP's one (when they do the same effect)
        boolean rewindAndReplace = false;
        int newResponseCode = 0;

        switch (response) {
            case RIL_UNSOL_CALL_PROGRESS_INFO:
		rewindAndReplace = true;
		newResponseCode = RIL_UNSOL_RESPONSE_CALL_STATE_CHANGED;
		break;

            case RIL_UNSOL_INCOMING_CALL_INDICATION:
		setCallIndication((String[])ret);
                rewindAndReplace = true;
		newResponseCode = RIL_UNSOL_RESPONSE_CALL_STATE_CHANGED;
		break;

            case RIL_UNSOL_RESPONSE_PS_NETWORK_STATE_CHANGED:
                rewindAndReplace = true;
                newResponseCode = RIL_UNSOL_RESPONSE_VOICE_NETWORK_STATE_CHANGED;
                break;

            case RIL_UNSOL_SIM_INSERTED_STATUS:
            case RIL_UNSOL_SIM_MISSING:
            case RIL_UNSOL_SIM_PLUG_OUT:
            case RIL_UNSOL_SIM_PLUG_IN:
                rewindAndReplace = true;
                newResponseCode = RIL_UNSOL_RESPONSE_SIM_STATUS_CHANGED;
                break;

            case RIL_UNSOL_SMS_READY_NOTIFICATION:
                /*if (mGsmSmsRegistrant != null) {
                    mGsmSmsRegistrant
                        .notifyRegistrant();
                }*/
                break;
            case RIL_UNSOL_RESPONSE_RADIO_STATE_CHANGED:
		// intercept and send GPRS_TRANSFER_TYPE and GPRS_CONNECT_TYPE to RIL
	        setRadioStateFromRILInt(p.readInt());
		rewindAndReplace = true;
		newResponseCode = RIL_UNSOL_RESPONSE_RADIO_STATE_CHANGED;
		break;
            default:
                Rlog.i(LOG_TAG, "Unprocessed unsolicited known MTK response: " + response);
        }

        if (rewindAndReplace) {
            Rlog.w(LOG_TAG, "Rewriting MTK unsolicited response to " + newResponseCode);

            // Rewrite
            p.setDataPosition(dataPosition);
            p.writeInt(newResponseCode);

            // And rewind again in front
            p.setDataPosition(dataPosition);

            super.processUnsolicited(p);
        }
    }

    private
    void setCallIndication(String[] incomingCallInfo) {
	RILRequest rr
            = RILRequest.obtain(RIL_REQUEST_SET_CALL_INDICATION, null);

	int callId = Integer.parseInt(incomingCallInfo[0]);
        int callMode = Integer.parseInt(incomingCallInfo[3]);
        int seqNumber = Integer.parseInt(incomingCallInfo[4]);

	// some guess work is needed here, for now, just 0
	callMode = 0;

        rr.mParcel.writeInt(3);

        rr.mParcel.writeInt(callMode);
        rr.mParcel.writeInt(callId);
        rr.mParcel.writeInt(seqNumber);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> "
            + requestToString(rr.mRequest) + " " + callMode + " " + callId + " " + seqNumber);

        send(rr);
    }

    // Override setupDataCall as the MTK RIL needs 8th param CID (hardwired to 1?)
    @Override
    public void
    setupDataCall(String radioTechnology, String profile, String apn,
            String user, String password, String authType, String protocol,
            Message result) {
        RILRequest rr
                = RILRequest.obtain(RIL_REQUEST_SETUP_DATA_CALL, result);

        rr.mParcel.writeInt(8);

        rr.mParcel.writeString(radioTechnology);
        rr.mParcel.writeString(profile);
        rr.mParcel.writeString(apn);
        rr.mParcel.writeString(user);
        rr.mParcel.writeString(password);
        rr.mParcel.writeString(authType);
        rr.mParcel.writeString(protocol);
        rr.mParcel.writeString("1");

        if (RILJ_LOGD) riljLog(rr.serialString() + "> "
                + requestToString(rr.mRequest) + " " + radioTechnology + " "
                + profile + " " + apn + " " + user + " "
                + password + " " + authType + " " + protocol + "1");

        send(rr);
    }

    protected Object
    responseSignalStrength(Parcel p) {
        SignalStrength s = SignalStrength.makeSignalStrengthFromRilParcel(p);
	return new SignalStrength(s.getGsmSignalStrength(), 
				  s.getGsmBitErrorRate(),
				  s.getCdmaDbm(), 
				  s.getCdmaEcio(),
				  s.getEvdoDbm(), 
				  s.getEvdoEcio(),
				  s.getEvdoSnr(),
				  true);
    }

    private void setRadioStateFromRILInt (int stateCode) {
        switch (stateCode) {
	case 0: case 1: break; // radio off
	default:
	    {
	        RILRequest rr = RILRequest.obtain(RIL_REQUEST_SET_GPRS_TRANSFER_TYPE, null);

		if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

		rr.mParcel.writeInt(1);
		rr.mParcel.writeInt(1);

		send(rr);
	    }
	    {
	        RILRequest rr = RILRequest.obtain(RIL_REQUEST_SET_GPRS_CONNECT_TYPE, null);

		if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

		rr.mParcel.writeInt(1);
		rr.mParcel.writeInt(1);

		send(rr);
	    }
	}
    }

    @Override
    public void
    setRadioPower(boolean on, Message result) {
        boolean allow = SystemProperties.getBoolean("persist.ril.enable", true);
        if (!allow) {
            return;
        }

        RILRequest rr = RILRequest.obtain(RIL_REQUEST_DUAL_SIM_MODE_SWITCH, result);

        if (RILJ_LOGD) riljLog(rr.serialString() + "> " + requestToString(rr.mRequest));

        rr.mParcel.writeInt(1);
        rr.mParcel.writeInt(on ? 3 : 0); // SIM1 | SIM2 ?

        send(rr);
    }

    @Override
    public void setUiccSubscription(int slotId, int appIndex, int subId,
				    int subStatus, Message result) {
	    if (RILJ_LOGD) riljLog("setUiccSubscription" + slotId + " " + appIndex + " " + subId + " " + subStatus);

	    // Fake response (note: should be sent before mSubscriptionStatusRegistrants or
	    // SubscriptionManager might not set the readiness correctly)
	    AsyncResult.forMessage(result, 0, null);
	    result.sendToTarget();

	    // TODO: Actually turn off/on the radio (and don't fight with the ServiceStateTracker)
	    if (subStatus == 1 /* ACTIVATE */) {
		    // Subscription changed: enabled
		    if (mSubscriptionStatusRegistrants != null) {
			    mSubscriptionStatusRegistrants.notifyRegistrants(
									     new AsyncResult (null, new int[] {1}, null));
		    }
	    } else if (subStatus == 0 /* DEACTIVATE */) {
		    // Subscription changed: disabled
		    if (mSubscriptionStatusRegistrants != null) {
			    mSubscriptionStatusRegistrants.notifyRegistrants(
									     new AsyncResult (null, new int[] {0}, null));
		    }
	    }
    }

    public void setDataSubscription(Message result) {
	    int simId = mInstanceId == null ? 0 : mInstanceId;
	    if (RILJ_LOGD) riljLog("Setting data subscription to " + simId);
    }

    public void setDefaultVoiceSub(int subIndex, Message response) {
	    // No need to inform the RIL on MTK
	    if (RILJ_LOGD) riljLog("Setting defaultvoice subscription to " + mInstanceId);
	    AsyncResult.forMessage(response, 0, null);
	    response.sendToTarget();
    }
}
