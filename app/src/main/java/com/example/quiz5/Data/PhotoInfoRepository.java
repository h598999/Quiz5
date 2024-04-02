package com.example.quiz5.Data;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PhotoInfoRepository {
    private PhotoInfoDao dao;
    private LiveData<List<PhotoInfo>> allPictures;
    private Application application;

    public PhotoInfoRepository(Application application){
        PhotoInfoRoomDatabase.setRoomDatabaseCallback(application.getApplicationContext());
        PhotoInfoRoomDatabase db = PhotoInfoRoomDatabase.getDatabase(application);
        this.application = application;
        dao = db.dao();
        allPictures = dao.getAllEntries();
    }

    public LiveData<List<PhotoInfo>> getAllPictures(){
        Log.d("Database", "Retrieved all pictures");
        return allPictures;
    }

    public void insert(PhotoInfo photoinfo){
        Log.d("Database", "Inserted photo with name: " + photoinfo.getName() + " and URI: " + photoinfo.getImageData());
        PhotoInfoRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.insert(photoinfo);
        });
    }

    public void insert(String name, Uri imageUri){
        PhotoInfo photoinfo = new PhotoInfo(name, convertUriToByteArray(imageUri));
        Log.d("Database", "Inserted photo with name: " + photoinfo.getName() + " and URI: " + photoinfo.getImageData());
        PhotoInfoRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.insert(photoinfo);
        });
    }

    private byte[] convertUriToByteArray(Uri imageUri) {
        try {
            // Open an input stream from the image URI
            InputStream inputStream = this.application.getApplicationContext().getContentResolver().openInputStream(imageUri);
            if (inputStream != null) {
                // Decode the input stream into a bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                // Convert the bitmap to a byte array
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                // Close the input stream
                inputStream.close();

                return byteArray;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void resetDatabase() {
        Log.d("Database", "Database reset");
        PhotoInfoRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.deleteAll();
        });
    }

    public void delete(PhotoInfo photoInfo) {
        Log.d("Database", "Deleted photo with name: " + photoInfo.getName() + " and URI: " + photoInfo.getImageData());
        PhotoInfoRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.delete(photoInfo);
        });
    }

    public void printAllEntries(){
        Log.d("Database", "Printed all entries");
        PhotoInfoRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<PhotoInfo> allEntries = dao.getAllEntriesSync(); // Get entries synchronously
            for (PhotoInfo entry : allEntries) {
                Log.d("Database", "id: " + entry.getId() + "Name: " + entry.getName() + " Uri: "+ entry.getImageData());
            }
        });
    }
}
