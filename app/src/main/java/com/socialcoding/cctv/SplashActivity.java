package com.socialcoding.cctv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by yoon on 2016. 11. 10..
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/NanumBarunGothic_Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        Intent intent = new Intent(this, FacebookLoginActivity.class);
        startActivity(intent);
        finish();
    }
}
