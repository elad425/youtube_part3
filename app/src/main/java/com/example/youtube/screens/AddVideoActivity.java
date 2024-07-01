package com.example.youtube.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.youtube.MainActivity;
import com.example.youtube.R;
import com.example.youtube.entities.creator;
import com.example.youtube.entities.user;
import com.example.youtube.entities.video;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class AddVideoActivity extends AppCompatActivity {

    private static final int PICK_VIDEO_REQUEST = 1;
    private static final int PICK_THUMBNAIL_REQUEST = 2;

    private EditText videoNameEditText;
    private ImageView thumbnailImageView;
    private TextView thumbnailPlaceholder;
    private VideoView videoVideoView;
    private TextView videoPlaceholder;
    private Uri videoUri, thumbnailUri;
    private ArrayList<video> videos;
    private ArrayList<user> users;
    private user user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        setupWindow();
        initializeUI();
        setupBottomNavigation();
        handleBackButton();
    }

    private void setupWindow() {
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));
    }

    private void initializeUI() {
        videoNameEditText = findViewById(R.id.et_video_title);
        thumbnailImageView = findViewById(R.id.iv_thumbnail);
        videoVideoView = findViewById(R.id.vv_video);
        videoPlaceholder = findViewById(R.id.tv_add_video);
        thumbnailPlaceholder = findViewById(R.id.tv_add_thumbnail);

        videoVideoView.setOnClickListener(v -> selectVideo());
        thumbnailImageView.setOnClickListener(v -> selectThumbnail());

        Button addVideoButton = findViewById(R.id.btn_add_video);
        addVideoButton.setOnClickListener(v -> addVideo());

        videos = getIntent().getParcelableArrayListExtra("videos");
        users = getIntent().getParcelableArrayListExtra("users");
        user = getIntent().getParcelableExtra("user");
        if (videos == null) {
            videos = new ArrayList<>();
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_add_video);
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_profile) {
                navigateToProfilePage();
                return true;
            }
            else if (item.getItemId() == R.id.navigation_home) {
                navigateToMainActivity();
                return true;
            }
            return false;
        });
    }

    private void navigateToProfilePage() {
        Intent intent = new Intent(AddVideoActivity.this, ProfilePage.class);
        intent.putExtra("user", user);
        intent.putParcelableArrayListExtra("videos", videos);
        intent.putParcelableArrayListExtra("users", users);
        startActivity(intent);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(AddVideoActivity.this, MainActivity.class);
        intent.putParcelableArrayListExtra("video_list", videos);
        intent.putParcelableArrayListExtra("users", users);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void selectVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    private void selectThumbnail() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_THUMBNAIL_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedUri = data.getData();
            if (requestCode == PICK_VIDEO_REQUEST) {
                videoUri = selectedUri;
                playVideoSample();
            } else if (requestCode == PICK_THUMBNAIL_REQUEST) {
                thumbnailUri = selectedUri;
                displayThumbnailSample();
            }
        }
    }

    private void playVideoSample() {
        videoVideoView.setVideoURI(videoUri);
        videoPlaceholder.setText("");
        videoPlaceholder.setBackgroundColor(0x00FFFFFF);
        videoVideoView.start();
    }

    private void displayThumbnailSample() {
        thumbnailImageView.setImageURI(thumbnailUri);
        thumbnailPlaceholder.setText("");
        thumbnailPlaceholder.setBackgroundColor(0x00FFFFFF);
    }

    private void addVideo() {
        String videoName = videoNameEditText.getText().toString().trim();

        if (videoName.isEmpty() || thumbnailUri == null || videoUri == null) {
            Toast.makeText(this, "Please fill all fields and choose a video and a thumbnail", Toast.LENGTH_SHORT).show();
            return;
        }

        creator newCreator = new creator(user.getName(), "0", user.getProfile_pic());
        video newVideo = new video(videoName, newCreator, "today", videoUri.toString(), thumbnailUri.toString(), "0:12", "0", "0");

        videos.add(newVideo);
        Toast.makeText(this, "Video added successfully", Toast.LENGTH_SHORT).show();

        navigateToMainActivity();
        finish();
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
        Intent intent = new Intent(this, MainActivity.class);
        intent.putParcelableArrayListExtra("video_list", videos);
        intent.putParcelableArrayListExtra("users", users);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}
