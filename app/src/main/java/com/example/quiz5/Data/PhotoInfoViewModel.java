package com.example.quiz5.Data;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.quiz5.Data.PhotoInfo;
import com.example.quiz5.Data.PhotoInfoRepository;

import java.util.List;

public class PhotoInfoViewModel extends AndroidViewModel {

    // Declares private fields
    private PhotoInfoRepository repository;
    private LiveData<List<PhotoInfo>> allPictures;

    //Constructor, since this class extends the AndroidViewModel it has to have
    // a constructor that takes in an Application as a parameter
    public PhotoInfoViewModel(@NonNull Application application) {
        super(application);
        //Creates a new PhotoInfoRepository
        repository = new PhotoInfoRepository(application);
        //Sets the allPictures to be the result of the getAllPictures() method in the repository
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

