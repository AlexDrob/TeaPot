package com.bignerdranch.android.teapot;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by AREG on 27.01.2017.
 */

public class TeapotThemeFragment extends Fragment {

    private TextView mTheme1;
    private TextView mTheme2;
    private TextView mTheme3;
    private LinearLayout mLinearLayout;

    private TeapotData data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.teapot_theme_fragment, container, false);

        data = TeapotData.get();

        mTheme1 = (TextView) v.findViewById(R.id.theme1);
        mTheme2 = (TextView) v.findViewById(R.id.theme2);
        mTheme3 = (TextView) v.findViewById(R.id.theme3);
        mLinearLayout = (LinearLayout) v.findViewById(R.id.theme_fragment);

        View.OnClickListener theme1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinearLayout.setBackgroundResource(R.color.colorBackGround);
                ((ActionBarActivity)getActivity()).getSupportActionBar().
                        setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTopGround)));
                data.setColorTheme(1);
            }
        };

        View.OnClickListener theme2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinearLayout.setBackgroundResource(R.color.colorBackGround2);
                ((ActionBarActivity)getActivity()).getSupportActionBar().
                        setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTopGround2)));
                data.setColorTheme(2);
            }
        };

        View.OnClickListener theme3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinearLayout.setBackgroundResource(R.color.colorBackGround3);
                ((ActionBarActivity)getActivity()).getSupportActionBar().
                        setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTopGround3)));
                data.setColorTheme(3);
            }
        };

        mTheme1.setOnClickListener(theme1);
        mTheme2.setOnClickListener(theme2);
        mTheme3.setOnClickListener(theme3);

        switch (data.getColorTheme()) {
            case 1:
                mLinearLayout.setBackgroundResource(R.color.colorBackGround);
                break;
            case 2:
                mLinearLayout.setBackgroundResource(R.color.colorBackGround2);
                break;
            case 3:
                mLinearLayout.setBackgroundResource(R.color.colorBackGround3);
                break;
        }

        return v;
    }
}
