package com.bignerdranch.android.teapot;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class TeapotActivity extends ActionBarActivity {

    private static final String TAG = "TeapotActivity";
    private static final String WIFI_STATE = "WiFi_state";

    private boolean NetworkIsOk = false;

    private int list_index;
    private TeapotData data;
    private Intent teapot_service;

    private Toolbar mToolbar;
    private Drawer mNavigationDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teapot);

        data = TeapotData.get();
        // восстанавливаем данные
        TeapotSharedPreferences TeapotPreferences = new TeapotSharedPreferences();
        TeapotPreferences.TeapotReStoreData(data, getApplicationContext());

        list_index = 0;
        NetworkIsOk = false;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //create the drawer and remember the `Drawer` result object
        mNavigationDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog).withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_temperature).withIcon(FontAwesome.Icon.faw_thermometer_half).withIdentifier(3),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_notifications).withIcon(FontAwesome.Icon.faw_comment).withIdentifier(4),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_color_themes).withIcon(FontAwesome.Icon.faw_cogs).withIdentifier(5),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_send).withIcon(FontAwesome.Icon.faw_envelope).withIdentifier(6),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_exit).withIcon(FontAwesome.Icon.faw_sign_out).withIdentifier(7)
                )
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Скрываем клавиатуру при открытии Navigation Drawer
                        InputMethodManager inputMethodManager = (InputMethodManager) TeapotActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(TeapotActivity.this.getCurrentFocus().getWindowToken(), 0);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                    }

                    @Override
                    public void onDrawerSlide (View drawerView, float slideOffset)
                    {

                    }
                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.d(TAG, "position: " + String.valueOf(position));
                        if (position != 0) {
                            ShowCurrentFragment(position - 1);
                        }
                        mNavigationDrawer.closeDrawer();
                        return true;
                    }
                })
                .build();

        switch (data.getColorTheme()) {
            case 1:
                getSupportActionBar().setBackgroundDrawable(getResources().
                        getDrawable(R.color.colorTopGround));
                break;
            case 2:
                getSupportActionBar().setBackgroundDrawable(getResources().
                        getDrawable(R.color.colorTopGround2));
                break;
            case 3:
                getSupportActionBar().setBackgroundDrawable(getResources().
                        getDrawable(R.color.colorTopGround3));
                break;
        }
        getSupportActionBar().setElevation(0);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //для портретного режима
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
        TeapotSharedPreferences TeapotPreferences = new TeapotSharedPreferences();
        TeapotPreferences.TeapotStoreData(data, getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed() called");
        if(mNavigationDrawer.isDrawerOpen()){
            mNavigationDrawer.closeDrawer();
        } else if (list_index == 5) {
            // сбрасываем отправку email
            list_index = 0;
        } else if (list_index != 0) {
            list_index = 0;
            ShowCurrentFragment(list_index);
        } else {
            // Otherwise defer to system default behavior.
            super.onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
        // проверяем состояние WiFi
        TeapotWiFi mTeapotWiFi = new TeapotWiFi(getApplicationContext());
        if (mTeapotWiFi.TeapotCurrentWiFiState() == false) {
            Log.d(TAG, "WiFi is turn off");
            NetworkIsOk = false;
            ShowDialogWindow(R.string.WiFiTurnOffHeader, R.string.WiFiTurnOffBody);
        }
        else {
            Log.d(TAG, "WiFi is turn on");
            if (mTeapotWiFi.TeapotIsConnectedToWiFi() == false) {
                Log.d(TAG, "Device is not connected to WiFi network");
                NetworkIsOk = false;
                ShowDialogWindow(R.string.WiFiNetworkAbsentHeader, R.string.WiFiNetworkAbsentBody);
            }
            else {
                Log.d(TAG, "Device is connected to WiFi network");
                Log.d(TAG, "WiFi network name - " + mTeapotWiFi.TeapotSSIDnetwork());
                String WiFiName = "\"" + data.getWiFiName() + "\"";
                Log.d(TAG, "WiFi network name - " + WiFiName);
                if (mTeapotWiFi.TeapotSSIDnetwork().equals(WiFiName)) {
                    NetworkIsOk = true;
                    Log.d(TAG, "Device is connected to correct WiFi network");
                }
                else {
                    NetworkIsOk = false;
                    ShowDialogWindow(R.string.WiFiNetworkWrongHeader, R.string.WiFiNetworkWrongBody);
                    Log.d(TAG, "Device is connected to incorrect WiFi network");
                }
            }
        }

        teapot_service = new Intent(TeapotActivity.this, TeapotService.class);
        startService(teapot_service);

        if (list_index == 5) {
            // сбрасываем отправку email
            list_index = 0;
        }
        ShowCurrentFragment(list_index);
    }

    private void ShowCurrentFragment(int index) {
        list_index = index;
        Fragment fragment = new TeapotMainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (index) {
            case 0: // главный экран
                Bundle bundle = new Bundle();
                bundle.putBoolean(WIFI_STATE, NetworkIsOk);
                fragment = new TeapotMainFragment();
                fragment.setArguments(bundle);
                break;
            case 1: // окно ввода имени WiFi сети
                fragment = new TeapotSettingsWiFiFragment();
                break;
            case 2: // температурные приделы
                fragment = new TeapotTemperatureLimitsFragment();
                break;
            case 3: // настройка уведомлений
                fragment = new TeapotNotificationFragment();
                break;
            case 4: // настройка цвета
                fragment = new TeapotThemeFragment();
                break;
            case 5:
                SendEmail();
                break;
            case 6:
                stopService(teapot_service);
                TeapotActivity.this.finish();
                break;
            default:
                break;
        }
        if (index < 5) {
            try {
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            } catch (IllegalStateException e) {
                fragmentManager.beginTransaction().add(R.id.content_frame, fragment);
            }
        }
    }

    // вывод диалогового окна
    private void ShowDialogWindow(int header, int messageBody) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TeapotActivity.this);
        builder.setTitle(header)
                .setMessage(messageBody)
                .setCancelable(false)
                .setPositiveButton(R.string.dialogOkButton,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                NetworkIsOk = false;
                                dialog.cancel();
                            }
                        })
                .setNegativeButton(R.string.dialogCancelButton,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                TeapotActivity.this.finish();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void SendEmail() {
        String MessageBody = "Текущее состояние системы:\r\n\r\n";
        MessageBody += "Текущая температура " + String.valueOf(data.getCurrentTemperature())
                + " градусов\r\n";
        MessageBody += "Температура поддержания " + String.valueOf(data.getTargetTemperature())
                + " градусов\r\n";
        MessageBody += "WiFi сеть с устройством " + data.getWiFiName() + "\r\n";
        MessageBody += "Текущий Ip адрес устройства " + data.getWiFiIpAddress() + "\r\n";
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"khadi10@mail.ru"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Log Teapot App");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, MessageBody);
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(Intent.createChooser(emailIntent, "Отправка письма..."));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Не установлен почтовый клиент для отправки письма.",
                    Toast.LENGTH_LONG).show();
        }
    }
}
