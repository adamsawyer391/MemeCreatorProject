package com.example.memecreatorproject.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.memecreatorproject.CreateMemeActivity;
import com.example.memecreatorproject.GridViewAdapter;
import com.example.memecreatorproject.R;

public class DefaultFragment extends Fragment {

    private static final String TAG = "DefaultFragment";

    public static DefaultFragment getInstance(){
        return new DefaultFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_default, container, false);

        Context context = container.getContext();

        int orientation = context.getResources().getConfiguration().orientation;
        Log.d(TAG, "onCreateView: orientation value : " + orientation);

        GridView gridView = rootView.findViewById(R.id.gridView);
        gridView.setAdapter(new GridViewAdapter(context, orientation));
        if (orientation == 1){
            gridView.setNumColumns(3);
        }else{
            gridView.setNumColumns(5);
        }
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(context, CreateMemeActivity.class);
            intent.putExtra("id", position);
            startActivity(intent);
        });
        return rootView;
    }
}
