package com.example.memecreatorproject;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridViewAdapter extends BaseAdapter {

    private final Context mContext;
    private final int orientation;
    public int[] memeImagesArray = {
            R.drawable.meme1,
            R.drawable.meme2,
            R.drawable.meme3,
            R.drawable.meme4,
            R.drawable.meme5,
            R.drawable.meme6,
            R.drawable.meme7,
            R.drawable.meme8,
            R.drawable.meme9,
            R.drawable.meme10,
            R.drawable.meme11,
            R.drawable.meme12,
            R.drawable.meme13,
            R.drawable.meme14,
            R.drawable.meme15,
            R.drawable.meme16,
            R.drawable.meme17
    };

    public GridViewAdapter(Context mContext, int orientation) {
        this.mContext = mContext;
        this.orientation = orientation;
    }

    @Override
    public int getCount() {
        return memeImagesArray.length;
    }

    @Override
    public Object getItem(int position) {
        return memeImagesArray[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(memeImagesArray[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        //orientation is portrait
        if (orientation == 1){
            imageView.setLayoutParams(new ViewGroup.LayoutParams(width/3, height/5));
        }
        //orientation is in landscape
        else{
            imageView.setLayoutParams(new ViewGroup.LayoutParams(width/3, height/5));
        }
        return imageView;
    }
}
