package com.blstreampatronage.patrykkrawczyk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Patterns;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView emailErrorTextView;
    private TextView passwordErrorTextView;
    private TextView errorLogText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        emailEditText         = (EditText) findViewById(R.id.emailEditText);
        passwordEditText      = (EditText) findViewById(R.id.passwordEditText);
        emailErrorTextView    = (TextView) findViewById(R.id.emailErrorText);
        passwordErrorTextView = (TextView) findViewById(R.id.passwordErrorText);
        errorLogText          = (TextView) findViewById(R.id.networkLogText);
        errorLogText.setText("");
    }

    public void onLoginButton(View v) {
        String email    = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean properEmail    = validateEmail(email);
        boolean properPassword = validatePassword(password);

        if (properEmail && properPassword) {
            errorLogText.setText("");
            if (checkConnectionAvailability()) loginUser(email, password);
        }
    }

    private boolean validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            emailErrorTextView.setText(R.string.emailTextViewEmpty);
            return false;
        } else {
            emailErrorTextView.setText("");

            boolean isEmailCorrect = Patterns.EMAIL_ADDRESS.matcher(email).matches();

            if (!isEmailCorrect) {
                emailErrorTextView.setText(R.string.emailTextViewWrong);
            }
            return isEmailCorrect;
        }
    }

    private boolean validatePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            passwordErrorTextView.setText(R.string.passwordTextViewEmpty);
            return false;
        } else {
            passwordErrorTextView.setText("");

            final String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
            boolean isPasswordCorrect = Pattern.matches(regex, password);

            if (!isPasswordCorrect) {
                passwordErrorTextView.setText(R.string.passwordTextViewWrong);
            }
            return isPasswordCorrect;
        }
    }

    private void loginUser(String email, String password) {
        SharedPreferences sharedPref    = getSharedPreferences("UserInformation", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(getString(R.string.userEmailKey),    email);
        editor.putString(getString(R.string.userPasswordKey), password);
        editor.commit();

        Intent intent = new Intent(this, ImagesActivity.class);
        startActivity(intent);
        finish();
    }
    public boolean checkConnectionAvailability()
    {
        errorLogText.setText(R.string.activityCheckConnection);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        try
        {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        catch(SecurityException e)
        {
            errorLogText.setText(R.string.errorNoPermissionConnectionStatus);
        }
        catch(Exception e)
        {
            errorLogText.setText(R.string.errorProblemWithConnection);
        }

        return false;
    }

}
