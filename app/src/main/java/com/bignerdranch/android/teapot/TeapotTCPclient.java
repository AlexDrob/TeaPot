package com.bignerdranch.android.teapot;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by AREG on 20.01.2017.
 */

public class TeapotTCPclient {

    private Socket socket;

    private static final int SERVERPORT = 4024;
    private static final String SERVER_IP = "192.168.1.103";

    public void Create(mode Mode, int target_temperature) {

        try {
            Log.d("TeapotTCPclient", "SET" + Mode + target_temperature);

            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
            socket = new Socket(serverAddr, SERVERPORT);
/*
            char str[] = new char[5];
            byte[] buffer = new byte[1024];

            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())), true);
            out.println("SET" + Mode + target_temperature);

            InputStream inputStream = socket.getInputStream();

            while (inputStream.read(buffer) != -1) {
                Log.d("TeapotTCPclient", buffer.toString());
            } */

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
