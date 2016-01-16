package com.blstreampatronage.patrykkrawczyk.connection;

// Singleton

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;

/**
 * Singleton class that allows you to execute background GET requests on specified URL.
 * Using this class you are only allowed to run and cancel tasks.
 */
public class ConnectionHandler {

        private static ConnectionHandler  mInstance;
        private        OkHttpClient       mClient;
        private        List<DownloadTask> mTasks;

        private ConnectionHandler() {
            mClient = getOkHttpClient();
            mTasks  = new ArrayList<>();
        }

        private OkHttpClient getOkHttpClient() {
            if (mClient == null) {
                mClient = new OkHttpClient();
            }

            return mClient;
        }

    /**
     *  Method used to receive instance of 'ConnectionHandler'
     * @return returns instance of singleton 'ConnectionHandler'
     */
        public static synchronized ConnectionHandler getInstance() {
            if (mInstance == null) {
                mInstance = new ConnectionHandler();
            }

            return mInstance;
        }

    /**
     *  Method used to execute GET request on 'url'
     * @param url object of type 'URL' pointing to site, e.g. "http://example.com/"
     * @return on success: returns 'DownloadTask' instance, containing invoked task
     *         on failure: returns null
     */
        private DownloadTask doGetRequest(URL url) {
            DownloadTask dt;

            try {
                dt = new DownloadTask(url, getOkHttpClient(), mTasks);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            if (null != dt) mTasks.add(dt);

            return dt;
        }

    /**
     *  Method used to execute GET request on 'strURL'
     * @param strUrl url in string format e.g. "http://example.com/"
     * @return on success: returns 'DownloadTask' instance, containing invoked task
     *         on failure: returns null
     */
        public DownloadTask doGetRequest(String strUrl) {
            URL url;

            try {
                url = new URL(strUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            return doGetRequest(url);
        }


    /**
     *  Method used to finish all currently running tasks
     * @return on success: returns true, meaning all tasks have been canceled
     *         on failure: returns false, meaning there was an exception during canceling
     */
        public boolean finishAllTasks() {
            boolean notInterrupted = true;

            while (mTasks.size() > 0) {
                try {
                    mTasks.get(0).cancel(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    notInterrupted = false;
                    break;
                }
            }

            return notInterrupted;
        }
}

