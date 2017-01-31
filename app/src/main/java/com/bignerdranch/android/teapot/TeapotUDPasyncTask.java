package com.bignerdranch.android.teapot;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.DhcpInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.View;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.channels.DatagramChannel;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

/**
 * Created by AREG on 24.01.2017.
 */

public class TeapotUDPasyncTask extends AsyncTask<Void, Void, Void> {

    private final static String TAG = "TeapotUDPasyncTask";
    private final static int UDP_PORT = 4023;

    private int OwnIpAddress;
    private DatagramSocket clientSocket;
    private TeapotData data;

    private Context mContext;

    public void SetIpAddress(int OwnIpAddress) {
        this.OwnIpAddress = OwnIpAddress;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(TAG, "UDP task has been started!");
        data = TeapotData.get();
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            InetSocketAddress sa = new InetSocketAddress(UDP_PORT);
            DatagramChannel channel = DatagramChannel.open();
            //DatagramSocket clientSocket = new DatagramSocket();
            clientSocket = channel.socket();

            clientSocket.setBroadcast(true);
            clientSocket.setReuseAddress(true);
            clientSocket.bind(sa);
            clientSocket.setSoTimeout(1000);

            byte[] sendData = {0x47, 0x45, 0x54, 0x00, 0x00, 0x00, 0x00};
            sendData[3] = (byte) ((OwnIpAddress >> 0) & 0xFF);
            sendData[4] = (byte) ((OwnIpAddress >> 8) & 0xFF);
            sendData[5] = (byte) ((OwnIpAddress >> 16) & 0xFF);
            sendData[6] = (byte) ((OwnIpAddress >> 24) & 0xFF);

            DatagramPacket sendPacket = new DatagramPacket(sendData,
                    sendData.length, InetAddress.getByName("255.255.255.255"), UDP_PORT);
            // create a buffer to copy packet contents into
            byte[] buf = new byte[200];
            // create a packet to receive
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            while (true) {
                clientSocket.send(sendPacket);
                // wait to receive the packet
                while (true) {
                    try {
                        clientSocket.receive(packet);
                        if (packet.getLength() == 8) {
                            int Mode = (buf[4] & 0xFF);
                            int target_temperature = (buf[5] & 0xFF);
                            int current_temperature = ((int)(buf[6] & 0xFF) * 256) + (int)(buf[7] & 0xFF);
                            String IpAddress = String.valueOf(buf[0] & 0xff) + "." +
                                    String.valueOf(buf[1] & 0xff) + "." + String.valueOf(buf[2] & 0xff)
                                    + "." + String.valueOf(buf[3] & 0xff);
                            Log.d(TAG, "Ip address " + IpAddress);
                            Log.d(TAG, "Current mode " + Mode);
                            Log.d(TAG, "Target temperature " + target_temperature);
                            float CurrentTemperature = (float)current_temperature / (float)10.0;
                            Log.d(TAG, "Current temperature " + String.valueOf(CurrentTemperature));
                            UpdateInfo(IpAddress, target_temperature, CurrentTemperature, Mode);
                        }
                    } catch (SocketTimeoutException e) {
                        break;
                    }
                }
                TimeUnit.SECONDS.sleep(10);
            }
        } catch (InterruptedException e) {
            CloseSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        CloseSocket();
    }

    protected void CloseSocket() {
        try {
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "UDP task has been stopped!");
    }

    private void UpdateInfo(String IpAddress, int target_temperature,
                            float CurrentTemperature, int Mode) {
        if (data.getTargetTemperature() != target_temperature) {
            // оповестить об изменении температуры поддержания
            if (data.isTemperatureChangeNotification() == true) {
                switch (data.getNotificationMode()) {
                    case 1: // SMS
                        PerformSmsRingtone();
                        break;
                    case 2: // Vibration
                        PerformVibrate();
                        break;
                    case 3: // Notification
                        break;
                }
            }
            data.setTargetTemperature(target_temperature);
        }
        if (data.getCurrentTemperature() != CurrentTemperature) {
            data.setCurrentTemperature(CurrentTemperature);
        }
        if (IpAddress.equals(data.getWiFiIpAddress()) == false) {
            data.setWiFiIpAddress(IpAddress);
        }
        mode MODE = mode.ModeTurnOff;
        switch (Mode) {
            case 1:
                MODE = mode.ModeTurnOff;
                break;
            case 2:
                MODE = mode.ModeAuto;
                break;
            case 3:
                MODE = mode.ModeHeat;
                break;
        }
        if (MODE != data.getCurrentMode()) {
            // оповестить о том, что чайник закипел
            if ((data.getCurrentMode() == mode.ModeHeat) & (data.isSimmerNotification() == true)) {
                switch (data.getNotificationMode()) {
                    case 1: // SMS
                        PerformSmsRingtone();
                        break;
                    case 2: // Vibration
                        PerformVibrate();
                        break;
                    case 3: // Notification
                        break;
                }
            }
            // оповестить о том, что изменился режим
            if (data.isModeChangeNotification() == true) {
                switch (data.getNotificationMode()) {
                    case 1: // SMS
                        PerformSmsRingtone();
                        break;
                    case 2: // Vibration
                        PerformVibrate();
                        break;
                    case 3: // Notification
                        break;
                }
            }
            data.setCurrentMode(MODE);
        }
    }

    private void PerformVibrate() {
        // Get instance of Vibrator from current Context
        Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 1000 milliseconds
        v.vibrate(1000);
    }

    private void PerformSmsRingtone() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(mContext, notification);
        r.play();
    }
}
