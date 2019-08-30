package com.picksplug.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.picksplug.R;
import com.picksplug.helpers.PreferenceConnector;

public class SplashActivity extends AppCompatActivity {

    private Context mContext;
    private static final int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (PreferenceConnector.readBoolean(mContext, PreferenceConnector.TAG_IS_LOGIN,false)){
                    Intent intent = new Intent(mContext, ActDashboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
