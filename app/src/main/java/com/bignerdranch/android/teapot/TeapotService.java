package com.bignerdranch.android.teapot;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class TeapotService extends Service {

    static private final String TAG = "TeapotService";

    private TeapotData data;
    private Timer mTimer;
    private TeapotUDPasyncTask UdpTask;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (data == null) {
            data = TeapotData.get();
        }
        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.schedule( new TimerTask() {
                @Override
                public void run() {
                    TeapotWiFi mTeapotWiFi = new TeapotWiFi(getApplicationContext());
                    if ((mTeapotWiFi.TeapotCurrentWiFiState() == true) & (mTeapotWiFi.TeapotIsConnectedToWiFi() == true)) {
                        String WiFiName = "\"" + data.getWiFiName() + "\"";
                        if (mTeapotWiFi.TeapotSSIDnetwork().equals(WiFiName)) {
                            UdpTask = new TeapotUDPasyncTask();
                            UdpTask.SetIpAddress(mTeapotWiFi.TeapotGetOwnIpAddress());
                            UdpTask.setContext(getApplicationContext());
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                UdpTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            else
                                UdpTask.execute();
                        }
                    }
                }
            }, 0L, 10L * 1000);
        }
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}
