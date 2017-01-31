package com.bignerdranch.android.teapot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by AREG on 27.01.2017.
 */

public class TeapotNotificationFragment extends Fragment {

    private FrameLayout mFrameLayout;
    private CheckBox mNotificationSimmer;
    private CheckBox mNotificationModeChange;
    private CheckBox mNotificationTemperatureChange;
    private RadioGroup mRadioGroup;
    private RadioButton mNotificationRington;
    private RadioButton mNotificationVibration;
    private RadioButton mNotificationPush;

    private TeapotData data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.teapot_notifications_fragment, container, false);

        data = TeapotData.get();

        mFrameLayout = (FrameLayout) v.findViewById(R.id.notifications_fragment);
        mNotificationSimmer = (CheckBox) v.findViewById(R.id.notificationSimmer);
        mNotificationModeChange = (CheckBox) v.findViewById(R.id.notificationModeChange);
        mNotificationTemperatureChange = (CheckBox) v.findViewById(R.id.notificationTemperatureChange);
        mRadioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        mNotificationRington = (RadioButton) v.findViewById(R.id.notificationRington);
        mNotificationVibration = (RadioButton) v.findViewById(R.id.notificationVibration);
        mNotificationPush = (RadioButton) v.findViewById(R.id.notificationPush);

        mNotificationSimmer.setChecked(data.isSimmerNotification());
        mNotificationModeChange.setChecked(data.isModeChangeNotification());
        mNotificationTemperatureChange.setChecked(data.isTemperatureChangeNotification());

        switch (data.getNotificationMode()) {
            case 1:
                mNotificationRington.setChecked(true);
                break;
            case 2:
                mNotificationVibration.setChecked(true);
                break;
            case 3:
                mNotificationPush.setChecked(true);
                break;
        }

        UpdateRadioGroup();

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

        mNotificationSimmer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setSimmerNotification(isChecked);
                UpdateRadioGroup();
            }
        });

        mNotificationModeChange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setModeChangeNotification(isChecked);
                UpdateRadioGroup();
            }
        });

        mNotificationTemperatureChange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setTemperatureChangeNotification(isChecked);
                UpdateRadioGroup();
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mNotificationRington.isChecked() == true) {
                    data.setNotificationMode(1);
                }
                if (mNotificationVibration.isChecked() == true) {
                    data.setNotificationMode(2);
                }
                if (mNotificationPush.isChecked() == true) {
                    data.setNotificationMode(3);
                }
            }
        });
        return v;
    }

    private void UpdateRadioGroup() {
        if ((mNotificationSimmer.isChecked() == false) &
                (mNotificationModeChange.isChecked() == false) &
                (mNotificationTemperatureChange.isChecked() == false)) {
            mRadioGroup.setEnabled(false);
            mNotificationRington.setEnabled(false);
            mNotificationVibration.setEnabled(false);
            mNotificationPush.setEnabled(false);
        }
        else {
            mRadioGroup.setEnabled(true);
            mNotificationRington.setEnabled(true);
            mNotificationVibration.setEnabled(true);
            mNotificationPush.setEnabled(true);
        }
    }
}
