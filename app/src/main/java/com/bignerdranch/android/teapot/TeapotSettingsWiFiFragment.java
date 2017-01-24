package com.bignerdranch.android.teapot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

        data = new TeapotData();
        // восстанавливаем данные
        TeapotSharedPreferences TeapotPreferences = new TeapotSharedPreferences();
        TeapotPreferences.TeapotReStoreData(data, getContext());

        mWiFiNameEdit.setText(data.getWiFiName());
        mIpAddress.setText(data.getWiFiIpAddress());

        mWiFiNameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
    /* When focus is lost check that the text field has valid values.
    */
                if (!hasFocus) {
                    //validateInput(v);
                }
            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        // сохраняем данные
        TeapotSharedPreferences TeapotPreferences = new TeapotSharedPreferences();
        TeapotPreferences.TeapotStoreData(data, getContext());
    }
}
