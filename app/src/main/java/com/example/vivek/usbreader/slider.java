package com.example.vivek.usbreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoViewAttacher;

public class slider extends AppCompatActivity {
    ImageView imageView;
    PhotoViewAttacher photoViewAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_slider);

        imageView=(ImageView)findViewById(R.id.imageView);
        photoViewAttacher=new PhotoViewAttacher(imageView);
        photoViewAttacher.update();


    }
}
