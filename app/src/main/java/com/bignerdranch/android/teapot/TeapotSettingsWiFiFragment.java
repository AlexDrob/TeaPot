package com.bignerdranch.android.teapot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by AREG on 23.01.2017.
 */

public class TeapotSettingsWiFiFragment extends Fragment {

    private EditText mWiFiNameEdit;
    private TextView mIpAddress;

    TeapotData data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.teapot_settings_wifi_fragment, container, false);

        mWiFiNameEdit = (EditText) v.findViewById(R.id.WiFiSSID);
        mIpAddress = (TextView) v.findViewById(R.id.CurrentIpAddress);

        data = TeapotData.get();

        mWiFiNameEdit.setText(data.getWiFiName());
        mIpAddress.setText(data.getWiFiIpAddress());

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // сохраняем данные
        if (data.getWiFiName().equals(mWiFiNameEdit.getText().toString()) == false) {
            data.setWiFiName(mWiFiNameEdit.getText().toString());
        }
    }
}
