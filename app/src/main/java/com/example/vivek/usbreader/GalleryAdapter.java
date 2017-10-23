package com.example.vivek.usbreader;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.github.mjdev.libaums.fs.UsbFile;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Vivek on 21/10/2017.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {
    private List<ImageInfo> images;
    private Context mContext;

    public GalleryAdapter(Context mContext,List<ImageInfo> images) {
        this.images = images;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_thumbnail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ImageInfo image = images.get(position);
        final Uri uri=getImageUri(mContext,decodeSampledBitmap(image.getInputStream()));
        Log.i("hello",uri.toString());
        if(uri !=null) {
            Glide.with(mContext).load(uri).into(holder.thumbnail);
            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(Intent.ACTION_VIEW);
                    i.setType("image/*");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setData(uri);
                    mContext.startActivity(i);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return images.size();

    }

    public void setImages(List<ImageInfo> images) {
        this.images = images;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        public MyViewHolder(View itemView) {
            super(itemView);
            thumbnail=(ImageView)itemView.findViewById(R.id.thumbnail);

        }
    }
    public static Bitmap decodeSampledBitmap(InputStream res) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();

        // Calculate inSampleSize
        options.inSampleSize = 18;

        // Decode bitmap with inSampleSize set
        return BitmapFactory.decodeStream(res,null,options);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


}
