package com.example.healthup.domain;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequest {

    public interface ResponseListener {
        void onResponse(String response);
        void onError(String error);
    }

    public static void sendPostRequest(String urlString, String jsonBody, ResponseListener listener) {
        new AsyncTask<String, Void, String>() {
            String error = null;

            @Override
            protected String doInBackground(String... params) {
                String response = "";
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(15000);
                    conn.setReadTimeout(15000);

                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type", "application/json; utf-8");
                    conn.setRequestProperty("Accept", "application/json");

                    try (OutputStream os = conn.getOutputStream()) {
                        byte[] input = jsonBody.getBytes("utf-8");
                        os.write(input, 0, input.length);
                        os.flush();
                    }

                    int responseCode = conn.getResponseCode();
                    Log.d("HttpRequestHelper", "Response code: " + responseCode);

                    BufferedReader reader;
                    if (responseCode >= 200 && responseCode < 300) {
                        reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                    } else {
                        reader = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"));
                        error = "Error: " + responseCode;
                    }

                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line.trim());
                    }
                    reader.close();

                    if (error == null) {
                        response = sb.toString();
                    } else {
                        error += " - " + sb.toString();
                    }

                    conn.disconnect();

                } catch (Exception e) {
                    Log.e("HttpRequestHelper", "Exception in HTTP POST request", e);
                    error = e.getMessage();
                }

                return response;
            }

            @Override
            protected void onPostExecute(String s) {
                if (error != null) {
                    listener.onError(error);
                } else {
                    listener.onResponse(s);
                }
            }
        }.execute(urlString);
    }
}
