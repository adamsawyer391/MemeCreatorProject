package com.example.memecreatorproject.fragments;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.memecreatorproject.R;
import com.example.memecreatorproject.ViewMemeActivity;
import com.example.memecreatorproject.adapter.ImageAdapter;
import com.example.memecreatorproject.dao.Database;
import com.example.memecreatorproject.dao.DbViewModel;
import com.example.memecreatorproject.dao.ImageDAO;
import com.example.memecreatorproject.helper.RecycleItemClickListener;
import com.example.memecreatorproject.model.Images;

import java.util.List;

public class MyMemesFragment extends Fragment {

    private static final String TAG = "MyMemesFragment";

    private RecyclerView recyclerView;
    private Context mContext;

    public static MyMemesFragment getInstance(){
        return new MyMemesFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_memes, container, false);
        mContext = container.getContext();
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);

        Database database = Room.databaseBuilder(mContext, Database.class, "images.db").fallbackToDestructiveMigration().build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Images> all_images = database.imageDAO().getAll();
                Log.d(TAG, "run: all images : " + all_images);
                ImageAdapter imageAdapter = new ImageAdapter(all_images, mContext);
                recyclerView.setAdapter(imageAdapter);
//                List<Images> all_images = database.imageDAO().getAll();
//                Log.d(TAG, "run: all images : " + all_images);
//                ImageAdapter imageAdapter = new ImageAdapter(all_images, mContext);
//                recyclerView.setAdapter(imageAdapter);
            }
        }).start();

        recyclerTouchHelper();

        return rootView;
    }

    private void recyclerTouchHelper(){
        recyclerView.addOnItemTouchListener(new RecycleItemClickListener(mContext, new RecycleItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, ViewMemeActivity.class);
                intent.putExtra("position", position);
                mContext.startActivity(intent);
                Animatoo.animateSlideLeft(mContext);
            }
        }));
    }

}
