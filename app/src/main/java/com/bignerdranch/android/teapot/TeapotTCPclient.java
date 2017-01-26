package com.bignerdranch.android.teapot;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

/**
 * Created by AREG on 20.01.2017.
 */

public class TeapotTCPclient {

    private Socket socket;

    private static final String TAG = "TeapotTCPclient";

    private static final int SERVERPORT = 4024;

    public boolean Create(mode Mode, int target_temperature, String SERVER_IP) {

        boolean result = false;

        try {
            byte[] data = new byte[6];
            byte MODE = (byte)0x02;

            switch (Mode)
            {
                case ModeTurnOff: MODE = (byte)0x01; break;
                case ModeAuto:    MODE = (byte)0x02; break;
                case ModeHeat:    MODE = (byte)0x03; break;
            }

            // Подготовим данные для записи
            byte[] send_buffer = {(byte)0x53, (byte)0x45, (byte)0x54, MODE, (byte)target_temperature};

            Log.d(TAG, "SET: " + Mode + " " + target_temperature + " degree");

            // Создаем и открываем сокет
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
            socket = new Socket(serverAddr, SERVERPORT);

            // Создаем поток для записи и пишем данные в поток
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.flush();
            outputStream.write(send_buffer, 0, 5);

            // Читаем и проверяем данные из потока
            int count = socket.getInputStream().read(data);
            if ((count == 6) && ("SET_OK".equals(new String(data)))) {
                result = true;
                Log.d(TAG, "GET: " + String.valueOf(count) + " bytes");
                Log.d(TAG, "GETed data: " + new String(data) + " bytes");
            }

            // Закрываем поток и сокет
            outputStream.close();
            socket.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
