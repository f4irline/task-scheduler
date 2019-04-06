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

public class HttpFetch extends AsyncTask<URL, Void, String> {

    private HttpURLConnection connection;
    private TaskListener listener;

    public HttpFetch(TaskListener listener) {
        this.listener = listener;
    }

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

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        listener.onTaskCompleted(result);
    }
}
