package com.bignerdranch.android.teapot;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * Created by AREG on 27.01.2017.
 */

public class TeapotTemperatureLimitsFragment extends Fragment {

    private final static String TAG = "TeapotLimitsFragment";

    private static final String DialogMinLimit = "dialogMinLimit";
    private static final String DialogMaxLimit = "dialogMaxLimit";

    private final static int sMinLimit = 1;
    private final static int sMaxLimit = 2;

    private TextView mMinLimit;
    private TextView mMaxLimit;
    private LinearLayout mLinearLayout;
    private NumberPicker TemperaturePicker;

    private TeapotData data;

    private int source;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.teapot_temperature_limits_fragment, container, false);

        data = TeapotData.get();

        mMinLimit = (TextView) v.findViewById(R.id.min_limit);
        mMaxLimit = (TextView) v.findViewById(R.id.max_limit);
        mLinearLayout = (LinearLayout) v.findViewById(R.id.LimitSettings);

        mMinLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "call MinLimitOnClickListener");
                source = sMinLimit;
                ShowDialog(data.getAbsTargetTemperatureMinLimit(),
                        data.getTargetTemperatureMaxLimit(), data.getTargetTemperatureMinLimit());
            }
        });

        mMaxLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "call MaxLimitOnClickListener");
                source = sMaxLimit;
                ShowDialog(data.getTargetTemperatureMinLimit(),
                        data.getAbsTargetTemperatureMaxLimit(), data.getTargetTemperatureMaxLimit());
            }
        });

        switch (data.getColorTheme()) {
            case 1:
                mLinearLayout.setBackgroundResource(R.color.colorBackGround);
                break;
            case 2:
                mLinearLayout.setBackgroundResource(R.color.colorBackGround2);
                break;
            case 3:
                mLinearLayout.setBackgroundResource(R.color.colorBackGround3);
                break;
        }

        return v;
    }

    private void ShowDialog(int minTemperature, int maxTemperature, int currentTemperature) {
        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
        if (source == sMinLimit) {
            builder.setTitle(R.string.SetMinLimit);
        }
        if (source == sMaxLimit) {
            builder.setTitle(R.string.SetMaxLimit);
        }

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.teapot_dialog_limits_fragment, null);

        builder.setView(dialogView);

        builder.setPositiveButton(R.string.dialogOkButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (source == sMinLimit) {
                    data.setTargetTemperatureMinLimit(TemperaturePicker.getValue());
                }
                if (source == sMaxLimit) {
                    data.setTargetTemperatureMaxLimit(TemperaturePicker.getValue());
                }
            }
        });

        builder.setNegativeButton(R.string.dialogCancelButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alert = builder.create();
        alert.setCancelable(false);

        TemperaturePicker = (NumberPicker) dialogView.findViewById(R.id.numberPicker1);
        TemperaturePicker.setMaxValue(maxTemperature);
        TemperaturePicker.setMinValue(minTemperature);
        TemperaturePicker.setValue(currentTemperature);
        TemperaturePicker.setWrapSelectorWheel(false);

        alert.show();
    }
}
