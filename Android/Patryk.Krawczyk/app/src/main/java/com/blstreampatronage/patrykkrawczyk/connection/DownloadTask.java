package com.blstreampatronage.patrykkrawczyk.connection;

import android.os.AsyncTask;
import android.util.Log;
import com.blstreampatronage.patrykkrawczyk.events.LoadNewImagesEvent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import de.greenrobot.event.EventBus;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Class used by ConnectionHandler to realize AsyncTasks on provided URL
 */
public class DownloadTask extends AsyncTask<URL, Integer, Boolean> implements Callback {

    private final OkHttpClient mClient;
    private List<DownloadTask> mTasks;

    /**
     * Constructor for DownloadTask, executes task immediately
     * @param url address at which we want to do GET request
     * @param okHttpClient client that is used to make request
     * @param downloadTaskList List of tasks handled by ConnectionHandler
     */
    public DownloadTask(URL url, OkHttpClient okHttpClient, List<DownloadTask> downloadTaskList) {
        mClient = okHttpClient;
        mTasks  = downloadTaskList;

        this.execute(url);
    }

    /**
     * Task that will be ran in background
     * @param urls address that should be asked with GET request, accepts array but only [0] will be used
     * @return true if tasks has finished successfully
     */
    protected Boolean doInBackground(URL... urls) {

        Request request = new Request.Builder().url(urls[0]).build();

        if (isCancelled()) {
            return false;
        }

        mClient.newCall(request).enqueue(this);

        return true;
    }

    /**
     * The GET Request has failed, dispatch Event with isError flag set to true
     * @param request  provided by Client, used to print its body
     * @param exception exception that was returned, source of failure
     */
    @Override
    public void onFailure(Request request, IOException exception) {
        exception.printStackTrace();
        Log.d("TAG", request.toString());

        EventBus.getDefault().post(new LoadNewImagesEvent(request.toString(), true));
    }

    /**
     * GET Request has succeeded, note that this means task was able to connect to URL
     * Check the code to make sure response was 200OK
     * It then dispatches Event with server response body
     * @param response server response provided by our Client, check its body() and code()
     */
    @Override
    public void onResponse(Response response) throws IOException {
        int     code  = response.code();
        boolean error = false;

        if (code != 200) {
            error = true;
        }
        String message = response.body().string();

        EventBus.getDefault().post(new LoadNewImagesEvent(message, error));
    }

    /**
     * When task has finished, remove it from ConnectionHandler tasks list
     * @param aBoolean success/failure of task
     */
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        mTasks.remove(this);
        super.onPostExecute(aBoolean);
    }

    /**
     * When task was cancelled by user, remove it from ConnectionHandler tasks list
     * @param aBoolean set to true if it should cancel when task is currently running
     */
    @Override
    protected void onCancelled(Boolean aBoolean) {
        mTasks.remove(this);
        super.onCancelled(aBoolean);
    }
}