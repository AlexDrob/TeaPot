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

    private final String COLOR_THEME = "color_theme";
    private final String SIMMER_NOTIFICATION = "simmer_notification";
    private final String MODE_CHANGE_NOTIFICATION = "mode_change_notification";
    private final String TEMPERATURE_CHANGE_NOTIFICATION = "temperature_change_notification";
    private final String NOTIFICATION_MODE = "notification_mode";

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

        data.setColorTheme(mPreferences.getInt(COLOR_THEME, data.getColorTheme()));

        data.setSimmerNotification(mPreferences.getBoolean(SIMMER_NOTIFICATION, data.isSimmerNotification()));
        data.setModeChangeNotification(mPreferences.getBoolean(MODE_CHANGE_NOTIFICATION, data.isModeChangeNotification()));
        data.setTemperatureChangeNotification(mPreferences.getBoolean(TEMPERATURE_CHANGE_NOTIFICATION, data.isTemperatureChangeNotification()));
        data.setNotificationMode(mPreferences.getInt(NOTIFICATION_MODE, data.getNotificationMode()));
    }

    public void TeapotStoreData(TeapotData data, Context context) {

        mPreferences = context.getSharedPreferences(TEAPOT_STORAGE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = mPreferences.edit();

        if (mPreferences.getInt(TARGET_TEMPERATURE, 0) != data.getTargetTemperature()) {
            edit.putInt(TARGET_TEMPERATURE, data.getTargetTemperature());
        }
        if (mPreferences.getFloat(CURRENT_TEMPERATURE, (float)0.0) != data.getCurrentTemperature()) {
            edit.putFloat(CURRENT_TEMPERATURE, data.getCurrentTemperature());
        }
        if (mPreferences.getInt(TARGET_TEMPERATURE_MIN_LIMIT, 0) != data.getTargetTemperatureMinLimit()) {
            edit.putInt(TARGET_TEMPERATURE_MIN_LIMIT, data.getTargetTemperatureMinLimit());
        }
        if (mPreferences.getInt(TARGET_TEMPERATURE_MAX_LIMIT, 0) != data.getTargetTemperatureMaxLimit()) {
            edit.putInt(TARGET_TEMPERATURE_MAX_LIMIT, data.getTargetTemperatureMaxLimit());
        }

        if (data.getWiFiName().equals(mPreferences.getString(WIFI_NAME, "")) == false) {
            edit.putString(WIFI_NAME, data.getWiFiName());
        }
        if (data.getWiFiIpAddress().equals(mPreferences.getString(WIFI_IP_ADDRESS, "")) == false) {
            edit.putString(WIFI_IP_ADDRESS, data.getWiFiIpAddress());
        }

        if (ModeToInt(data.getCurrentMode()) != mPreferences.getInt(MODE, 0xFF)) {
            edit.putInt(MODE, ModeToInt(data.getCurrentMode()));
        }

        if (data.getColorTheme() != mPreferences.getInt(COLOR_THEME, 0)) {
            edit.putInt(COLOR_THEME, data.getColorTheme());
        }
        if (data.isSimmerNotification() != mPreferences.getBoolean(SIMMER_NOTIFICATION, true)) {
            edit.putBoolean(SIMMER_NOTIFICATION, data.isSimmerNotification());
        }
        if (data.isModeChangeNotification() != mPreferences.getBoolean(MODE_CHANGE_NOTIFICATION, true)) {
            edit.putBoolean(MODE_CHANGE_NOTIFICATION, data.isModeChangeNotification());
        }
        if (data.isTemperatureChangeNotification() != mPreferences.getBoolean(TEMPERATURE_CHANGE_NOTIFICATION, true)) {
            edit.putBoolean(TEMPERATURE_CHANGE_NOTIFICATION, data.isTemperatureChangeNotification());
        }
        if (data.getNotificationMode() != mPreferences.getInt(NOTIFICATION_MODE, 0)) {
            edit.putInt(NOTIFICATION_MODE, data.getNotificationMode());
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
