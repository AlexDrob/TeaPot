package com.bignerdranch.android.teapot;

import android.content.Context;

/**
 * Created by AREG on 17.01.2017.
 */

public class TeapotData {

    private static TeapotData sTeapotData;

    private static final int mAbsTargetTemperatureMinLimit = 20;
    private static final int mAbsTargetTemperatureMaxLimit = 99;

    private static mode mCurrentMode;
    private static int mTargetTemperature;
    private static int mTargetTemperatureMinLimit;
    private static int mTargetTemperatureMaxLimit;
    private static float mCurrentTemperature;
    private static String mWiFiName;
    private static String mWiFiIpAddress;

    private static int sColorTheme;
    private static boolean sSimmerNotification;
    private static boolean sModeChangeNotification;
    private static boolean sTemperatureChangeNotification;
    private static int sNotificationMode;

    public static int getTargetTemperature() {
        return mTargetTemperature;
    }

    public static void setTargetTemperature(int targetTemperature) {
        mTargetTemperature = targetTemperature;
    }

    public static mode getCurrentMode() {
        return mCurrentMode;
    }

    public static void setCurrentMode(mode mCurrentMode) {
        TeapotData.mCurrentMode = mCurrentMode;
    }

    public static float getCurrentTemperature() {
        return mCurrentTemperature;
    }

    public static void setCurrentTemperature(float mCurrentTemperature) {
        TeapotData.mCurrentTemperature = mCurrentTemperature;
    }

    public static int getTargetTemperatureMinLimit() {
        return mTargetTemperatureMinLimit;
    }

    public static void setTargetTemperatureMinLimit(int mTargetTemperatureMinLimit) {
        TeapotData.mTargetTemperatureMinLimit = mTargetTemperatureMinLimit;
    }

    public static int getTargetTemperatureMaxLimit() {
        return mTargetTemperatureMaxLimit;
    }

    public static void setTargetTemperatureMaxLimit(int mTargetTemperatureMaxLimit) {
        TeapotData.mTargetTemperatureMaxLimit = mTargetTemperatureMaxLimit;
    }

    public static int getAbsTargetTemperatureMinLimit() {
        return mAbsTargetTemperatureMinLimit;
    }

    public static int getAbsTargetTemperatureMaxLimit() {
        return mAbsTargetTemperatureMaxLimit;
    }

    public static String getWiFiName() {
        return mWiFiName;
    }

    public static void setWiFiName(String mWiFiName) {
        TeapotData.mWiFiName = mWiFiName;
    }

    public static String getWiFiIpAddress() {
        return mWiFiIpAddress;
    }

    public static void setWiFiIpAddress(String mWiFiIpAddress) {
        TeapotData.mWiFiIpAddress = mWiFiIpAddress;
    }

    public static int getColorTheme() {
        return sColorTheme;
    }

    public static void setColorTheme(int colorTheme) {
        sColorTheme = colorTheme;
    }

    public static int getNotificationMode() {
        return sNotificationMode;
    }

    public static void setNotificationMode(int notificationMode) {
        sNotificationMode = notificationMode;
    }

    public static boolean isSimmerNotification() {
        return sSimmerNotification;
    }

    public static void setSimmerNotification(boolean simmerNotification) {
        sSimmerNotification = simmerNotification;
    }

    public static boolean isModeChangeNotification() {
        return sModeChangeNotification;
    }

    public static void setModeChangeNotification(boolean modeChangeNotification) {
        sModeChangeNotification = modeChangeNotification;
    }

    public static boolean isTemperatureChangeNotification() {
        return sTemperatureChangeNotification;
    }

    public static void setTemperatureChangeNotification(boolean temperatureChangeNotification) {
        sTemperatureChangeNotification = temperatureChangeNotification;
    }

    public static TeapotData get() {
        if (sTeapotData == null) {
            sTeapotData = new TeapotData();
        }
        return sTeapotData;
    }

    private TeapotData() {
        mTargetTemperature = 70;
        mCurrentTemperature = (float)27.0;
        mCurrentMode = mode.ModeTurnOff;
        mWiFiName = "eCozy24Gh";
        mWiFiIpAddress = "192.168.1.103";
        mTargetTemperatureMinLimit = mAbsTargetTemperatureMinLimit;
        mTargetTemperatureMaxLimit = mAbsTargetTemperatureMaxLimit;

        sColorTheme = 1;
        sSimmerNotification = false;
        sModeChangeNotification = false;
        sTemperatureChangeNotification = false;
        sNotificationMode = 1;
    }
}
