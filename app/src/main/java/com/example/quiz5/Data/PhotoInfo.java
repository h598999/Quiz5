package com.example.quiz5.Data;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "info_table")
public class PhotoInfo {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int Id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] imageData;

    public PhotoInfo(String name, byte[] imageData){
        this.name = name;
        this.imageData = imageData;
    }

    public void setId(int newId){
        this.Id = newId;
    }
    public String getName(){
        return this.name;
    }

    public void setName(String newName){
        this.name = newName;
    }

    public byte[] getImageData(){
        return this.imageData;
    }

    public void setImageUri(byte[] newImageData){
        this.imageData = newImageData;
    }

    public int getId(){
        return this.Id;
    }
}
