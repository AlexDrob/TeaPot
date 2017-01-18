package com.bignerdranch.android.teapot;

/**
 * Created by AREG on 17.01.2017.
 */

public class TeapotData {

    private static mode mCurrentMode;
    private static int mTargetTemperature;
    private static float mCurrentTemperature;

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

    public TeapotData() {
        mTargetTemperature = 70;
        mCurrentTemperature = (float)27.0;
        mCurrentMode = mode.ModeTurnOff;
    }
}
