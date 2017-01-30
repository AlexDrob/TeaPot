package com.bignerdranch.android.teapot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by AREG on 23.01.2017.
 */

public class TeapotSettingsWiFiFragment extends Fragment {

    private EditText mWiFiNameEdit;
    private TextView mIpAddress;
    private FrameLayout mFrameLayout;

    public TextView getIpAddress() {
        return mIpAddress;
    }

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
        mFrameLayout = (FrameLayout) v.findViewById(R.id.SettingsWiFi);

        data = TeapotData.get();

        mWiFiNameEdit.setText(data.getWiFiName());
        mIpAddress.setText(data.getWiFiIpAddress());

        switch (data.getColorTheme()) {
            case 1:
                mFrameLayout.setBackgroundResource(R.color.colorBackGround);
                break;
            case 2:
                mFrameLayout.setBackgroundResource(R.color.colorBackGround2);
                break;
            case 3:
                mFrameLayout.setBackgroundResource(R.color.colorBackGround3);
                break;
        }

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
