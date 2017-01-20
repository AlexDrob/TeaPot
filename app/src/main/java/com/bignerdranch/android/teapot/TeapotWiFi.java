package com.bignerdranch.android.teapot;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by AREG on 19.01.2017.
 */

public class TeapotWiFi {

    private Context mContext;
    private WifiInfo InfoWiFi;
    private WifiManager mManagerWiFi;

    public TeapotWiFi(Context context) {
        // Get the WifiManager service from the context
        mContext = context;
        mManagerWiFi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
    }

    // return True if WiFi is on, False otherwise
    public boolean TeapotCurrentWiFiState() {
        return mManagerWiFi.isWifiEnabled();
    }

    // return True if device is connected to WiFi, False otherwise
    public boolean TeapotIsConnectedToWiFi() {
        InfoWiFi = mManagerWiFi.getConnectionInfo();

        if (InfoWiFi == null) {
            return false;
        }
        if( InfoWiFi.getNetworkId() == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public String TeapotSSIDnetwork() {
        return InfoWiFi.getSSID();
    }
}
