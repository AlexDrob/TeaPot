package com.bignerdranch.android.teapot;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TeapotActivity extends ActionBarActivity {

    private static final String TAG = "TeapotActivity";
    private static final String WIFI_STATE = "WiFi_state";

    private DrawerLayout myDrawerLayout;
    private ListView myDrawerList;
    private ActionBarDrawerToggle myDrawerToggle;

    // navigation drawer title
    private CharSequence myDrawerTitle;
    // used to store app title
    private CharSequence myTitle;

    private String[] viewsNames;

    private boolean NetworkIsOk = false;

    private int list_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teapot);

        list_index = 0;
        NetworkIsOk = false;

        myTitle =  getTitle();
        myDrawerTitle = getResources().getString(R.string.menu);

        // load slide menu items
        viewsNames = getResources().getStringArray(R.array.views_array);
        myDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        myDrawerList = (ListView) findViewById(R.id.left_drawer);

        myDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, viewsNames));

        myDrawerToggle = new ActionBarDrawerToggle(this, myDrawerLayout,
                R.string.open_menu,
                R.string.close_menu
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(myTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(myDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        myDrawerLayout.setDrawerListener(myDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        myDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTopGround)));

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        myDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        myDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (myDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(
                AdapterView<?> parent, View view, int position, long id
        ) {
            // display view for selected nav drawer item
            Log.d(TAG, "tap on nav drawer item " + String.valueOf(position));

            myDrawerLayout.closeDrawers();

            ShowCurrentFragment(position);
        }
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
                TeapotData data = new TeapotData();
                // восстанавливаем данные
                TeapotSharedPreferences TeapotPreferences = new TeapotSharedPreferences();
                TeapotPreferences.TeapotReStoreData(data, getApplicationContext());
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
        ShowCurrentFragment(list_index);
    }

    private void ShowCurrentFragment(int index) {
        list_index = index;
        switch (index) {
            case 0:

                Bundle bundle = new Bundle();
                bundle.putBoolean(WIFI_STATE, NetworkIsOk);
                FragmentManager fm = getSupportFragmentManager();
                //Fragment fragment = fm.findFragmentById(R.id.content_frame);
                //if (fragment == null) {
                Fragment fragment = new TeapotMainFragment();
                fragment.setArguments(bundle);

                fm.beginTransaction().replace(R.id.content_frame, fragment)
                        .commit();
                //}
                break;
            case 1:
                FragmentManager fm1 = getSupportFragmentManager();
                //Fragment fragment1 = fm1.findFragmentById(R.id.teapot_settings_wifi_fragment_container);
                //if (fragment1 == null) {
                Fragment fragment1 = new TeapotSettingsWiFiFragment();
                fm1.beginTransaction().replace(R.id.content_frame, fragment1)
                        .commit();

                break;
            case 5:
                TeapotActivity.this.finish();
                break;
            default:
                break;
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
