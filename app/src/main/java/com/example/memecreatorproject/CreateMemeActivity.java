package com.example.memecreatorproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.memecreatorproject.adapter.ArrayAdapterHelper;
import com.example.memecreatorproject.adapter.ImageAdapter;
import com.example.memecreatorproject.dao.Database;
import com.example.memecreatorproject.model.Images;
import com.muddzdev.quickshot.QuickShot;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateMemeActivity extends AppCompatActivity {

    private static final String TAG = "CreateMemeActivity";

    private static final int GALLERY = 1;
    private static final int CAMERA = 2;

    private static final String DATABASE_NAME = "images.db";
    private Database database;

    private Context context;
    private ImageView main_image;
    private EditText firstLine, secondLine;
    private TextView black, white, red, blue, green;
    private TextView textOne, textTwo;
    private Spinner spinner;
    private SeekBar seekBar;
    int textSizeValue;
    int textColor;
    private String currentPhotoPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meme);
        context = CreateMemeActivity.this;

        database = Room.databaseBuilder(getApplicationContext(), Database.class, DATABASE_NAME).fallbackToDestructiveMigration().build();

        initUI();
        getIntentExtras();
        getTextDisplay();
        changeTextSize();
        changeTextColor();
        setSpinnerObject();
    }

    private void getIntentExtras(){
        Intent intent = getIntent();
        String extraValue = intent.getExtras().get("id").toString();
        Log.d(TAG, "getIntentExtras: extra value : " + extraValue);
        if (extraValue.equals("take photo")){
            takePhoto();
        }else if (extraValue.equals("get image")){
            openGallery();
        }else{
            int position = intent.getExtras().getInt("id");
            GridViewAdapter gridViewAdapter = new GridViewAdapter(context, 1);
            main_image.setScaleType(ImageView.ScaleType.FIT_XY);
            main_image.setAdjustViewBounds(true);
            main_image.setImageResource(gridViewAdapter.memeImagesArray[position]);
        }
    }

    private void initUI(){
        main_image = findViewById(R.id.main_image);
        ImageView close = findViewById(R.id.close);
        ImageView save = findViewById(R.id.save);
        black = findViewById(R.id.colorBlack);
        white = findViewById(R.id.colorWhite);
        red = findViewById(R.id.colorRed);
        blue = findViewById(R.id.colorBlue);
        green = findViewById(R.id.colorGreen);
        firstLine = findViewById(R.id.etTop);
        secondLine = findViewById(R.id.etBottom);
        textOne = findViewById(R.id.textOne);
        textTwo = findViewById(R.id.textTwo);
        spinner = findViewById(R.id.spinner);
        seekBar = findViewById(R.id.seekBarTextSize);
        textOne.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                return false;
            }
        });

        close.setOnClickListener(v -> {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            finish();
        });
        save.setOnClickListener(v -> {
            //saveMemePhoto();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                createDirectory();
            }else{
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }

        });
    }

    private void getTextDisplay(){
        firstLine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mText = s.toString();
                textOne.setTextSize(24);
                textOne.setTextColor(Color.WHITE);
                textOne.setGravity(Gravity.CENTER);
                textOne.setText(mText);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        secondLine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mText = s.toString();
                textTwo.setTextSize(24);
                textTwo.setTextColor(Color.WHITE);
                textTwo.setGravity(Gravity.CENTER);
                textTwo.setText(mText);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void changeTextSize(){
        seekBar.setProgress(24);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            seekBar.setMin(12);
        }
        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textSizeValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (firstLine.hasFocus()){
                    textOne.setTextSize(textSizeValue);
                }
                if (secondLine.hasFocus()){
                    textTwo.setTextSize(textSizeValue);
                }
            }
        });
    }

    private void changeTextColor(){
        black.setOnClickListener(v -> {
            textColor = ContextCompat.getColor(context, R.color.black);
            if (firstLine.hasFocus()){
                textOne.setTextColor(textColor);
            }
            if (secondLine.hasFocus()){
                textTwo.setTextColor(textColor);
            }
        });
        white.setOnClickListener(v -> {
            textColor = ContextCompat.getColor(context, R.color.white);
            if (firstLine.hasFocus()){
                textOne.setTextColor(textColor);
            }
            if (secondLine.hasFocus()){
                textTwo.setTextColor(textColor);
            }
        });
        red.setOnClickListener(v -> {
            textColor = ContextCompat.getColor(context, R.color.red);
            if (firstLine.hasFocus()){
                textOne.setTextColor(textColor);
            }
            if (secondLine.hasFocus()){
                textTwo.setTextColor(textColor);
            }
        });
        blue.setOnClickListener(v -> {
            textColor = ContextCompat.getColor(context, R.color.blue);
            if (firstLine.hasFocus()){
                textOne.setTextColor(textColor);
            }
            if (secondLine.hasFocus()){
                textTwo.setTextColor(textColor);
            }
        });
        green.setOnClickListener(v -> {
            textColor = ContextCompat.getColor(context, R.color.green);
            if (firstLine.hasFocus()){
                textOne.setTextColor(textColor);
            }
            if (secondLine.hasFocus()){
                textTwo.setTextColor(textColor);
            }
        });
    }

    private void setSpinnerObject(){
        String[] fonts = getResources().getStringArray(R.array.fonts);
        String[] font_list = getResources().getStringArray(R.array.fonts_adapter);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.layout_spinner_item, fonts);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapterHelper(context, R.layout.layout_spinner_item, fonts, font_list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                switch (item){
                    case "Baloo":
                        displayFont(R.font.baloo);
                        break;
                    case "Creepster":
                        displayFont(R.font.creepster);
                        break;
                    case "Faster One":
                        displayFont(R.font.faster_one);
                        break;
                    case "Holtwood":
                        displayFont(R.font.holtwood_one_sc);
                        break;
                    case "Nosifer":
                        displayFont(R.font.nosifer);
                        break;
                    case "Permanent Marker":
                        displayFont(R.font.permanent_marker);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void displayFont(int font){
        Typeface typeface = ResourcesCompat.getFont(context, font);
        textOne.setTypeface(typeface);
        textTwo.setTypeface(typeface);
    }

    private void openGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void takePhoto(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null){
            File photoFile;
            photoFile = createImageFile();
            if (photoFile != null){
                currentPhotoPath = photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(this, context.getPackageName() + ".fileprovider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, CAMERA);
            }
        }
    }

    private File createImageFile(){
        String name = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try{
            image = File.createTempFile(name, ".jpg", storageDirectory);
        }catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == GALLERY){
                Uri selectedImage = null;
                if (data != null) {
                    selectedImage = data.getData();
                }
                main_image.setScaleType(ImageView.ScaleType.FIT_XY);
                main_image.setAdjustViewBounds(true);
                main_image.setImageURI(selectedImage);
            }else if (requestCode == CAMERA){
                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                try{
                    ExifInterface exifInterface = new ExifInterface(currentPhotoPath);
                    int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                    Log.d(TAG, "onActivityResult: orientation : " + orientation);
                    Bitmap rotatedBitmap;
                    switch (orientation){
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatedBitmap = rotateImage(bitmap, 90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatedBitmap = rotateImage(bitmap, 180);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatedBitmap = rotateImage(bitmap, 270);
                            break;
                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            rotatedBitmap = bitmap;
                    }
                    main_image.setScaleType(ImageView.ScaleType.FIT_XY);
                    main_image.setAdjustViewBounds(true);
                    main_image.setImageBitmap(rotatedBitmap);
                }catch (IOException e){
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void createDirectory(){
        //stores to app specific directory
//        String album_name = "GoofyPics";
//        File file = new File(getFilesDir(), album_name);
//        if (file.mkdirs()){
//            Log.d(TAG, "createDirectory: file was created : " + file.getAbsolutePath());
//        }else{
//            Log.d(TAG, "createDirectory: file was not created");
//        }
        long name = System.currentTimeMillis();
        String filename = String.valueOf(name);
        View root_view = findViewById(R.id.image_layout);
        QuickShot.of(root_view).setResultListener(new QuickShot.QuickShotListener() {
            @Override
            public void onQuickShotSuccess(String path) {
                Log.d(TAG, "onQuickShotSuccess: path : " + path);
                File file = new File(path);
                Uri uri = Uri.fromFile(file);
                Log.d(TAG, "onQuickShotSuccess: uri : " + uri);
                String message = "Meme Saved!";
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(context, message, duration).show();
                String url = String.valueOf(uri);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Images image = new Images();
                        image.setFilename(filename);
                        image.setFilepath(path);
                        image.setUri(url);
                        //List<Images> all_images = database.imageDAO().getAll();
                        List<Images> all_images = database.imageDAO().getAll();
//                        int count = all_images.size();
                        int count = all_images.size();
                        Log.d(TAG, "run: count : " + count);
                        count+=1;
                        Log.d(TAG, "run: count : " + count);
                        image.setUid(count);
                        database.imageDAO().insert(image);
                    }
                }).start();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateAdapter();
                    }
                }, 500);

            }

            @Override
            public void onQuickShotFailed(String path, String errorMsg) {

            }
        }).enableLogging().setFilename(filename).setPath("GoofyPics").toJPG().save();
    }

    private void updateAdapter(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Images> all_images = database.imageDAO().getAll();
                ImageAdapter imageAdapter = new ImageAdapter(all_images, context);
                imageAdapter.notifyDataSetChanged();
            }
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0){
            createDirectory();
        }
    }

}
