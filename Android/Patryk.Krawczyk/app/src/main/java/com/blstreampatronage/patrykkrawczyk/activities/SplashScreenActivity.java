package com.blstreampatronage.patrykkrawczyk.activities;

import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.blstreampatronage.patrykkrawczyk.R;

/**
 * This activity will only be shown for a certain period of time
 * Set "SPLASH_DURATION" to define how much should user spend on this activity
 * Set "SPLASH_REFRESH_RATE" to define how often should this activity refresh
 */
public class SplashScreenActivity extends AppCompatActivity
{
    private CountDownTimer    countDownTimer;
    private static final long SPLASH_DURATION     = 5000;
    private static final long SPLASH_REFRESH_RATE = 5000;

    /**
     * Hide action bar, go full screen, hide notification bar
     * Starts countdown until activity change
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Hide action bar
        ActionBar ab = null;
        try {
            ab = getSupportActionBar();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (null != ab) ab.hide();

        // Hide notification/status bar
        if (Build.VERSION.SDK_INT < 16) {
            int flagFullscreen = WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setFlags(flagFullscreen, flagFullscreen);
        } else {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

        createCountdown();
    }

    /**
     * Created a new countdown after which activity will change to next one
     */
    private void createCountdown() {

        countDownTimer = new CountDownTimer(SplashScreenActivity.SPLASH_DURATION, SplashScreenActivity.SPLASH_REFRESH_RATE) {

            public void onTick(long millisUntilFinished) {
                // TODO: implement something to let user know that app is running, adjust @SPLASH_REFRESH_RATE
            }

            public void onFinish() {
                moveToMainActivity();
            }
        };

        countDownTimer.start();
    }

    /**
     * Move user to the next activity
     */
    private void moveToMainActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * When user presses the back button, countdown will be canceled
     */
    @Override
    public void onBackPressed() {
        // TODO: implement something to let user know that app is running
        if (null != countDownTimer) {
            countDownTimer.cancel();
        }
    }
}
