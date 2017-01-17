package com.bignerdranch.android.teapot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by AREG on 17.01.2017.
 */

public class TeapotMainFragment extends Fragment {

    private static final String TAG = "TeapotMainFragment";

    private ImageButton mArrowLeft;
    private ImageButton mArrowRight;
    private TextView mTargetTemperatureView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.teapot_main_fragment, container, false);
        Log.d(TAG, "onCreateView() called");

        // найдем изображения кнопок
        mArrowLeft = (ImageButton) v.findViewById(R.id.arrow_left);
        mArrowRight = (ImageButton) v.findViewById(R.id.arrow_right);
        // найдем изображение текста, который выводит целевую температуру в чайнике
        mTargetTemperatureView = (TextView) v.findViewById(R.id.target_temperature);

        // обработаем нажатие на кнопку уменьшения температуры
        mArrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTargetTemperatureView.getText();
            }
        });

        // обработаем нажатие на кнопку увеличения температуры
        mArrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}
