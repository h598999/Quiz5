package com.example.quiz5.Gallery;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz5.Data.PhotoInfo;
import com.example.quiz5.Data.PhotoInfoViewModel;
import com.example.quiz5.R;
import com.example.quiz5.Upload.UploadActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class GalleryActivity extends AppCompatActivity {

   private Button backButton;
   private FloatingActionButton sortButton;
   private RecyclerView imageViews;
   private GalleryAdapter adapter;
   private PhotoInfoViewModel viewModel;
   private Button uploadActvity;
   private static List<PhotoInfo> allPhotos;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_gallery);

        backButton = findViewById(R.id.backGallery_Button);
        backButton.setOnClickListener( l -> {
            finish();
        });

        imageViews = findViewById(R.id.recyclerImageView_Gallery);
        imageViews.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(PhotoInfoViewModel.class);

        // Set an observer to the returned LiveData from the viewModel.getAllPictures()
        // the observer is notified anytime the LiveData changes
        viewModel.getAllPictures().observe(this, photoList -> {
            // Creates a new GalleryAdapter with the retrieved photoList
            adapter = new GalleryAdapter(getApplication(), photoList);
            // uses the setAdapter method to set the new adapter on the imageviews
            imageViews.setAdapter(adapter);
            // Sets the allPhotos field to be the retrieved photoList
            allPhotos = photoList;
        });

        uploadActvity = findViewById(R.id.new_Button);

        uploadActvity.setOnClickListener( v -> {
            Intent intent = new Intent(GalleryActivity.this, UploadActivity.class);
            startActivity(intent);
        });

    }

    public static List<PhotoInfo> getAllPhotos(){
        return allPhotos;
    }
}
