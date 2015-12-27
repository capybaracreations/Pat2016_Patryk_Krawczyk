package com.blstreampatronage.patrykkrawczyk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView userEmailTextView;
    private TextView userPasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        SharedPreferences sharedPref = getSharedPreferences("UserInformation", this.MODE_PRIVATE);
        String userEmail    = sharedPref.getString(getString(R.string.userEmailKey), null);
        String userPassword = sharedPref.getString(getString(R.string.userPasswordKey), null);

        userEmailTextView    = (TextView) findViewById(R.id.mainUserEmail);
        userPasswordTextView = (TextView) findViewById(R.id.mainUserPassword);

        userEmailTextView.setText(userEmail);
        userPasswordTextView.setText(userPassword);
    }

    public void onLogOutUser(View v) {
        SharedPreferences sharedPref    = getSharedPreferences("UserInformation", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(getString(R.string.userEmailKey),    null);
        editor.putString(getString(R.string.userPasswordKey), null);
        editor.commit();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
