package com.project.weijinwen.qihong;

/**
 * Created by weijinwen on 14-9-27.
 */

import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.UUID;

public class QH_DeviceInfo {
    public QH_DeviceInfo(QH_Activity_Signin activity) {
        mQHSigninActivity = activity;
        mTelephonyManager = (TelephonyManager)activity.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public String GetDeviceID() {
        String tmDevice, tmSerial, androidId;

        tmDevice = "" + mTelephonyManager.getDeviceId();
        tmSerial = "" + mTelephonyManager.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(mQHSigninActivity.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }

    public String GetPlatform() {
        return "android" ;// + mTelephonyManager.getDeviceSoftwareVersion();
    }

    private TelephonyManager mTelephonyManager;
    private QH_Activity_Signin mQHSigninActivity;
}

//        sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());
//        sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());
//        sb.append("\nLine1Number = " + tm.getLine1Number());
//        sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());
//        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());
//        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());
//        sb.append("\nNetworkType = " + tm.getNetworkType());
//        sb.append("\nPhoneType = " + tm.getPhoneType());
//        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
//        sb.append("\nSimOperator = " + tm.getSimOperator());
//        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
//        sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
//        sb.append("\nSimState = " + tm.getSimState());
//        sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
//        sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());
//        Log.e("info", sb.toString());