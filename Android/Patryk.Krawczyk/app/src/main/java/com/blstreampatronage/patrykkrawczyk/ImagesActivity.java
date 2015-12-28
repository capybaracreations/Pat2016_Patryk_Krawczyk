package com.blstreampatronage.patrykkrawczyk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class ImagesActivity extends AppCompatActivity implements AbsListView.OnScrollListener {

    private ArrayList<SingleImage> mImageList;
    private GridView               mImageGrid;
    private ProgressBar            mProgressBar;
    private int                    mCurrentPage = -1;
    private boolean                mContinueLoading = true;
    final static String            BASE_SERVER_URL = "http://192.168.88.250:8080/"; // remember about trailing /

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

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

        mImageList = new ArrayList<>();

        mImageGrid = (GridView) findViewById(R.id.imageGrid);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        if (getResources().getBoolean(R.bool.isTablet)) mImageGrid.setNumColumns(4);
        mImageGrid.setAdapter(new ImageAdapter(this, mImageList));

        mImageGrid.setOnScrollListener(this);
    }

    private void getPage() {
        String url = BASE_SERVER_URL + "page_" + mCurrentPage + ".json";

        mContinueLoading = false;

        StringRequest request = new StringRequest
                (Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        mContinueLoading = true;
                        try {
                            feedImages(response);
                        } catch (IOException e) {
                            e.printStackTrace();
                            // TODO handle the exception gracefully
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mContinueLoading = false;
                        mProgressBar.setVisibility(View.INVISIBLE);
                        Log.d("CONNECTION", "Error: " + error.toString());
                        // TODO make it visible to user
                    }
                });

        ConnectionHandler.getInstance(this).addToRequestQueue(request);
    }

    private void feedImages(String response) throws IOException {
        JsonImageArray imageArray = null;

        ObjectMapper mapper = new ObjectMapper(); // Jackson powah
        imageArray = mapper.readValue(response, JsonImageArray.class);

        if (null != imageArray) {
            Collection<SingleImage> array = imageArray.getArray();
            for(SingleImage image : array)
                mImageList.add(image);
        }

        mProgressBar.setVisibility(View.INVISIBLE);
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

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO handle maybe?
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mContinueLoading)
            if(firstVisibleItem + visibleItemCount >= totalItemCount) {
                mProgressBar.setVisibility(View.VISIBLE);
                ++mCurrentPage;
                getPage();
            }
    }
}
