package com.example.quiz5.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

//Database access object
//Denotes that this is a Dao
@Dao
public interface PhotoInfoDao {

    //Insert is a built in method that will insert its parameter into the database
    // Sets the onConflict strategy to be ignore, that means it will return -1 for rows
    // that is not inserted because of a conflict
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(PhotoInfo photoInfo);

    //Delete will delete the parameter from the database
    @Delete
    void delete(PhotoInfo photoInfo);

    //This method will perform the given query towards the database
    //Will delete everything from the database
    @Query("DELETE FROM info_table")
    void deleteAll();

    //This method will perform the given query towards the database
    // Will return all the stored objects sorted alphabetically by name
    @Query("SELECT * FROM info_table ORDER BY name ASC")
    LiveData<List<PhotoInfo>> getAllEntries();

    //This method will perform the given query towards the database
    // Will return all the stored objects sorted alphabetically by name
    @Query("SELECT * FROM info_table ORDER BY name ASC")
    List<PhotoInfo> getAllEntriesSync();
}
