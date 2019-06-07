package com.minsudongP;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;


public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ImageView iv = (ImageView)findViewById(R.id.loading_imageView);
        DrawableImageViewTarget gifImage = new DrawableImageViewTarget(iv);
        Glide.with(this).load(R.drawable.jumping_location).into(gifImage);
    }
}
