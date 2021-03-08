package com.example.memecreatorproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.memecreatorproject.R;

public class ArrayAdapterHelper extends ArrayAdapter<String> {

    private static final String TAG = "ArrayAdapterHelper";

    private final Context mContext;
    int layoutResourceID;
    String[] list_text;
    String[] font_selection;

    public ArrayAdapterHelper(Context context, int resource, String[] list_text, String[] font_list) {
        super(context, resource, list_text);
        this.mContext = context;
        this.layoutResourceID = resource;
        this.list_text = list_text;
        this.font_selection = font_list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        MyItemHolder holder = null;
        if (row == null){
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceID, parent, false);
            holder = new MyItemHolder();
            holder.spinnerItem = row.findViewById(R.id.standard_spinner_format);
            row.setTag(holder);
        }else{
            holder = (MyItemHolder)row.getTag();
        }
        String text = list_text[position];
        holder.spinnerItem.setText(text);
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), font_selection[position] + ".ttf");
        Log.d(TAG, "getView: type face : " + typeface);
        holder.spinnerItem.setTypeface(typeface);

        return row;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    static class MyItemHolder{
        TextView spinnerItem;
    }

}
