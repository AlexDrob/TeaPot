package com.bignerdranch.android.teapot;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by AREG on 17.01.2017.
 */

public class TeapotMainFragment extends Fragment {

    private static final int REQUEST_RESEND = 1;
    private static final int REQUEST_WIFI_OFF = 2;
    private static final int REQUEST_WIFI_ABSENT = 3;
    private static final int REQUEST_WIFI_WRONG = 4;

    private static final String TAG = "TeapotMainFragment";
    private static final String CURRENT_TEMP = "CurrentTemperature";
    private static final String DialogResendMode = "dialogResendMode";
    private static final String DialogWiFiIsOff = "dialogWiFiIsOff";
    private static final String DialogWiFiAbsent = "dialogWiFiNetworkIsAbsent";
    private static final String DialogWiFiWrong = "dialogWiFiNetworkWrong";

    private Button mTurnOffButton;
    private Button mAutoButton;
    private Button mHeatButton;
    private ImageButton mArrowLeft;
    private ImageButton mArrowRight;
    private TextView mTargetTemperatureView;
    private TextView mCurrentTemperatureView;

    private TeapotData data;

    private TeapotWiFi mTeapotWiFi;

    private boolean NetworkIsOk = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        data = new TeapotData(getContext());
        NetworkIsOk = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.teapot_main_fragment, container, false);
        Log.d(TAG, "onCreateView() called");

        // Восстанавливаем значение текущей температуры
        if (savedInstanceState != null) {
            float currentTemperature = savedInstanceState.getFloat(CURRENT_TEMP, data.getCurrentTemperature());
            data.setCurrentTemperature(currentTemperature);
            Log.d(TAG, "restored data");
        }

        // найдем изображения кнопок
        mAutoButton = (Button) v.findViewById(R.id.auto_button);
        mTurnOffButton = (Button) v.findViewById(R.id.turn_off_button);
        mHeatButton = (Button) v.findViewById(R.id.heat_button);
        mArrowLeft = (ImageButton) v.findViewById(R.id.arrow_left);
        mArrowRight = (ImageButton) v.findViewById(R.id.arrow_right);
        // найдем изображение текста, который выводит целевую температуру в чайнике
        mTargetTemperatureView = (TextView) v.findViewById(R.id.target_temperature);
        // найдем изображение текста, который выводит текущую температуру в чайнике
        mCurrentTemperatureView = (TextView) v.findViewById(R.id.CurrentTemperature);

        // обработаем нажатие на поле с целевой температурой
        View.OnLongClickListener TargetTemperatureButton = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                        R.string.updatetemperaturesent,
                        Toast.LENGTH_SHORT);
                TextView t = (TextView) toast.getView().findViewById(android.R.id.message);
                if( t != null) t.setGravity(Gravity.CENTER);
                toast.show();
                return true;
            }
        };

        // обработаем нажатие на кнопку режима выключения
        View.OnClickListener TurnOffButton = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.getCurrentMode() == mode.ModeTurnOff) {
                    ShowDialogWindow(R.string.ResendCommandHeader, R.string.ResendCommandBody,
                            REQUEST_RESEND, DialogResendMode); // вывод диалогового окна
                }
                else {
                    data.setCurrentMode(mode.ModeTurnOff);
                    ViewCurrentMode();
                    SendTcpCommandToTeapot();
                }
            }
        };

        // обработаем нажатие на кнопку режима поддержания
        View.OnClickListener AutoButton = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.getCurrentMode() == mode.ModeAuto) {
                    ShowDialogWindow(R.string.ResendCommandHeader, R.string.ResendCommandBody,
                            REQUEST_RESEND, DialogResendMode); // вывод диалогового окна
                }
                else {
                    data.setCurrentMode(mode.ModeAuto);
                    ViewCurrentMode();
                    SendTcpCommandToTeapot();
                }
            }
        };

        // обработаем нажатие на кнопку режима нагрева
        View.OnClickListener HeatButton = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.getCurrentMode() == mode.ModeHeat) {
                    ShowDialogWindow(R.string.ResendCommandHeader, R.string.ResendCommandBody,
                            REQUEST_RESEND, DialogResendMode); // вывод диалогового окна
                }
                else {
                    data.setCurrentMode(mode.ModeHeat);
                    ViewCurrentMode();
                    SendTcpCommandToTeapot();
                }
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
                if (target_temperature == data.getTargetTemperatureMinLimit()) {
                    ShowDialogWindow(R.string.ResendTemperatureHeader,
                            R.string.ResendTemperatureBody, REQUEST_RESEND,
                            DialogResendMode); // вывод диалогового окна
                }
                else {
                    target_temperature -= 1;
                    data.setTargetTemperature(target_temperature);
                    ShowTargetTemperature();
                    Log.d(TAG, "New target temperature: " + mTargetTemperatureView.getText()
                            .toString() + "degrees");
                    SendTcpCommandToTeapot();
                }
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
                if (target_temperature == data.getTargetTemperatureMaxLimit()) {
                    ShowDialogWindow(R.string.ResendTemperatureHeader,
                            R.string.ResendTemperatureBody, REQUEST_RESEND,
                            DialogResendMode); // вывод диалогового окна
                }
                else {
                    target_temperature += 1;
                    data.setTargetTemperature(target_temperature);
                    ShowTargetTemperature();
                    Log.d(TAG, "New target temperature: " + mTargetTemperatureView.getText()
                            .toString() + "degrees");
                    SendTcpCommandToTeapot();
                }
            }
        };

        // Привяжем обработчики нажатия к кнопкам
        mArrowLeft.setOnClickListener(MinusButton);
        mArrowRight.setOnClickListener(PlusButton);
        mTurnOffButton.setOnClickListener(TurnOffButton);
        mAutoButton.setOnClickListener(AutoButton);
        mHeatButton.setOnClickListener(HeatButton);

        // Привяжем обработчик к нажатию на поле с целевой температурой
        mTargetTemperatureView.setOnLongClickListener(TargetTemperatureButton);

        // Отображаем активной кнопку с текущим режимом
        ViewCurrentMode();

        // Сожмем текст целевой температуры чтобы он влез на экран
        mTargetTemperatureView.setTextScaleX((float)0.8);

        // Отображаем текущую целевую температуру
        ShowTargetTemperature();
        // Отображаем текущую температуру
        ShowCurrentTemperature();

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "onSaveInstanceState() called");
        savedInstanceState.putFloat(CURRENT_TEMP, data.getCurrentTemperature());
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
        // проверяем состояние WiFi
        mTeapotWiFi = new TeapotWiFi(getContext());
        if (mTeapotWiFi.TeapotCurrentWiFiState() == false) {
            Log.d(TAG, "WiFi is turn off");
            ShowDialogWindow(R.string.WiFiTurnOffHeader, R.string.WiFiTurnOffBody,
                    REQUEST_WIFI_OFF, DialogWiFiIsOff);
        }
        else {
            Log.d(TAG, "WiFi is turn on");
            if (mTeapotWiFi.TeapotIsConnectedToWiFi() == false) {
                Log.d(TAG, "Device is not connected to WiFi network");
                ShowDialogWindow(R.string.WiFiNetworkAbsentHeader, R.string.WiFiNetworkAbsentBody,
                        REQUEST_WIFI_ABSENT, DialogWiFiAbsent);
            }
            else {
                Log.d(TAG, "Device is connected to WiFi network");
                Log.d(TAG, "WiFi network name - " + mTeapotWiFi.TeapotSSIDnetwork());
                Log.d(TAG, "WiFi network name - " + data.getWiFiName());
                if (mTeapotWiFi.TeapotSSIDnetwork().equals(data.getWiFiName())) {
                    NetworkIsOk = true;
                    Log.d(TAG, "Device is connected to correct WiFi network");
                }
                else {
                    ShowDialogWindow(R.string.WiFiNetworkWrongHeader, R.string.WiFiNetworkWrongBody,
                            REQUEST_WIFI_WRONG, DialogWiFiWrong);
                    Log.d(TAG, "Device is connected to incorrect WiFi network");
                }
            }
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_RESEND:
                if (resultCode == Activity.RESULT_OK) {
                    SendTcpCommandToTeapot();
                    Log.d(TAG, "dialogOkButton called");
                }
                break;

            case REQUEST_WIFI_OFF:
            case REQUEST_WIFI_ABSENT:
            case REQUEST_WIFI_WRONG:
                if (resultCode == Activity.RESULT_CANCELED) {
                    Log.d(TAG, "dialogCancelButton called");
                    getActivity().finish();
                }
                break;

            default:
                break;
        }
    }

    private void UpdateTemperatureColor(TextView mTextView, int temperature) {
        int new_color = CalculateNewTemperatureColor(temperature);
        mTextView.setTextColor(new_color);
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

    private int CalculateNewTemperatureColor(int temperature) {
        int color[] = new int[3];
        if (temperature < 60) {
            color[0] = ((temperature - 20) * 6); // red
            color[1] = ((temperature - 20) * 6); // green
            color[2] = 0xFF - color[1]; // blue
        }
        else {
            color[0] = 0xFF; // red
            color[1] = 0xFF - ((temperature - 60) * 6); // green
            color[2] = 0; // blue
        }
        return Color.rgb(color[0],color[1],color[2]);
    }

    // Отображаем текущую целевую температуру
    private void ShowTargetTemperature() {
        mTargetTemperatureView.setText(String.valueOf(data.getTargetTemperature()) + (char) 0x00B0);
        UpdateTemperatureColor(mTargetTemperatureView, data.getTargetTemperature());
    }

    // Отображаем текущую температуру
    private void ShowCurrentTemperature() {
        mCurrentTemperatureView.setText(String.valueOf(data.getCurrentTemperature()) + (char) 0x00B0);
        UpdateTemperatureColor(mCurrentTemperatureView, (int)data.getCurrentTemperature());
    }

    // вывод диалогового окна
    private void ShowDialogWindow(int header, int messageBody, int requestCode, String label) {
        DialogFragment newFragment = TeapotDialogFragment.newInstance(
                header, messageBody);
        newFragment.setTargetFragment(this, requestCode);
        newFragment.show(getFragmentManager(), label);
    }

    private void SendTcpCommandToTeapot() {
        if (NetworkIsOk == true) {
            TeapotTCPasyncTask TcpTask = new TeapotTCPasyncTask();
            TcpTask.SetActivity(getActivity());
            TcpTask.execute(data);
        }
    }
}
