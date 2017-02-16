package com.sk.greate43.flickrbrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by great on 2/14/2017.
 */

class FlikrRecyclerViewAdapter extends RecyclerView.Adapter<FlikrImageViewHolder> {
    private static final String TAG = "FlikrRecyclerViewAdapte";
    private List<Photo> mPhotos;
    private Context mContext;

    public FlikrRecyclerViewAdapter(Context context, List<Photo> photos) {
        mPhotos = photos;
        mContext = context;
    }

    @Override
    public FlikrImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: New View Requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);
        return new FlikrImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FlikrImageViewHolder holder, int position) {

        if (mPhotos == null || mPhotos.size() == 0) {
            holder.thumbnail.setImageResource(R.drawable.ic_image_black_48dp);
            holder.title.setText("No Photo matched your search \n\n Use search icons to search photos");
        }else {
            Photo photoItem = mPhotos.get(position);

            Log.d(TAG, "onBindViewHolder: " + photoItem.getTitle() + "---->" + position);

            Picasso.with(mContext)
                    .load(photoItem.getImage())
                    .error(R.drawable.ic_image_black_48dp)
                    .placeholder(R.drawable.ic_image_black_48dp)
                    .into(holder.thumbnail);
            holder.title.setText(photoItem.getTitle());
        }


    }

    @Override
    public int getItemCount() {

        return ((mPhotos != null && mPhotos.size() != 0) ? mPhotos.size() : 1);
    }

    public void LoadNewData(List<Photo> newPhotos) {
        mPhotos = newPhotos;
        notifyDataSetChanged();
    }

    public Photo getPhoto(int position) {
        return ((mPhotos != null && mPhotos.size() != 0) ? mPhotos.get(position) : null);
    }
}
