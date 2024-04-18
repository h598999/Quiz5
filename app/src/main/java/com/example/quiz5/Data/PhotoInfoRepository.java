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

//Repository class
public class PhotoInfoRepository {
    //Declaring private fields
    private PhotoInfoDao dao;
    private LiveData<List<PhotoInfo>> allPictures;
    private Application application;

    // Constructor for the PhotoInfoRepository object, takes an Application as a parameter
    public PhotoInfoRepository(Application application){
        // Defines the callback that initiates data in the database
        PhotoInfoRoomDatabase.setRoomDatabaseCallback(application.getApplicationContext());
        // Sets the db field to be the result of the getDatabase method from the static PhotoInfoRoomDatabase class
        PhotoInfoRoomDatabase db = PhotoInfoRoomDatabase.getDatabase(application);
        this.application = application;
        // Sets the dao field to be the dao instance from the database
        dao = db.dao();
        // Sets the allPictures field to be the result of the getAllEntries method from the dao
        allPictures = dao.getAllEntries();
    }

    // Returns the value of the allPictures field
    public LiveData<List<PhotoInfo>> getAllPictures(){
        Log.d("Database", "Retrieved all pictures");
        return allPictures;
    }

    //Inserts the parameter into the database
    public void insert(PhotoInfo photoinfo){
        Log.d("Database", "Inserted photo with name: " + photoinfo.getName() + " and URI: " + photoinfo.getImageData());
        // Uses the ExecutorService from the database to asynchronusly insert the parameter into the database
        PhotoInfoRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.insert(photoinfo);
        });
    }

    // Deletes the given parameter from the database
    public void delete(PhotoInfo photoInfo) {
        Log.d("Database", "Deleted photo with name: " + photoInfo.getName() + " and URI: " + photoInfo.getImageData());
        // Uses the ExecutorService from the database to asynchronusly delete the parameter into the database
        PhotoInfoRoomDatabase.databaseWriteExecutor.execute(() -> {
            dao.delete(photoInfo);
        });
    }
}
