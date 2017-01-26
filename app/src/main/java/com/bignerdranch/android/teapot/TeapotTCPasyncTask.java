package com.bignerdranch.android.teapot;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by AREG on 21.01.2017.
 */

public class TeapotTCPasyncTask extends AsyncTask<TeapotData, Void, Boolean> {

    private Activity mActivity;

    public void SetActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    protected Boolean doInBackground(TeapotData... params) {

        TeapotData data = params[0];

        Log.d("TeapotTCPasyncTask", "TCP AcyncTack started!");
        Log.d("TeapotTCPasyncTask", String.valueOf(data.getTargetTemperature()));

        TeapotTCPclient sendTCP = new TeapotTCPclient();

        return sendTCP.Create(data.getCurrentMode(), data.getTargetTemperature(), data.getWiFiIpAddress());
    }

    protected void onPostExecute(Boolean result) {
        Log.d("TeapotTCPasyncTask", "AsyncTask done");
        if (result == false) {
            Toast toast = Toast.makeText(mActivity.getApplicationContext(),
                    R.string.TCPsendFail,
                    Toast.LENGTH_LONG);
            TextView t = (TextView) toast.getView().findViewById(android.R.id.message);
            if( t != null) t.setGravity(Gravity.CENTER);
            toast.show();
        }
    }
}
