package com.example.quiz5.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PhotoInfoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(PhotoInfo photoInfo);

    @Delete
    void delete(PhotoInfo photoInfo);

    @Query("DELETE FROM info_table")
    void deleteAll();
    @Query("SELECT * FROM info_table ORDER BY name ASC")
    LiveData<List<PhotoInfo>> getAllEntries();

    @Query("SELECT * FROM info_table ORDER BY name ASC")
    List<PhotoInfo> getAllEntriesSync();
}
