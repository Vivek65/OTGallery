package com.example.vivek.usbreader;


import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.fs.UsbFileStreamFactory;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    FileSystem currentFs;
    ArrayList<UsbFile> list;
    UsbMassStorageDevice device;
    private ArrayList<ImageInfo> images;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;
    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            //call method to set up device communication
                        }
                    } else {
                        Log.d("MyPermission", "permission denied for device " + device);
                    }
                }
            }
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_WRITE_STORAGE);
            return;
        }
        UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);




        registerReceiver(mUsbReceiver, filter);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        images = new ArrayList<>();
        mAdapter = new GalleryAdapter(getApplicationContext(), images);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



        UsbMassStorageDevice[] devices = UsbMassStorageDevice.getMassStorageDevices(this /* Context or Activity */);

        for (UsbMassStorageDevice tmpDevice : devices) {
            mUsbManager.requestPermission(tmpDevice.getUsbDevice(), mPermissionIntent);
            // before interacting with a device you need to call init()!
            try {
                tmpDevice.init();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Only uses the first partition on the device
            currentFs = tmpDevice.getPartitions().get(0).getFileSystem();
            Log.i("USBRead", "Capacity: " + currentFs.getCapacity());
            Log.i("USBRead", "Occupied Space: " + currentFs.getOccupiedSpace());
            Log.i("USBRead", "Free Space: " + currentFs.getFreeSpace());
            Log.i("USBRead", "Chunk size: " + currentFs.getChunkSize());
            device = tmpDevice;

        }

        try {
            if(device==null)
            {
                Intent i=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(i);
            }
            else
                fetchImages();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //    textView.setText(getAllStoragePath().toString());


    }

    public void fetchImages() throws IOException {
        UsbFile root = currentFs.getRootDirectory();
        list = new MediaScanner().imageReader(root);

        String fullPath;
        for (UsbFile tmpIterator : list) {
            ImageInfo image = new ImageInfo();
            image.setInputStream(UsbFileStreamFactory.createBufferedInputStream(tmpIterator,currentFs));
            image.setName(tmpIterator.getName());
            images.add(image);

        }

        mAdapter.notifyDataSetChanged();
    }


}