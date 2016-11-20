package com.socialcoding.cctv;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by yoon on 2016. 11. 10..
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.splash_image_view)
                        .setBackgroundResource(R.drawable.eye_of_seoul_logo);
                finish();
            }
        }, 1500);
    }
}
