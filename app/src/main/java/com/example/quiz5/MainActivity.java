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

        //Find the button that directs to gallery activity
        Button galleryButton = findViewById(R.id.MAIN_galleryButton);
        // setOnClickListener on the button and bind the openGallery() method
        galleryButton.setOnClickListener(l -> {openGallery();});

        //Find the button that directs to the quiz activity
        Button quizButton = findViewById(R.id.Quiz);

        //setOnClickListener on the button and bind the openQuiz() method
        quizButton.setOnClickListener(l -> {openQuiz();});

    }

    //Method for starting the galleryActivity
    private void openGallery(){
        // Creates a new intent for opening the gallery activity
        Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
        // Starts the galleryActivity by using the startActivity method and using the previously created intent
        startActivity(intent);
    }

    //Method for opening the Quiz activity
    private void openQuiz(){
        // Creates a new intent for opening the quiz activity
        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        // Starts the quizActiviy by using the startActivity method and using the previously created intent
        startActivity(intent);
    }
}
