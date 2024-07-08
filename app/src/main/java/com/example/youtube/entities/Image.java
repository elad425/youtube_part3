package com.example.youtube.entities;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.youtube.utils.imageConverter;

@Entity(tableName = "image")
public class Image {
    @PrimaryKey
    @NonNull
    private String path;
    private byte[] img;

    public Image(byte[] img, @NonNull String path) {
        this.img = img;
        this.path = path;
    }

    @NonNull
    public String getPath() {
        return path;
    }

    public void setPath(@NonNull String path) {
        this.path = path;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }
}
