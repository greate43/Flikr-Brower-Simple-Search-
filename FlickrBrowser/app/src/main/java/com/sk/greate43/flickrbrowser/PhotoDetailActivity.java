package com.sk.greate43.flickrbrowser;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        activateToolbar(true);

        Intent intent = getIntent();

        Photo photo = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);
        if (photo != null) {
            TextView photoTitle=(TextView) findViewById(R.id.photo_title);
            TextView photoTags=(TextView) findViewById(R.id.photo_tag);
            TextView photoAuthor=(TextView) findViewById(R.id.photoAuthor);
            ImageView photoImage=(ImageView)findViewById(R.id.photo_image);

            photoTitle.setText("Title : "+photo.getTitle());
            photoTags.setText("Tags : "+photo.getTags());
            photoAuthor.setText(photo.getAuthor());


            Picasso.with(this)
                    .load(photo.getLink())
                    .error(R.drawable.ic_image_black_48dp)
                    .placeholder(R.drawable.ic_image_black_48dp)
                    .into(photoImage);
        }

    }

}
