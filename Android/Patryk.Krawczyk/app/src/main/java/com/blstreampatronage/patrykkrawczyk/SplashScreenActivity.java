package com.blstreampatronage.patrykkrawczyk;

import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class SplashScreenActivity extends AppCompatActivity
{

    private CountDownTimer countDownTimer;
    private long splashDuration    = 5000;
    private long splashRefreshRate = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Hide action bar
        getSupportActionBar().hide();

        // Hide notification/status bar
        if (Build.VERSION.SDK_INT < 16) {
            int flagFullscreen = WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setFlags(flagFullscreen, flagFullscreen);
        } else {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

        createCountdown(splashDuration, splashRefreshRate);
    }

    private void createCountdown(long splashDuration, long splashRefreshRate) {

        countDownTimer = new CountDownTimer(splashDuration, splashRefreshRate) {

            public void onTick(long millisUntilFinished) {
                // TODO: implement something to let user know that app is running, adjust @splashRefreshRate
            }

            public void onFinish() {
                moveToMainActivity();
            }
        };

        countDownTimer.start();
    }

    private void moveToMainActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        // TODO: implement something to let user know that app is running
        if (null != countDownTimer) {
            countDownTimer.cancel();
        }
    }
}
