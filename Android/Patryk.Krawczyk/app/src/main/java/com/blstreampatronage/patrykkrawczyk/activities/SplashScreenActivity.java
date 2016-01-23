package com.blstreampatronage.patrykkrawczyk.activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.blstreampatronage.patrykkrawczyk.DebugHelper;
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

        DebugHelper.initializeDebugHelpers(this, true, true);

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
                moveToLogInActivity();
            }
        };

        countDownTimer.start();
    }

    /**
     * Cancel current countdown
     */
    private void cancelCountdown() {
        if (null != countDownTimer) {
            countDownTimer.cancel();
        }
    }

    /**
     * Move user to the next activity
     */
    private void moveToLogInActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Cancel the countdown on back button press
     */
    @Override
    public void onBackPressed() {
        cancelCountdown();
    }

    /**
     * Cancel the countdown on leaving the app
     */
    @Override
    protected void onPause() {
        super.onPause();
        cancelCountdown();
    }
}
