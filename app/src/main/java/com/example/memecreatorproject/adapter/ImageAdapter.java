package com.example.memecreatorproject.adapter;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.memecreatorproject.R;
import com.example.memecreatorproject.model.Images;

import java.io.File;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private static final String TAG = "ImageAdapter";

    private final List<Images> all_images;
    private final Context context;

    public ImageAdapter(List<Images> all_images, Context context) {
        this.all_images = all_images;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_file_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        File file = Environment.getExternalStorageDirectory();
        Log.d(TAG, "onBindViewHolder: load path : " + file);
        String loadpath = String.valueOf(file);
        Glide.with(context.getApplicationContext()).load(loadpath +"/" + all_images.get(position).getFilepath()).into(holder.circleImageView);
        holder.filePath.setText(all_images.get(position).getFilepath());
    }

    @Override
    public int getItemCount() {
        return all_images.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView circleImageView;
        public TextView filePath;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.file_photo);
            filePath = itemView.findViewById(R.id.file_path);
        }

    }
}
