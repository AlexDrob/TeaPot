package com.bignerdranch.android.teapot;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by AREG on 17.01.2017.
 */

public class TeapotMainFragment extends Fragment {

    private static final String TAG = "TeapotMainFragment";

    private Button mTurnOffButton;
    private Button mAutoButton;
    private Button mHeatButton;
    private ImageButton mArrowLeft;
    private ImageButton mArrowRight;
    private TextView mTargetTemperatureView;

    private TeapotData data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        data = new TeapotData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.teapot_main_fragment, container, false);
        Log.d(TAG, "onCreateView() called");

        // найдем изображения кнопок
        mAutoButton = (Button) v.findViewById(R.id.auto_button);
        mTurnOffButton = (Button) v.findViewById(R.id.turn_off_button);
        mHeatButton = (Button) v.findViewById(R.id.heat_button);
        mArrowLeft = (ImageButton) v.findViewById(R.id.arrow_left);
        mArrowRight = (ImageButton) v.findViewById(R.id.arrow_right);
        // найдем изображение текста, который выводит целевую температуру в чайнике
        mTargetTemperatureView = (TextView) v.findViewById(R.id.target_temperature);
        mTargetTemperatureView.setText(String.valueOf(data.getTargetTemperature()));

        // обработаем нажатие на кнопку режима выключения
        View.OnClickListener TurnOffButton = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setCurrentMode(mode.ModeTurnOff);
                ViewCurrentMode();
            }
        };

        // обработаем нажатие на кнопку режима поддержания
        View.OnClickListener AutoButton = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setCurrentMode(mode.ModeAuto);
                ViewCurrentMode();
            }
        };

        // обработаем нажатие на кнопку режима нагрева
        View.OnClickListener HeatButton = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setCurrentMode(mode.ModeHeat);
                ViewCurrentMode();
            }
        };
        // обработаем нажатие на кнопку уменьшения температуры
        View.OnClickListener MinusButton = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Push on minus button");
                Log.d(TAG, "Old target temperature: " + mTargetTemperatureView.getText()
                        .toString() + "degrees");
                int target_temperature = data.getTargetTemperature();
                target_temperature -= 1;
                data.setTargetTemperature(target_temperature);
                mTargetTemperatureView.setText(String.valueOf(target_temperature));
                Log.d(TAG, "New target temperature: " + mTargetTemperatureView.getText()
                        .toString() + "degrees");
            }
        };

        // обработаем нажатие на кнопку увеличения температуры
        View.OnClickListener PlusButton = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Push on plus button");
                Log.d(TAG, "Old target temperature: " + mTargetTemperatureView.getText()
                        .toString() + "degrees");
                int target_temperature = data.getTargetTemperature();
                target_temperature += 1;
                data.setTargetTemperature(target_temperature);
                mTargetTemperatureView.setText(String.valueOf(target_temperature));
                Log.d(TAG, "New target temperature: " + mTargetTemperatureView.getText()
                        .toString() + "degrees");
            }
        };

        // Привяжем обработчики нажатия к кнопкам
        mArrowLeft.setOnClickListener(MinusButton);
        mArrowRight.setOnClickListener(PlusButton);
        mTurnOffButton.setOnClickListener(TurnOffButton);
        mAutoButton.setOnClickListener(AutoButton);
        mHeatButton.setOnClickListener(HeatButton);

        ViewCurrentMode();

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

    private void ViewCurrentMode() {
        switch (data.getCurrentMode())
        {
            case ModeTurnOff:
                mTurnOffButton.setBackgroundResource(R.drawable.turn_on_button);
                mAutoButton.setBackgroundResource(R.drawable.turn_off_button);
                mHeatButton.setBackgroundResource(R.drawable.turn_off_button);
                break;

            case ModeAuto:
                mTurnOffButton.setBackgroundResource(R.drawable.turn_off_button);
                mAutoButton.setBackgroundResource(R.drawable.turn_on_button);
                mHeatButton.setBackgroundResource(R.drawable.turn_off_button);
                break;

            case ModeHeat:
                mTurnOffButton.setBackgroundResource(R.drawable.turn_off_button);
                mAutoButton.setBackgroundResource(R.drawable.turn_off_button);
                mHeatButton.setBackgroundResource(R.drawable.turn_on_button);
                break;
        }
    }
}
