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
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * Created by AREG on 27.01.2017.
 */

public class TeapotTemperatureLimitsFragment extends Fragment {

    private final static String TAG = "TeapotLimitsFragment";

    private final static int sMinLimit = 1;
    private final static int sMaxLimit = 2;

    private TextView mMinLimit;
    private TextView mMaxLimit;

    private TeapotData data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.teapot_temperature_limits_fragment, container, false);

        mMinLimit = (TextView) v.findViewById(R.id.min_limit);
        mMaxLimit = (TextView) v.findViewById(R.id.max_limit);

        mMinLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "call MinLimitOnClickListener");
                ShowDialog(20, 99, 20, sMinLimit);
            }
        });

        mMaxLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "call MaxLimitOnClickListener");
                ShowDialog(20, 99, 20, sMaxLimit);
            }
        });

        return v;
    }

    private void ShowDialog(int minTemperature, int maxTemperature, int currentTemperature, int source) {
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

            }
        });

        builder.setNegativeButton(R.string.dialogCancelButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alert = builder.create();
        alert.setCancelable(false);

        NumberPicker TemperaturePicker = (NumberPicker) dialogView.findViewById(R.id.numberPicker1);
        TemperaturePicker.setMaxValue(maxTemperature);
        TemperaturePicker.setMinValue(minTemperature);
        TemperaturePicker.setValue(currentTemperature);
        TemperaturePicker.setWrapSelectorWheel(false);

        alert.show();
    }
}
