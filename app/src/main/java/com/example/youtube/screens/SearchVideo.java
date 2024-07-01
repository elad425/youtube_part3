package com.example.youtube.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.MainActivity;
import com.example.youtube.R;
import com.example.youtube.adapters.SearchAdapter;
import com.example.youtube.entities.user;
import com.example.youtube.entities.video;
import com.example.youtube.utils.JsonUtils;

import java.util.ArrayList;

public class SearchVideo extends AppCompatActivity {

    private ArrayList<video> filteredList;
    private SearchAdapter searchAdapter;
    private ArrayList<video> videos;
    private ArrayList<user> users;
    private user user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_video);

        setupWindow();
        initializeData();
        setupUI();
        setupSearchView();
    }

    private void setupWindow() {
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));
    }

    private void initializeData() {
        Intent intent = getIntent();
        videos = intent.getParcelableArrayListExtra("video_list");
        users = intent.getParcelableArrayListExtra("users");
        if (videos == null) {
            videos = JsonUtils.loadVideosFromJson(this);
        }
        filteredList = new ArrayList<>();
    }

    private void setupUI() {
        user = getIntent().getParcelableExtra("user");
        setupRecyclerView(user);
        setupBackButton();
        setupBackPressedDispatcher();
    }

    private void setupRecyclerView(user user) {
        searchAdapter = new SearchAdapter(videos, filteredList, this, user, users);
        RecyclerView rvSearch = findViewById(R.id.rv_search);
        rvSearch.setLayoutManager(new LinearLayoutManager(this));
        rvSearch.setAdapter(searchAdapter);
    }

    private void setupBackButton() {
        ImageButton btnBack = findViewById(R.id.search_back);
        btnBack.setOnClickListener(v -> handleBackAction());
    }

    private void setupBackPressedDispatcher() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackAction();
            }
        });
    }

    private void setupSearchView() {
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterVideos(newText);
                return true;
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

    private void filterVideos(String query) {
        filteredList.clear();
        if (!query.isEmpty()) {
            for (video video : videos) {
                if (video.getVideo_name().toLowerCase().startsWith(query.toLowerCase())) {
                    filteredList.add(video);
                }
            }
        }
        searchAdapter.setFilteredList(filteredList);
    }
}
