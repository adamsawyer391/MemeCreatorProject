package com.example.memecreatorproject.model;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "images")
public class Images {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "path")
    public String filepath;

    @ColumnInfo(name = "name")
    public String filename;

    @ColumnInfo(name = "uri")
    public String uri;

    public Images() {

    }

    public Images(int uid, String filepath, String filename, String uri) {
        this.uid = uid;
        this.filepath = filepath;
        this.filename = filename;
        this.uri = uri;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
