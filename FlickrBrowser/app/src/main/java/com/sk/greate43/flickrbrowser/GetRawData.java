package com.sk.greate43.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus {IDLE, PROCESSING, NOT_INITALISED, FAILED_OR_EMPTY, OK}


public class GetRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetRawData";
    private final OnDownloadComplete mCallback;
    private DownloadStatus mDownloadStatus;

    public GetRawData(OnDownloadComplete mCallback) {
        this.mCallback = mCallback;
        mDownloadStatus = DownloadStatus.IDLE;
    }

    @Override
    protected void onPostExecute(String s) {
        // Log.d(TAG, "onPostExecute: parameters " + s);
        try {
            mCallback.onDownloadComplete(s, mDownloadStatus);
        } catch (JSONException e) {
            Log.d(TAG, "onPostExecute: error parsing json");
        }
        Log.d(TAG, "onPostExecute: end");
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        if (params == null) {
            mDownloadStatus = DownloadStatus.NOT_INITALISED;

            return null;
        }

        try {

            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: Response Code" + response);
            StringBuilder result = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while (null != (line = reader.readLine())) {
                result.append(line).append("\n");

            }
            mDownloadStatus = DownloadStatus.OK;
            return result.toString();

        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground: INVALID URL " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: IO Exception reading data " + e.getMessage());
        } catch (SecurityException e) {
            Log.e(TAG, "doInBackground: Security Exception Needs permission " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.d(TAG, "doInBackground: Error Closing stream");
                }
            }
        }
        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }

    void RunInTheSameThread(String s) {
        Log.d(TAG, "RunInTheSameThread: starts");

        if (mCallback!=null){
            try {
                mCallback.onDownloadComplete(doInBackground(s),mDownloadStatus);
            } catch (JSONException e) {
                Log.d(TAG, "RunInTheSameThread: Error parsing json "+e.getMessage());
            }
        }


        Log.d(TAG, "RunInTheSameThread: ends");
    }

    interface OnDownloadComplete {
        void onDownloadComplete(String data, DownloadStatus status) throws JSONException;
    }

}
