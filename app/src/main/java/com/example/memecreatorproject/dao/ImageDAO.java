package com.example.memecreatorproject.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.memecreatorproject.model.Images;

import java.util.List;

@Dao
public interface ImageDAO {

    @Query("SELECT * FROM images")
    List<Images> getAll();

    @Insert
    void insertAll(List<Images> images);

    @Insert
    void insert(Images images);

    @Delete
    void delete(Images images);

}
