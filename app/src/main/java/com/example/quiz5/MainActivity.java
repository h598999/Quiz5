package com.example.quiz5;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.util.Rational;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.quiz5.Gallery.GalleryActivity;
import com.example.quiz5.Quiz.QuizActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button galleryButton = findViewById(R.id.MAIN_galleryButton);
        galleryButton.setOnClickListener(l -> {openGallery();});

        Button quizButton = findViewById(R.id.Quiz);
        quizButton.setOnClickListener(l -> {openQuiz();});

    }

    private void openGallery(){
        Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
        startActivity(intent);
    }

    private void openQuiz(){
        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        startActivity(intent);
    }

    /*
    private void requestRuntimePermission(){
        if (ContextCompat.checkSelfPermission(MainActivity.this, READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED){
            is_storage_image_permitted = true;
            Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
            startActivity(intent);
        } else {
            request_permission_launcher_storage_items.launch(READ_MEDIA_IMAGES);
        }
    }

    /*
    private ActivityResultLauncher<String> request_permission_launcher_storage_items =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                    if (isGranted){
                        Log.d("Media", READ_MEDIA_IMAGES + " Granted");
                    } else {
                        Log.d("Media", READ_MEDIA_IMAGES + " is not granted");
                        is_storage_image_permitted = false;
                    }
                    });

    /*
    private void requestRuntimePermission(){
        //Permission for sdk between 23 and 29
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && Build.VERSION.SDK_INT<Build.VERSION_CODES.R){
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
            }
            //Permission storage for sdk 30 or above
        } else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()){
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                    startActivityIfNeeded(intent, REQUEST_SETTINGS_INTENT);
                } catch(Exception e){
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    startActivityIfNeeded(intent, REQUEST_SETTINGS_INTENT);
                }
            }
        }

    }
    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission granted, you can read external storage", Toast.LENGTH_SHORT).show();
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("This app needs to read external storage to work, please allow Read external storage permission from" +
                        "settings to proceed");
                builder.setTitle("Permission required")
                        .setCancelable(false)
                        .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                builder.show();
            } else {
                requestRuntimePermission();
            }
        }
    }*/
}
