package com.bignerdranch.android.teapot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by AREG on 27.01.2017.
 */

public class TeapotNotificationFragment extends Fragment {

    private FrameLayout mFrameLayout;

    private TeapotData data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.teapot_notifications_fragment, container, false);

        data = TeapotData.get();

        mFrameLayout = (FrameLayout) v.findViewById(R.id.notifications_fragment);

        switch (data.getColorTheme()) {
            case 1:
                mFrameLayout.setBackgroundResource(R.color.colorBackGround);
                break;
            case 2:
                mFrameLayout.setBackgroundResource(R.color.colorBackGround2);
                break;
            case 3:
                mFrameLayout.setBackgroundResource(R.color.colorBackGround3);
                break;
        }

        return v;
    }
}
