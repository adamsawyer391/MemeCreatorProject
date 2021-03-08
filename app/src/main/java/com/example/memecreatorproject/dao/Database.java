package com.example.memecreatorproject.dao;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.memecreatorproject.model.Images;

@androidx.room.Database(entities = {Images.class}, version = 1)
public abstract class Database extends RoomDatabase {

    private static Database instance;
    public abstract ImageDAO imageDAO();

    public static synchronized Database getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context, Database.class, "images.db").fallbackToDestructiveMigration().addCallback(roomCallback).build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{

        private ImageDAO imageDAO;

        private PopulateDbAsyncTask(Database database){
            imageDAO = database.imageDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            imageDAO.insert(new Images());
            return null;
        }
    }

}
