package com.example.memecreatorproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.memecreatorproject.fragments.DefaultFragment;
import com.example.memecreatorproject.fragments.MyMemesFragment;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        RelativeLayout gallery = findViewById(R.id.galleryButton);
        RelativeLayout camera = findViewById(R.id.cameraButton);

        tabLayout = findViewById(R.id.bar_layout);
        viewPager = findViewById(R.id.view_pager);
        MemePagerAdapter memePagerAdapter = new MemePagerAdapter(getSupportFragmentManager(), 0);
        memePagerAdapter.addFragment(DefaultFragment.getInstance(), "SELECT");
        memePagerAdapter.addFragment(MyMemesFragment.getInstance(), "MY MEMES");

        MyMemesFragment.getInstance().onAttach(context);

        viewPager.setAdapter(memePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        camera.setOnClickListener(v -> {
            //check permissions
            Dexter.withContext(context)
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            Intent intent = new Intent(context, CreateMemeActivity.class);
                            intent.putExtra("id", "take photo");
                            startActivity(intent);
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                            Toast.makeText(context, "You cannot take a photo", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();


        });
        gallery.setOnClickListener(v -> {
            Intent intent = new Intent(context, CreateMemeActivity.class);
            intent.putExtra("id", "get image");
            startActivity(intent);
        });
    }

}