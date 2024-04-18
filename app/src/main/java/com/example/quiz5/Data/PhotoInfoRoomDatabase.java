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

// Annotates that this is a database
// entities decides what object is to be stored in the database
// version describes what version of the database it is
// exportschema = false, tells the app not to export a exported version files of the database
@Database(entities = {PhotoInfo.class}, version = 1, exportSchema = false)
public abstract class PhotoInfoRoomDatabase extends RoomDatabase {

    // Declares an abstract instance of the PhotoInfoDao interface
    public abstract PhotoInfoDao dao();

    //Declares a private PhotoinfoRoomDatabase field called INSTANCE
    private static PhotoInfoRoomDatabase INSTANCE;

    //Declares a static final int NUMBER_OF_THREADS
    private static final int NUMBER_OF_THREADS = 4;
    //Declares a new ExecutorService called databaseWriteExecutor Using the Executors.newFixedThreadPool() with the
    // NUMBER_OF_THREADS as the parameter
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Returns an INSTANCE of the PhotoInfoRoomDatabase, if there is no existing INSTANCE it will create a new Instance
    // and return that. If there is an existing INSTANCE it will return the existing INSTANCE.
    static PhotoInfoRoomDatabase getDatabase(final Context context){
        // Checks if an INSTANCE of the database exists
        if (INSTANCE == null){
            //If not
            synchronized (PhotoInfoRoomDatabase.class){
                if (INSTANCE == null){
                    // Build a new database using the Room.databaseBuilder()
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PhotoInfoRoomDatabase.class, "photoinfo_database")
                            // Add the sRoomDatabaseCallback to the database
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        //return the INSTANCE
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback;

    // Sets the sRoomDatabaseCallback
    public static void setRoomDatabaseCallback(Context context) {
        //Creates a callback that adds three pictures to the database when it is created
        sRoomDatabaseCallback = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                // Uses databaseWriteExecutor to perform asyncrhonusly
                databaseWriteExecutor.execute(() -> {
                    PhotoInfoDao dao = INSTANCE.dao();
                    // Clears the database
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
