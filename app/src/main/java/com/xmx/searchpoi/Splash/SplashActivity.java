package com.xmx.searchpoi.Splash;

import android.os.Bundle;
import android.os.Handler;

import com.xmx.searchpoi.Constants;
import com.xmx.searchpoi.R;
import com.xmx.searchpoi.Tools.ActivityBase.BaseSplashActivity;

public class SplashActivity extends BaseSplashActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //startMainActivity();
                finish();
            }
        }, Constants.SPLASH_TIME);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}