package com.example.youtube;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.youtube.Daos.userDao;
import com.example.youtube.Daos.videoDao;
import com.example.youtube.entities.user;
import com.example.youtube.entities.video;
import com.example.youtube.screens.AddVideoActivity;
import com.example.youtube.screens.LogIn;
import com.example.youtube.screens.ProfilePage;
import com.example.youtube.screens.SearchVideo;
import com.example.youtube.utils.JsonUtils;
import com.example.youtube.utils.GeneralUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private int userId;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWindow();

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "userDb").allowMainThreadQueries().build();

        if (checkPermissions()) {
            lunchApp();
        } else {
            requestPermissions();
        }
    }

    private void initializeData() {
        userDao userDao = db.userDao();
        if (userDao.getAllUsers().isEmpty()) {
            ArrayList<user> tempUser = JsonUtils.loadUsersFromJson(this);
            for (user u : tempUser) {
                userDao.insert(u);
            }
        }

        videoDao videoDao = db.videoDao();
        if (videoDao.getAllVideos().isEmpty()) {
            ArrayList<video> tempVideo = JsonUtils.loadVideosFromJson(this);
            for (video v : tempVideo) {
                videoDao.insert(v);
            }
        }
    }

    private void setupWindow() {
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));
    }

    private void initialize() {
        Intent intent = getIntent();
        userId = intent.getIntExtra("user",-1);
    }

    private void setupUI() {
        RecyclerView lstVideos = findViewById(R.id.lstVideos);
        GeneralUtils.displayVideoList(this, lstVideos, userId,null, db);

        ImageButton btnSearch = findViewById(R.id.search_button);
        btnSearch.setOnClickListener(v -> navigateToSearch());

        ImageButton btnCast = findViewById(R.id.cast_button);
        btnCast.setOnClickListener(v -> Toast.makeText(MainActivity.this,
                "The app doesn't support Chromecast yet", Toast.LENGTH_SHORT).show());
    }

    private void navigateToSearch() {
        Intent intent = new Intent(this, SearchVideo.class);
        intent.putExtra("user", userId);
        startActivity(intent);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_home);
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_profile) {
                navigateToProfile();
                return true;
            }
            else if (item.getItemId() == R.id.navigation_add_video) {
                navigateToAddVideo();
                return true;
            }
            return false;
        });
    }

    private void navigateToProfile() {
        Intent intent = new Intent(MainActivity.this, ProfilePage.class);
        intent.putExtra("user", userId);
        startActivity(intent);
    }

    private void navigateToAddVideo() {
        if (userId != -1) {
            Intent intent = new Intent(MainActivity.this, AddVideoActivity.class);
            intent.putExtra("user", userId);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Please log in to add a video", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LogIn.class);
            startActivity(intent);
        }
    }

    private boolean checkPermissions() {
        int resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (Build.VERSION.SDK_INT >= 33) {
            int resultVideo = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO);
            int resultImages = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES);
            return resultVideo == PackageManager.PERMISSION_GRANTED &&
                    resultImages == PackageManager.PERMISSION_GRANTED &&
                    resultCamera == PackageManager.PERMISSION_GRANTED;
        } else{
            int resultStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            return resultStorage == PackageManager.PERMISSION_GRANTED &&
                    resultCamera == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.CAMERA
            }, 1);
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && Build.VERSION.SDK_INT >= 33) {
            boolean videoPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean imagesPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            boolean cameraPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
            if (videoPermission && imagesPermission && cameraPermission) {
                lunchApp();
            } else {
                Toast.makeText(MainActivity.this, "this app need permissions, please go to setting and grant them", Toast.LENGTH_SHORT).show();
            }
        } else if (grantResults.length > 0) {
            boolean storagePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            if (storagePermission && cameraPermission) {
                lunchApp();
            } else {
                Toast.makeText(MainActivity.this, "this app need permissions, please go to setting and grant them", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void lunchApp(){
        setContentView(R.layout.activity_main);
        initializeData();
        initialize();
        setupUI();
        setupBottomNavigation();
        handleBackButton();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearUserDetails();
    }

    private void clearUserDetails() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void handleBackButton() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackAction();
            }
        });
    }

    private void handleBackAction() {

    }
}
