package com.example.memecreatorproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.memecreatorproject.dao.Database;
import com.example.memecreatorproject.model.Images;

import java.io.File;
import java.util.List;

public class ViewMemeActivity extends AppCompatActivity {

    private static final String TAG = "ViewMemeActivity";
    private Context mContext;
    private int position = 0;
    private ImageView meme_photo;
    private File file;
    private String path;
    private String loadpath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meme);
        mContext = ViewMemeActivity.this;

        meme_photo = findViewById(R.id.meme_photo);

        Database database = Room.databaseBuilder(mContext, Database.class, "images.db").fallbackToDestructiveMigration().build();

        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Images> all_images = database.imageDAO().getAll();
                file = Environment.getExternalStorageDirectory();
                loadpath = String.valueOf(file);
                path = loadpath + "/" + all_images.get(position).getFilepath();
                Log.d(TAG, "run: file : " + file);
                Log.d(TAG, "run: load path : " + loadpath);
                Log.d(TAG, "run: path : " + path);
                Log.d(TAG, "run: position : " + position);

            }
        }).start();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Glide.with(mContext).load(path).into(meme_photo);
            }
        }, 500);
    }
}
