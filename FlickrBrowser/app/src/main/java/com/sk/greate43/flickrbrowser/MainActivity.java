package com.sk.greate43.flickrbrowser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends BaseActivity
        implements GetFlikrjsonData.OnDataAvailable,
        RecyclerItemClickListener.OnRecyclerClickListenier {
    private static final String TAG = "MainActivity";
 private SharedPreferences sharedPreferfances;
    private FlikrRecyclerViewAdapter mFlikrRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activateToolbar(false);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));
        mFlikrRecyclerViewAdapter = new FlikrRecyclerViewAdapter(this, new ArrayList<Photo>());

        recyclerView.setAdapter(mFlikrRecyclerViewAdapter);

        Log.d(TAG, "onCreate: ends");
    }


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();

         sharedPreferfances = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String queryResult = sharedPreferfances.getString(FLIKR_QUERY, "Android");


        if (queryResult.length() > 0) {
            GetFlikrjsonData getFlikrjsonData = new GetFlikrjsonData(this, "https://api.flickr.com/services/feeds/photos_public.gne", "en-us", true);

            getFlikrjsonData.execute(queryResult);
        }

        Log.d(TAG, "onResume: ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "onCreateOptionsMenu() returned: " + true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_delete) {
            sharedPreferfances.edit().remove(FLIKR_QUERY).commit();
            Toast.makeText(MainActivity.this, "Last Search Result Deleted " , Toast.LENGTH_LONG).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDataAvailable(ArrayList<Photo> data, DownloadStatus status) {
        Log.d(TAG, "onDataAvailable: starts");
        if (status == DownloadStatus.OK) {
            mFlikrRecyclerViewAdapter.LoadNewData(data);
        } else {
            Log.e(TAG, "onDataAvailable: falied with status " + status);
        }
        Log.d(TAG, "onDataAvailable: ends");
    }

    @Override
    public void OnItemClick(View v, int position) {
        Log.d(TAG, "OnItemClick: starts");
        Toast.makeText(MainActivity.this, "normal click position " + position, Toast.LENGTH_LONG).show();
    }

    @Override
    public void OnItemLongClick(View v, int position) {
        Log.d(TAG, "OnItemLongClick: starts");

        Intent intent = new Intent(this, PhotoDetailActivity.class);
        intent.putExtra(PHOTO_TRANSFER, mFlikrRecyclerViewAdapter.getPhoto(position));
        startActivity(intent);
        Toast.makeText(MainActivity.this, "Long click position " + position, Toast.LENGTH_LONG).show();

    }
}
