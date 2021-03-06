package com.bignerdranch.android.teapot;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Timer;
import java.util.TimerTask;

public class TeapotService extends Service {

    static private final String TAG = "TeapotService";

    private TeapotData data;
    private Timer mTimer;
    private TeapotUDPasyncTask UdpTask;
    private int NOTIFY_ID;

    @Override
    public void onCreate() {
        Log.i("Test", "Service: onCreate");

        Intent notificationIntent = new Intent(this, TeapotActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.teapot)
                .setContentTitle("Teapot")
                .setContentText("Приложение чайника работает")
                .setContentIntent(pendingIntent);
        Notification notification;
        if (Build.VERSION.SDK_INT < 16)
            notification = builder.getNotification();
        else
            notification = builder.build();

        notification.contentView = new RemoteViews(getPackageName(), R.layout.teapot_service_notification);
        startForeground(777, notification);
    }

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
                            UdpTask.SetData(data);
                            UdpTask.SetNotifyNumber(NOTIFY_ID++);
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
