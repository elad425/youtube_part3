package com.example.youtube.screens;

import static com.example.youtube.utils.GeneralUtils.findVideoPlace;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.youtube.AppDatabase;
import com.example.youtube.MainActivity;
import com.example.youtube.R;
import com.example.youtube.adapters.CommentsAdapter;
import com.example.youtube.entities.comment;
import com.example.youtube.entities.user;
import com.example.youtube.entities.video;
import com.example.youtube.utils.GeneralUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class VideoPlayerActivity extends AppCompatActivity {

    private static final int PICK_VIDEO_THUMBNAIL = 100;
    private static final int PICK_VIDEO_FILE = 101;
    private boolean isLiked = false;
    private boolean isDisliked = false;
    private boolean isSubscribe = false;
    private boolean areCommentsVisible = false;
    private RecyclerView rvComments;
    private TextView tvComments;
    private ImageButton ivToggleComments;
    private FloatingActionButton fabAddComment;
    private CommentsAdapter commentsAdapter;
    private ArrayList<comment> commentList;
    private ArrayList<video> videos;
    private ArrayList<user> users;
    private int videoPosition;
    private int userId;
    private int videoId;
    private Uri newThumbnailUri;
    private Uri newVideoUri;
    private EditText inputVideoName;
    private AppDatabase db;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "userDb").allowMainThreadQueries().build();

        initializeUI();
        setupVideoView();
        handleCommentsSection();
        handleActionButtons();
        handleBackButton();
    }

    private void initializeUI() {
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));

        RecyclerView lstVideos = findViewById(R.id.lstVideos);
        Intent intent = getIntent();
        videoId = intent.getIntExtra("video_item",-1);
        userId = intent.getIntExtra("user",-1);
        users = new ArrayList<user>(db.userDao().getAllUsers());
        videos = new ArrayList<video>(db.videoDao().getAllVideos());

        int videoViews = Integer.parseInt(videos.get(videoId).getViews()) + 1;
        videos.get(videoId).setViews(Integer.toString(videoViews));
        db.videoDao().update(videos.get(videoId));

        if (userId != -1) {
            isLiked = users.get(userId).isLiked(videos.get(videoId));
            isDisliked = users.get(userId).isDisLiked(videos.get(videoId));
            isSubscribe = users.get(userId).isSubs(users.get(videos.get(videoId).getCreator()));
        }

        if (videos.get(videoId) != null) {
            videoPosition = findVideoPlace(videos, videos.get(videoId));
            GeneralUtils.displayVideoList(this, lstVideos, videos, userId, videos.get(videoId), users);
        }
    }

    private void setupVideoView() {
        if (videos.get(videoId) == null) return;

        final VideoView videoView = findViewById(R.id.tv_video_view);
        videoView.setVideoURI(Uri.parse(videos.get(videoId).getVideo_path()));
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();

        displayVideoInfo();
    }

    private void displayVideoInfo() {
        TextView tvVideoName = findViewById(R.id.tv_video_name);
        TextView tvVideoViews = findViewById(R.id.tv_video_views);
        TextView tvCreator = findViewById(R.id.tv_creator);
        TextView tvPublishDate = findViewById(R.id.tv_publish_date);
        TextView creatorSubCount = findViewById(R.id.tv_creator_subs);
        ShapeableImageView creatorPic = findViewById(R.id.creator_pic);

        String formatViews = GeneralUtils.getViews(videos.get(videoId).getViews()) + " views";
        tvVideoName.setText(videos.get(videoId).getVideo_name());
        tvVideoViews.setText(formatViews);
        tvCreator.setText(users.get(videos.get(videoId).getCreator()).getName());
        tvPublishDate.setText(GeneralUtils.timeAgo(videos.get(videoId).getDate_of_release()));
        creatorSubCount.setText(GeneralUtils.getViews(users.get(videos.get(videoId).getCreator()).getSubs_count()));

        String creatorPicPath = users.get(videos.get(videoId).getCreator()).getProfile_pic();
        int creatorPicId = getResources().getIdentifier(creatorPicPath, "drawable", getPackageName());
        if (creatorPicId != 0) {
            creatorPic.setImageResource(creatorPicId);
        } else {
            creatorPic.setImageURI(Uri.parse(creatorPicPath));
        }
    }

    @SuppressLint("DefaultLocale")
    private void handleCommentsSection() {
        if (videos.get(videoId) == null) return;

        tvComments = findViewById(R.id.tv_comments);
        rvComments = findViewById(R.id.rv_comments);
        ivToggleComments = findViewById(R.id.iv_toggle_comments);
        fabAddComment = findViewById(R.id.fab_add_comment);

        commentList = videos.get(videoId).getComments();
        tvComments.setText(String.format("Comments (%d)", commentList.size()));
        ivToggleComments.setOnClickListener(v -> toggleComments());
        tvComments.setOnClickListener(v -> toggleComments());

        rvComments.setLayoutManager(new LinearLayoutManager(this));
        commentsAdapter = new CommentsAdapter(commentList, this, userId, this, users);
        rvComments.setAdapter(commentsAdapter);

        fabAddComment.setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(this, "please login in order to add a comment", Toast.LENGTH_SHORT).show();
                goToLogIn();
            } else {
                showAddCommentDialog();
            }
        });
    }

    private void goToLogIn(){
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }

    private void handleActionButtons() {
        ImageButton btnShare = findViewById(R.id.tv_btn_share);
        ImageButton btnLike = findViewById(R.id.tv_btn_like);
        ImageButton btnDislike = findViewById(R.id.tv_btn_dislike);
        ImageButton btnEdit = findViewById(R.id.edit_video);
        Button btnSubscribe = findViewById(R.id.btn_subscribe);

        updateActionButtons(btnLike, btnDislike, btnSubscribe);

        btnShare.setOnClickListener(v -> shareVideo());
        btnLike.setOnClickListener(v -> handleLikeAction(btnLike, btnDislike));
        btnDislike.setOnClickListener(v -> handleDislikeAction(btnLike, btnDislike));
        btnSubscribe.setOnClickListener(v -> handleSubscribeAction(btnSubscribe));
        btnEdit.setOnClickListener(v -> showEditVideoDialog());
    }

    private void updateActionButtons(ImageButton btnLike, ImageButton btnDislike, Button btnSubscribe) {
        if (isLiked) {
            btnLike.setImageResource(R.drawable.ic_like_fill);
        }
        if (isDisliked) {
            btnDislike.setImageResource(R.drawable.ic_dislike_fill);
        }
        if (isSubscribe) {
            btnSubscribe.setBackgroundColor(ContextCompat.getColor(this, R.color.text_color));
            btnSubscribe.setTextColor(ContextCompat.getColor(this, R.color.system_color));
            btnSubscribe.setText(R.string.unsubscribe);
        }
    }

    private void handleBackButton() {
        ImageButton btnBack = findViewById(R.id.tv_video_back);
        btnBack.setOnClickListener(v -> handleBackAction());

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackAction();
            }
        });
    }

    private void handleBackAction() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("user", userId);
        startActivity(intent);
    }

    private void toggleComments() {
        if (areCommentsVisible) {
            rvComments.setVisibility(View.GONE);
            fabAddComment.setVisibility(View.GONE);
            ivToggleComments.setImageResource(R.drawable.ic_arrow_down);
        } else {
            rvComments.setVisibility(View.VISIBLE);
            fabAddComment.setVisibility(View.VISIBLE);
            ivToggleComments.setImageResource(R.drawable.ic_arrow_up);
        }
        areCommentsVisible = !areCommentsVisible;
    }

    @SuppressLint({"DefaultLocale", "NotifyDataSetChanged"})
    private void showAddCommentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a comment");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String commentText = input.getText().toString().trim();
            if (!commentText.isEmpty()) {
                comment newComment = new comment(commentText, userId, GeneralUtils.getTheDate());
                commentList.add(newComment);
                videos.get(videoPosition).setComments(commentList);
                commentsAdapter.notifyDataSetChanged();
                tvComments.setText(String.format("Comments (%d)", commentList.size()));
                db.videoDao().update(videos.get(videoId));
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @SuppressLint({"NotifyDataSetChanged", "DefaultLocale"})
    public void removeComment(int position) {
        commentList.remove(position);
        videos.get(videoPosition).setComments(commentList);
        commentsAdapter.notifyDataSetChanged();
        tvComments.setText(String.format("Comments (%d)", commentList.size()));
        db.videoDao().update(videos.get(videoId));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void editComment(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit comment");

        final EditText input = new EditText(this);
        input.setText(commentList.get(position).getComment());
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String editedCommentText = input.getText().toString().trim();
            if (!editedCommentText.isEmpty()) {
                commentList.get(position).setComment(editedCommentText);
                commentList.get(position).setDate(GeneralUtils.getTheDate());
                videos.get(videoPosition).setComments(commentList);
                commentsAdapter.notifyDataSetChanged();
                db.videoDao().update(videos.get(videoId));
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showEditVideoDialog() {
        if (userId == -1) {
            Toast.makeText(this, "please login in order to edit a video", Toast.LENGTH_SHORT).show();
            goToLogIn();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Video");

        View view = getLayoutInflater().inflate(R.layout.dialog_edit_video, null);
        inputVideoName = view.findViewById(R.id.input_video_name);
        Button btnSelectThumbnail = view.findViewById(R.id.btn_select_thumbnail);
        Button btnSelectVideo = view.findViewById(R.id.btn_select_video);

        inputVideoName.setText(videos.get(videoId).getVideo_name());

        btnSelectThumbnail.setOnClickListener(v -> pickMedia(PICK_VIDEO_THUMBNAIL));
        btnSelectVideo.setOnClickListener(v -> pickMedia(PICK_VIDEO_FILE));

        builder.setView(view);
        builder.setPositiveButton("Save", (dialog, which) -> saveVideoChanges());
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog editDialog = builder.create();
        editDialog.show();
    }

    private void pickMedia(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(requestCode == PICK_VIDEO_THUMBNAIL ? "image/*" : "video/*");
        startActivityForResult(intent, requestCode);
    }

    private void saveVideoChanges() {
        String newName = inputVideoName.getText().toString().trim();
        if (!newName.isEmpty()) {
            videos.get(videoId).setVideo_name(newName);
        }
        if (newThumbnailUri != null) {
            videos.get(videoId).setThumbnail(newThumbnailUri.toString());
        }
        if (newVideoUri != null) {
            videos.get(videoId).setVideo_path(newVideoUri.toString());
            final VideoView videoView = findViewById(R.id.tv_video_view);
            videoView.setVideoURI(newVideoUri);
            videoView.start();
        }
        TextView tvVideoName = findViewById(R.id.tv_video_name);
        tvVideoName.setText(videos.get(videoId).getVideo_name());

        db.videoDao().update(videos.get(videoId));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedMediaUri = data.getData();
            if (requestCode == PICK_VIDEO_THUMBNAIL) {
                newThumbnailUri = selectedMediaUri;
                Toast.makeText(this, "Thumbnail selected", Toast.LENGTH_SHORT).show();
            } else if (requestCode == PICK_VIDEO_FILE) {
                newVideoUri = selectedMediaUri;
                Toast.makeText(this, "Video selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void shareVideo() {
        if (videos.get(videoId) != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this video");
            shareIntent.putExtra(Intent.EXTRA_TEXT, videos.get(videoId).getVideo_path());
            startActivity(Intent.createChooser(shareIntent, "Share Video"));
        }
    }

    private void handleLikeAction(ImageButton btnLike, ImageButton btnDislike) {
        if (userId == -1) {
            Toast.makeText(this, "please login in order to like", Toast.LENGTH_SHORT).show();
            goToLogIn();
            return;
        }

        if (!isLiked) {
            users.get(userId).addToLiked(videos.get(videoId));
            users.get(userId).removeFromDisLiked(videos.get(videoId));
            btnLike.setImageResource(R.drawable.ic_like_fill);
            isLiked = true;
            if (isDisliked) {
                btnDislike.setImageResource(R.drawable.ic_dislike);
                isDisliked = false;
            }
        } else {
            users.get(userId).removeFromLiked(videos.get(videoId));
            btnLike.setImageResource(R.drawable.ic_like);
            isLiked = false;
        }
        db.userDao().update(users.get(userId));
    }

    private void handleDislikeAction(ImageButton btnLike, ImageButton btnDislike) {
        if (userId == -1) {
            Toast.makeText(this, "please login in order to dislike", Toast.LENGTH_SHORT).show();
            goToLogIn();
            return;
        }

        if (!isDisliked) {
            users.get(userId).addToDisLiked(videos.get(videoId));
            users.get(userId).removeFromLiked(videos.get(videoId));
            btnDislike.setImageResource(R.drawable.ic_dislike_fill);
            isDisliked = true;
            if (isLiked) {
                btnLike.setImageResource(R.drawable.ic_like);
                isLiked = false;
            }
        } else {
            users.get(userId).removeFromDisLiked(videos.get(videoId));
            btnDislike.setImageResource(R.drawable.ic_dislike);
            isDisliked = false;
        }
        db.userDao().update(users.get(userId));
    }

    private void handleSubscribeAction(Button btnSubscribe) {
        if (userId == -1) {
            Toast.makeText(this, "please login in order to subscribe", Toast.LENGTH_SHORT).show();
            goToLogIn();
            return;
        }
        int subCount = Integer.parseInt(users.get(videos.get(videoId).getCreator()).getSubs_count());

        if (!isSubscribe) {
            users.get(userId).addToSubs(users.get(videos.get(videoId).getCreator()));
            btnSubscribe.setBackgroundColor(ContextCompat.getColor(this, R.color.text_color));
            btnSubscribe.setTextColor(ContextCompat.getColor(this, R.color.system_color));
            btnSubscribe.setText(R.string.unsubscribe);
            users.get(videos.get(videoPosition).getCreator()).setSubs_count(String.valueOf(subCount + 1));
            isSubscribe = true;
        } else {
            users.get(userId).removeFromSubs(users.get(videos.get(videoId).getCreator()));
            btnSubscribe.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            btnSubscribe.setTextColor(ContextCompat.getColor(this, R.color.white));
            btnSubscribe.setText(R.string.subscribe);
            users.get(videos.get(videoPosition).getCreator()).setSubs_count(String.valueOf(subCount - 1));
            isSubscribe = false;
        }
        db.userDao().update(users.get(userId));
    }
}
