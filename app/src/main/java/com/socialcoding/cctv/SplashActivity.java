package com.socialcoding.cctv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by yoon on 2016. 11. 10..
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
