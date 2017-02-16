package com.sk.greate43.flickrbrowser;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by great on 2/10/2017.
 */

class GetFlikrjsonData extends AsyncTask<String,Void,List<Photo>> implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetFlikrjsonData";

    private ArrayList<Photo> mPhotoList = null;
    private String mBaseUrl;
    private String mLanguage;
    private boolean mMatchAll;
    private final OnDataAvailable mCallback;
    private boolean runningOnSameThread=false;

    public GetFlikrjsonData(OnDataAvailable callback, String baseUrl, String language, boolean matchAll) {
        Log.d(TAG, "GetFlikrjsonData: called");
        mBaseUrl = baseUrl;
        mLanguage = language;
        mMatchAll = matchAll;
        mCallback = callback;
    }

    @Override
    protected List<Photo> doInBackground(String... params) {
        Log.d(TAG, "doInBackground: starts");
        String destinationUri = createUri(params[0], mLanguage, mMatchAll);
        GetRawData getRawData = new GetRawData(this);
        getRawData.RunInTheSameThread(destinationUri);
        Log.d(TAG, "doInBackground: ends");
        return mPhotoList;
    }

    @Override
    protected void onPostExecute(List<Photo> photos) {

        Log.d(TAG, "onPostExecute: starts");
        if (mCallback!=null){
            mCallback.onDataAvailable(mPhotoList, DownloadStatus.OK);
        }

        Log.d(TAG, "onPostExecute: ends");

    }

    interface OnDataAvailable {
        void onDataAvailable(ArrayList<Photo> photoList, DownloadStatus status);
    }


    void executeOnSameThread(String searchCriteria) {
        Log.d(TAG, "executeOnSameThread: start");
        runningOnSameThread=true;
        String destinationUri = createUri(searchCriteria, mLanguage, mMatchAll);
        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destinationUri);
        Log.d(TAG, "executeOnSameThread: ends");
    }

    private String createUri(String searchCriteria, String language, boolean matchAll) {
        Log.d(TAG, "createUri: start");
        return Uri.parse(mBaseUrl).buildUpon()
                .appendQueryParameter("tags", searchCriteria)
                .appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY")
                .appendQueryParameter("lang", language)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .build()
                .toString();


    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: start. Status" + status);
        if (status == DownloadStatus.OK) {

            //  Log.d(TAG, "onDownloadComplete: data is" + data);
            mPhotoList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray itemArray = jsonObject.getJSONArray("items");
                for (int i = 0; i < itemArray.length(); i++) {
                    JSONObject jsonPhoto = itemArray.getJSONObject(i);
                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String authorId = jsonPhoto.getString("author_id");
                    String tags = jsonPhoto.getString("tags");
                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String photoUrl = jsonMedia.getString("m");
                    String link = photoUrl.replaceFirst("_m.", "_b.");
                    Photo photoObject = new Photo(title, author, authorId, link, tags, photoUrl);
                    mPhotoList.add(photoObject);

                    Log.d(TAG, "onDownloadComplete:" + photoObject.toString());

                }

            } catch (JSONException e) {
                Log.d(TAG, "onDownloadComplete: error parsing json " + e.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
            }

        }

        if (runningOnSameThread&&mCallback != null) {
            mCallback.onDataAvailable(mPhotoList, status);
        }
        Log.d(TAG, "onDownloadComplete: ends");
    }

}
