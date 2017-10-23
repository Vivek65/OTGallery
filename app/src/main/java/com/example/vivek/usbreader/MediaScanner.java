package com.example.vivek.usbreader;

import com.github.mjdev.libaums.fs.UsbFile;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Vivek on 21/10/2017.
 */

public class MediaScanner {

    ArrayList<UsbFile> imageReader(UsbFile root)throws IOException {
        ArrayList<UsbFile> a=new ArrayList<>();
        UsbFile[] files=root.listFiles();
        for(int i=0;i<files.length;i++){
            if(files[i].isDirectory()){
                a.addAll(imageReader(files[i]));
            }
            else{
                if(files[i].getName().endsWith(".jpg") || files[i].getName().endsWith(".png")){
                    a.add(files[i]);
                }
            }
        }
        return a;
    }
    ArrayList<UsbFile> videoReader(UsbFile root)throws IOException {
        ArrayList<UsbFile> a=new ArrayList<>();
        UsbFile[] files=root.listFiles();
        for(int i=0;i<files.length;i++){
            if(files[i].isDirectory()){
                a.addAll(videoReader(files[i]));
            }
            else{
                if(files[i].getName().endsWith(".mp4") ){
                    a.add(files[i]);
                }
            }
        }
        return a;
    }
}
