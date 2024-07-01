package com.example.youtube.screens;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youtube.MainActivity;
import com.example.youtube.R;
import com.example.youtube.entities.user;
import com.example.youtube.entities.video;
import com.example.youtube.utils.GeneralUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class ProfilePage extends AppCompatActivity {

    private user user;
    private ArrayList<video> videos;
    private ArrayList<user> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        setupWindow();
        initializeData();
        setupUI();
        setupBottomNavigation();
        handleBackButton();
    }

    private void setupWindow() {
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));
    }

    private void initializeData() {
        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");
        videos = intent.getParcelableArrayListExtra("videos");
        users = intent.getParcelableArrayListExtra("users");
        if (videos == null) {
            videos = new ArrayList<>();
        }
    }

    private void setupUI() {
        Button btnLogIn = findViewById(R.id.btn_logIn);
        TextView username = findViewById(R.id.username);
        TextView userEmail = findViewById(R.id.user_email);
        ShapeableImageView userPic = findViewById(R.id.user_pic);
        ImageButton btnSettings = findViewById(R.id.settings);
        btnSettings.setOnClickListener(this::displaySettings);

        if (user != null) {
            displayUserInfo(username, userEmail, userPic, btnLogIn);
        } else {
            displayGuestInfo(username, userEmail, userPic, btnLogIn);
        }
    }

    private void displayUserInfo(TextView username, TextView userEmail, ShapeableImageView userPic, Button btnLogIn) {
        username.setText(user.getName());
        userEmail.setText(user.getEmail());
        String profilePic = user.getProfile_pic();
        int profilePicId = getResources().getIdentifier(profilePic, "drawable", getPackageName());
        if (profilePicId != 0) {
            userPic.setImageResource(profilePicId);
        } else {
            userPic.setImageURI(Uri.parse(profilePic));
        }

        btnLogIn.setText(R.string.logOut);
        btnLogIn.setOnClickListener(v -> onConfirmClick());
    }

    private void displayGuestInfo(TextView username, TextView userEmail, ShapeableImageView userPic, Button btnLogIn) {
        username.setText(getString(R.string.guest));
        userEmail.setText(R.string.guest);
        userPic.setImageResource(R.drawable.ic_account);

        btnLogIn.setText(R.string.login);
        btnLogIn.setOnClickListener(v -> {
            goToLogIn();
        });
    }

    private void displaySettings(View v){
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.getMenuInflater().inflate(R.menu.settings_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.about) {
                showAboutDialog();
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_about, null);
        builder.setView(dialogLayout);
        builder.show();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_profile);
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                navigateToHome();
                return true;
            } else if (item.getItemId() == R.id.navigation_add_video) {
                navigateToAddVideo();
                return true;
            }
            return false;
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(ProfilePage.this, MainActivity.class);
        intent.putExtra("video_list", videos);
        intent.putParcelableArrayListExtra("users", users);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void navigateToAddVideo() {
        if (user != null) {
            Intent intent = new Intent(ProfilePage.this, AddVideoActivity.class);
            intent.putExtra("videos", videos);
            intent.putParcelableArrayListExtra("users", users);
            intent.putExtra("user", user);
            startActivity(intent);
        } else {
            Toast.makeText(ProfilePage.this, "Please log in to add a video", Toast.LENGTH_SHORT).show();
            goToLogIn();
        }
    }

    public void onConfirmClick() {
        GeneralUtils.updateUsers(users, user);
        user = null;
        Toast.makeText(this, "You logged out", Toast.LENGTH_SHORT).show();
        navigateToHome();
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

    private void goToLogIn(){
        Intent intent = new Intent(this, LogIn.class);
        intent.putParcelableArrayListExtra("video_list", videos);
        intent.putParcelableArrayListExtra("users", users);
        startActivity(intent);
    }
}
