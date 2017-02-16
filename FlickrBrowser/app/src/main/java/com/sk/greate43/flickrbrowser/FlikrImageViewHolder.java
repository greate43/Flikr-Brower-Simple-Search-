package com.sk.greate43.flickrbrowser;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by great on 2/14/2017.
 */

class FlikrImageViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "FlikrImageViewHolder";
    ImageView thumbnail = null;
    TextView title = null;

    public FlikrImageViewHolder(View itemView) {
        super(itemView);
        Log.d(TAG, "FlikrImageViewHolder: start");

        thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        title = (TextView) itemView.findViewById(R.id.title);
        Log.d(TAG, "FlikrImageViewHolder: ends");
    }


}
