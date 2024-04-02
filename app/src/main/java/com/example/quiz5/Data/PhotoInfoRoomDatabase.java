package com.example.quiz5.Data;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.quiz5.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {PhotoInfo.class}, version = 1, exportSchema = false)
public abstract class PhotoInfoRoomDatabase extends RoomDatabase {

    public abstract PhotoInfoDao dao();

    private static PhotoInfoRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static PhotoInfoRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            //Build the database
            synchronized (PhotoInfoRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PhotoInfoRoomDatabase.class, "photoinfo_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        //return the INSTANCE
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback;

    public static void setRoomDatabaseCallback(Context context) {
        sRoomDatabaseCallback = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                databaseWriteExecutor.execute(() -> {
                    PhotoInfoDao dao = INSTANCE.dao();
                    dao.deleteAll();

                    // Add images from URIs
                    addImageFromUri(context, dao, "Java", "android.resource://com.example.quiz5/" + R.drawable.java);
                    addImageFromUri(context, dao, "Python", "android.resource://com.example.quiz5/" + R.drawable.python);
                    addImageFromUri(context, dao, "Neovim", "android.resource://com.example.quiz5/" + R.drawable.neovim);
                });
            }

            private void addImageFromUri(Context context, PhotoInfoDao dao, String name, String imageUriString) {
                try {
                    // Convert the URI string to a Uri object
                    Uri imageUri = Uri.parse(imageUriString);

                    // Open an input stream from the image URI
                    InputStream inputStream = context.getContentResolver().openInputStream(imageUri);

                    if (inputStream != null) {
                        // Convert the input stream to a byte array
                        byte[] byteArray = inputStreamToByteArray(inputStream);

                        // Insert the byte array into the database
                        dao.insert(new PhotoInfo(name, byteArray));

                        // Close the input stream
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private byte[] inputStreamToByteArray(InputStream inputStream) throws IOException {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }
                return outputStream.toByteArray();
            }
        };
    }


}
