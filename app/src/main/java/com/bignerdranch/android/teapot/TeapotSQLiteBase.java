package com.bignerdranch.android.teapot;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by AREG on 20.01.2017.
 */

public class TeapotSQLiteBase extends SQLiteOpenHelper {

    private static final String TAG = "TeapotSQLiteBase";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TeapotManager";

    private static final String TABLE_TEAPOT = "Teapot";
    private static final String CURRENT_MODE = "CurrentMode";
    private static final String TARGET_TEMPERATURE = "TargetTemperature";
    private static final String TARGET_TEMPERATURE_MIN_LIMIT = "TargetTemperatureMinLimit";
    private static final String TARGET_TEMPERATURE_MAX_LIMIT = "TargetTemperatureMaxLimit";
    private static final String WIFI_SSID = "WiFiSSID";

    public TeapotSQLiteBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "Base constructor is called");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Base is created");
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TEAPOT + "("
                + CURRENT_MODE + " INTEGER," + TARGET_TEMPERATURE + " INTEGER,"
                + TARGET_TEMPERATURE_MIN_LIMIT + " INTEGER," + TARGET_TEMPERATURE_MAX_LIMIT
                + " INTEGER," + WIFI_SSID + " TEXT," + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAPOT);

        onCreate(db);
    }

    /*public void ReadAll(TeapotData data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());

        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    public void ReadAll(TeapotData data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TARGET_TEMPERATURE, data.getTargetTemperature());

        db.insert(TABLE_TEAPOT, null, values);
        db.close();
    }*/
}
