package com.example.vivek.usbreader;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.InputStream;

public class ImageInfo {
        private String name;
        private String location;
        private InputStream inputStream;
        private Bitmap bitmap;
    public ImageInfo(){

    }

    public ImageInfo(String name, InputStream inputStream) {
        this.name = name;
        this.inputStream = inputStream;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getName() {
        return name;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
