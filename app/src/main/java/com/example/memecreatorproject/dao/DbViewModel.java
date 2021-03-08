package com.example.memecreatorproject.dao;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.example.memecreatorproject.model.Images;

import java.util.List;

public class DbViewModel extends AndroidViewModel {

    private Database database;
    private LiveData<List<Images>> all_images;

    public DbViewModel(@NonNull Application application) {
        super(application);
        database = Room.databaseBuilder(application, Database.class, "images.db").fallbackToDestructiveMigration().build();
        //all_images = database.imageDAO().getAll();
    }

    public LiveData<List<Images>> getAllImages(){
        return all_images;
    }
}
