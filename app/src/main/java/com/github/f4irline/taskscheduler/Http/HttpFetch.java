package com.github.f4irline.taskscheduler.Http;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Executes the http get request asynchronously to avoid locking the UI since tasks like this
 * may take some time.
 *
 * <p>
 * The class is given URl as a parameter and expects a String result after the background task
 * is finished.
 * </p>
 *
 * @author Tommi Lepola
 * @version 3.0
 * @since 2019.0406
 */
public class HttpFetch extends AsyncTask<URL, Void, String> {

    private HttpURLConnection connection;
    private TaskListener listener;

    /**
     * Initializes the listener to which the http request response is returned to.
     *
     * @param listener the listener implementation which is called when the task is completed.
     */
    public HttpFetch(TaskListener listener) {
        this.listener = listener;
    }

    /**
     * Does the http fetch in background.
     *
     * <p>
     * The method does the http fetch and append's it's result line by line to a StringBuilder.
     * </p>
     *
     * <p>
     * When the task is finished, it disconnects from the http connection and returns the result
     * to the onPostExecute() method.
     * </p>
     *
     * @param params the parameters given to the class when initialized, in this case the URL.
     * @return the result of the task as a string, which is then parsed with Gson.
     */
    @Override
    public String doInBackground(URL... params) {
        StringBuilder result = new StringBuilder();
        InputStream in = null;

        Log.d("doInBackground", params[0].toString());

        try {
            URL url = params[0];
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");

            in = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return result.toString();
    }

    /**
     * Called when the background task is finished.
     *
     * @param result the result of the background task.
     */
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        listener.onTaskCompleted(result);
    }
}
