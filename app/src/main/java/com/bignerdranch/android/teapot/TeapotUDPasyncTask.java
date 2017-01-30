package com.bignerdranch.android.teapot;

import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
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

    public interface AsyncListener {
        void UpdateInfo(String IpAddress, int mode, int target_temperature, int current_temperature);
    }

    AsyncListener mListener;

    public void setUpdateListener(AsyncListener listener) {
        this.mListener = listener;
    }

    public void SetIpAddress(int OwnIpAddress) {
        this.OwnIpAddress = OwnIpAddress;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(TAG, "UDP task has been started!");
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
                        Log.d(TAG, "Length: " + String.valueOf(packet.getLength()));
                        if (packet.getLength() == 8) {
                            int mode = buf[4];
                            int target_temperature = buf[5];
                            int current_temperature = ((int)(buf[6] & 0xFF) * 256) + (int)(buf[7] & 0xFF);
                            String IpAddress = String.valueOf(buf[0] & 0xff) + "." +
                                    String.valueOf(buf[1] & 0xff) + "." + String.valueOf(buf[2] & 0xff)
                                    + "." + String.valueOf(buf[3] & 0xff);
                            mListener.UpdateInfo(IpAddress, mode, target_temperature, current_temperature);
                        }
                    } catch (SocketTimeoutException e) {
                        Log.d(TAG, "Socket's input buffer is empty!");
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
}
