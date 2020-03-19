package com.fynd.ardemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intentSplash = new Intent(SplashActivity.this, LandActivity.class);
                startActivity(intentSplash);
                // Finish the activity so no going back to loading screen after app has loaded.
                finish();
            }
        }, 2000);
    }
}
