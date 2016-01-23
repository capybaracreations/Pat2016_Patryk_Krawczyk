package com.blstreampatronage.patrykkrawczyk.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Patterns;

import com.blstreampatronage.patrykkrawczyk.DebugHelper;
import com.blstreampatronage.patrykkrawczyk.R;

import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Activity that allows user to log in, will validate email and password for correctness
 */
public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.emailEditText)     EditText emailEditText;
    @Bind(R.id.passwordEditText)  EditText passwordEditText;
    @Bind(R.id.emailErrorText)    TextView emailErrorTextView;
    @Bind(R.id.passwordErrorText) TextView passwordErrorTextView;
    @Bind(R.id.networkLogText)    TextView errorLogText;
    /**
     * Hide action bar, go full screen, hide notification bar
     * Binds ButterKnife
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DebugHelper.initializeDebugHelpers(this, true, true);
        ButterKnife.bind(this);
        errorLogText.setText("");

        checkIfUserIsAlreadyLogedIn();
    }

    /**
     * Log in user that already provided his credentials before
     */
    private void checkIfUserIsAlreadyLogedIn() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.loginInformationFile), Context.MODE_PRIVATE);
        String userEmail = sharedPref.getString(getString(R.string.userEmailKey), "");
        String userPassword = sharedPref.getString(getString(R.string.userPasswordKey), "");

        boolean properEmail = !userEmail.isEmpty();
        boolean properPassword = !userPassword.isEmpty();

        Log.v("tag", ""+properEmail);
        Log.v("tag", ""+properPassword);

        if (properEmail && properPassword) {
            moveToImagesActivity();
        }
    }

    /**
     * When user presses the login button, email and password will be validated
     * If everything is alright, user wil be logged in and activity will change
     */
    public void onLoginButton(View view) {
        String  email          = emailEditText.getText().toString();
        String  password       = passwordEditText.getText().toString();

        boolean properEmail    = validateEmail(email);
        boolean properPassword = validatePassword(password);

        if (properEmail && properPassword) {
            errorLogText.setText("");
            if (checkConnectionAvailability()) {
                loginUser(email, password);
            }
        }
    }

    /**
     * Test if the email is not empty, and then if matches pattern of email
     * @param email string containing user provided email to be validated
     * @return success or false of validation
     */
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

    /**
     * Test if the password is not empty, and then if it matches regex
     * @param password string containing user provided password to be validated
     * @return success or false of validation
     */
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

    /**
     * After checking if email and password is correct, log user in and change activity
     * @param email user provided email that has been tested for correctness
     * @param password user provided password that has been tested for correctness
     */
    private void loginUser(String email, String password) {
        SharedPreferences sharedPref    = getSharedPreferences(getString(R.string.loginInformationFile), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(getString(R.string.userEmailKey),    email);
        editor.putString(getString(R.string.userPasswordKey), password);
        editor.apply();

        moveToImagesActivity();
    }

    /**
     * Test if user have access to internet and is connected
     * @return true if user has internet access
     */
    public boolean checkConnectionAvailability() {
        errorLogText.setText(R.string.activityCheckConnection);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        catch(SecurityException se) {
            errorLogText.setText(R.string.errorNoPermissionConnectionStatus);
        }
        catch(Exception e) {
            errorLogText.setText(R.string.errorProblemWithConnection);
        }

        return false;
    }

    private void moveToImagesActivity() {
        Intent intent = new Intent(this, ImagesActivity.class);
        startActivity(intent);
        finish();
    }

}
