package com.example.quiz5.Data;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.quiz5.Data.PhotoInfo;
import com.example.quiz5.Data.PhotoInfoRepository;

import java.util.List;

public class PhotoInfoViewModel extends AndroidViewModel {

    private PhotoInfoRepository repository;
    private LiveData<List<PhotoInfo>> allPictures;

    public PhotoInfoViewModel(@NonNull Application application) {
        super(application);
        repository = new PhotoInfoRepository(application);
        allPictures = repository.getAllPictures();
    }

    public LiveData<List<PhotoInfo>> getAllPictures() {
        return allPictures;
    }

    public void insert(PhotoInfo photoInfo) {
        repository.insert(photoInfo);
    }

    public void delete(PhotoInfo photoInfo) {
        repository.delete(photoInfo);
    }
}

