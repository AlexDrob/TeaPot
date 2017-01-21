package com.bignerdranch.android.teapot;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by AREG on 21.01.2017.
 */

// класс используется для сохранения данных в энергонезависимой
// памяти телефона (если проще, то на флешке)
public class TeapotSharedPreferences {

    private SharedPreferences mPreferences;

    private final String TEAPOT_STORAGE_NAME = "TEAPOT_APP";

    private final String TARGET_TEMPERATURE = "target_temperature";
    private final String CURRENT_TEMPERATURE = "current_temperature";
    private final String TARGET_TEMPERATURE_MIN_LIMIT = "target_temperature_min_limit";
    private final String TARGET_TEMPERATURE_MAX_LIMIT = "target_temperature_max_limit";

    private final String WIFI_NAME = "WiFi_name";
    private final String WIFI_IP_ADDRESS = "WiFi_Ip_address";

    private final String MODE = "mode";

    public void TeapotReStoreData(TeapotData data, Context context) {

        mPreferences = context.getSharedPreferences(TEAPOT_STORAGE_NAME, MODE_PRIVATE);

        // restore data from non-volatile memory
        data.setTargetTemperature(mPreferences.getInt(TARGET_TEMPERATURE, data.getTargetTemperature()));
        data.setCurrentTemperature(mPreferences.getFloat(CURRENT_TEMPERATURE, data.getCurrentTemperature()));
        data.setTargetTemperatureMinLimit(mPreferences.getInt(TARGET_TEMPERATURE_MIN_LIMIT, data.getTargetTemperatureMinLimit()));
        data.setTargetTemperatureMaxLimit(mPreferences.getInt(TARGET_TEMPERATURE_MAX_LIMIT, data.getTargetTemperatureMaxLimit()));

        data.setWiFiName(mPreferences.getString(WIFI_NAME, data.getWiFiName()));
        data.setWiFiIpAddress(mPreferences.getString(WIFI_IP_ADDRESS, data.getWiFiIpAddress()));

        data.setCurrentMode(IntToMode(mPreferences.getInt(MODE, ModeToInt(data.getCurrentMode()))));
    }

    public void TeapotStoreData(TeapotData data, Context context) {

        mPreferences = context.getSharedPreferences(TEAPOT_STORAGE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = mPreferences.edit();

        if (mPreferences.getInt(TARGET_TEMPERATURE, data.getTargetTemperature()) != data.getTargetTemperature()) {
            edit.putInt(TARGET_TEMPERATURE, data.getTargetTemperature());
        }
        if (mPreferences.getFloat(CURRENT_TEMPERATURE, data.getCurrentTemperature()) != data.getCurrentTemperature()) {
            edit.putFloat(CURRENT_TEMPERATURE, data.getCurrentTemperature());
        }
        if (mPreferences.getInt(TARGET_TEMPERATURE_MIN_LIMIT, data.getTargetTemperatureMinLimit()) != data.getTargetTemperatureMinLimit()) {
            edit.putInt(TARGET_TEMPERATURE_MIN_LIMIT, data.getTargetTemperatureMinLimit());
        }
        if (mPreferences.getInt(TARGET_TEMPERATURE_MAX_LIMIT, data.getTargetTemperatureMaxLimit()) != data.getTargetTemperatureMaxLimit()) {
            edit.putInt(TARGET_TEMPERATURE_MAX_LIMIT, data.getTargetTemperatureMaxLimit());
        }

        if (data.getWiFiName().equals(mPreferences.getString(WIFI_NAME, data.getWiFiName())) == false) {
            edit.putString(WIFI_NAME, data.getWiFiName());
        }
        if (data.getWiFiIpAddress().equals(mPreferences.getString(WIFI_IP_ADDRESS, data.getWiFiIpAddress())) == false) {
            edit.putString(WIFI_IP_ADDRESS, data.getWiFiIpAddress());
        }

        if (ModeToInt(data.getCurrentMode()) != mPreferences.getInt(MODE, ModeToInt(data.getCurrentMode()))) {
            edit.putInt(MODE, ModeToInt(data.getCurrentMode()));
        }

        edit.commit();
    }

    private int ModeToInt(mode value) {
        int ret_value = 0;
        switch (value) {
            case ModeTurnOff:
                ret_value = 1;
                break;
            case ModeAuto:
                ret_value = 2;
                break;
            case ModeHeat:
                ret_value = 3;
                break;
        }
        return ret_value;
    }

    private mode IntToMode(int value) {
        mode ret_value = mode.ModeTurnOff;
        switch (value) {
            case 1:
                ret_value = mode.ModeTurnOff;
                break;
            case 2:
                ret_value = mode.ModeAuto;
                break;
            case 3:
                ret_value = mode.ModeHeat;
                break;
        }
        return ret_value;
    }
}
