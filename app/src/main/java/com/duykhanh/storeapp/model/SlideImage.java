package com.duykhanh.storeapp.model;

import android.graphics.Bitmap;

import java.util.List;

public class SlideImage {

    List<Bitmap> bitmapList;
    List<String> stringList;

    public SlideImage() {
    }

    public void setBitmapList(List<Bitmap> bitmapList) {
        this.bitmapList = bitmapList;
    }

    public List<Bitmap> getBitmapList() {
        return bitmapList;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }
}
