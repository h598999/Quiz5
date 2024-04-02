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
        viewModel.getAllPictures().observe(this, photoList -> {
            adapter = new GalleryAdapter(getApplication(), photoList);
            imageViews.setAdapter(adapter);
        });

        uploadActvity = findViewById(R.id.new_Button);
        uploadActvity.setOnClickListener( v -> {
            Intent intent = new Intent(GalleryActivity.this, UploadActivity.class);
            startActivity(intent);
        });

    }

    private void grantUriPermission(Uri uri) {
        Intent intent = new Intent();
        List<ResolveInfo> resInfoList = getApplicationContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            getApplicationContext().grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    }
}
