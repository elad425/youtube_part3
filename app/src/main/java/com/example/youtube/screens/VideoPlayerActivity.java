package com.example.youtube.screens;

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
import com.example.youtube.UserSession;
import com.example.youtube.adapters.CommentsAdapter;
import com.example.youtube.adapters.VideoListAdapter;
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
    private int userId;
    private Uri newThumbnailUri;
    private Uri newVideoUri;
    private EditText inputVideoName;
    private AppDatabase db;
    private video currentVideo;
    private user currentUser;
    private user currentCreator;

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
        currentVideo = db.videoDao().getVideoById(intent.getIntExtra("video_item", -1));
        currentUser = db.userDao().getUserById(UserSession.getInstance().getUserId());
        currentCreator = db.userDao().getUserById(currentVideo.getCreatorId());

        int videoViews = Integer.parseInt(currentVideo.getViews()) + 1;
        currentVideo.setViews(Integer.toString(videoViews));
        db.videoDao().update(currentVideo);

        if (currentUser != null) {
            isLiked = currentUser.isLiked(currentVideo);
            isDisliked = currentUser.isDisLiked(currentVideo);
            isSubscribe = currentUser.isSubs(currentCreator);
            userId = currentUser.getId();
        }

        if (currentVideo != null) {
            lstVideos.setAdapter(new VideoListAdapter(this, userId, db, currentVideo));
            lstVideos.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private void setupVideoView() {
        if (currentVideo == null) return;

        final VideoView videoView = findViewById(R.id.tv_video_view);
        videoView.setVideoURI(Uri.parse(currentVideo.getVideo_path()));
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

        String formatViews = GeneralUtils.getViews(currentVideo.getViews()) + " views";
        tvVideoName.setText(currentVideo.getVideo_name());
        tvVideoViews.setText(formatViews);
        tvCreator.setText(currentCreator.getName());
        tvPublishDate.setText(GeneralUtils.timeAgo(currentVideo.getDate_of_release()));
        creatorSubCount.setText(GeneralUtils.getViews(currentCreator.getSubs_count()));

        String creatorPicPath = currentCreator.getProfile_pic();
        int creatorPicId = getResources().getIdentifier(creatorPicPath, "drawable", getPackageName());
        if (creatorPicId != 0) {
            creatorPic.setImageResource(creatorPicId);
        } else {
            creatorPic.setImageURI(Uri.parse(creatorPicPath));
        }
    }

    @SuppressLint("DefaultLocale")
    private void handleCommentsSection() {
        if (currentVideo == null) return;

        tvComments = findViewById(R.id.tv_comments);
        rvComments = findViewById(R.id.rv_comments);
        ivToggleComments = findViewById(R.id.iv_toggle_comments);
        fabAddComment = findViewById(R.id.fab_add_comment);

        commentList = currentVideo.getComments();
        tvComments.setText(String.format("Comments (%d)", commentList.size()));
        ivToggleComments.setOnClickListener(v -> toggleComments());
        tvComments.setOnClickListener(v -> toggleComments());

        rvComments.setLayoutManager(new LinearLayoutManager(this));
        commentsAdapter = new CommentsAdapter(commentList, this, userId, this, db);
        rvComments.setAdapter(commentsAdapter);

        fabAddComment.setOnClickListener(v -> {
            if (currentUser == null) {
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

        if ((currentUser == null) || (currentCreator.getId() != currentUser.getId())) {
            btnEdit.setVisibility(View.GONE);
        }

        if ((currentUser != null) && (currentCreator.getId() == currentUser.getId())){
            btnSubscribe.setVisibility(View.GONE);
        }

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
                currentVideo.setComments(commentList);
                commentsAdapter.notifyDataSetChanged();
                tvComments.setText(String.format("Comments (%d)", commentList.size()));
                db.videoDao().update(currentVideo);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @SuppressLint({"NotifyDataSetChanged", "DefaultLocale"})
    public void removeComment(int position) {
        commentList.remove(position);
        currentVideo.setComments(commentList);
        commentsAdapter.notifyDataSetChanged();
        tvComments.setText(String.format("Comments (%d)", commentList.size()));
        db.videoDao().update(currentVideo);
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
                currentVideo.setComments(commentList);
                commentsAdapter.notifyDataSetChanged();
                db.videoDao().update(currentVideo);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showEditVideoDialog() {
        if (currentUser == null) {
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

        inputVideoName.setText(currentVideo.getVideo_name());

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
            currentVideo.setVideo_name(newName);
        }
        if (newThumbnailUri != null) {
            currentVideo.setThumbnail(newThumbnailUri.toString());
        }
        if (newVideoUri != null) {
            currentVideo.setVideo_path(newVideoUri.toString());
            final VideoView videoView = findViewById(R.id.tv_video_view);
            videoView.setVideoURI(newVideoUri);
            videoView.start();
        }
        TextView tvVideoName = findViewById(R.id.tv_video_name);
        tvVideoName.setText(currentVideo.getVideo_name());

        db.videoDao().update(currentVideo);
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
        if (currentVideo != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this video");
            shareIntent.putExtra(Intent.EXTRA_TEXT, currentVideo.getVideo_path());
            startActivity(Intent.createChooser(shareIntent, "Share Video"));
        }
    }

    private void handleLikeAction(ImageButton btnLike, ImageButton btnDislike) {
        if (currentUser == null) {
            Toast.makeText(this, "please login in order to like", Toast.LENGTH_SHORT).show();
            goToLogIn();
            return;
        }

        if (!isLiked) {
            currentUser.addToLiked(currentVideo);
            currentUser.removeFromDisLiked(currentVideo);
            btnLike.setImageResource(R.drawable.ic_like_fill);
            isLiked = true;
            if (isDisliked) {
                btnDislike.setImageResource(R.drawable.ic_dislike);
                isDisliked = false;
            }
        } else {
            currentUser.removeFromLiked(currentVideo);
            btnLike.setImageResource(R.drawable.ic_like);
            isLiked = false;
        }
        db.userDao().update(currentUser);
    }

    private void handleDislikeAction(ImageButton btnLike, ImageButton btnDislike) {
        if (currentUser == null) {
            Toast.makeText(this, "please login in order to dislike", Toast.LENGTH_SHORT).show();
            goToLogIn();
            return;
        }

        if (!isDisliked) {
            currentUser.addToDisLiked(currentVideo);
            currentUser.removeFromLiked(currentVideo);
            btnDislike.setImageResource(R.drawable.ic_dislike_fill);
            isDisliked = true;
            if (isLiked) {
                btnLike.setImageResource(R.drawable.ic_like);
                isLiked = false;
            }
        } else {
            currentUser.removeFromDisLiked(currentVideo);
            btnDislike.setImageResource(R.drawable.ic_dislike);
            isDisliked = false;
        }
        db.userDao().update(currentUser);
    }

    private void handleSubscribeAction(Button btnSubscribe) {
        if (currentUser == null) {
            Toast.makeText(this, "please login in order to subscribe", Toast.LENGTH_SHORT).show();
            goToLogIn();
            return;
        }
        int subCount = Integer.parseInt(currentCreator.getSubs_count());

        if (!isSubscribe) {
            currentUser.addToSubs(currentCreator);
            btnSubscribe.setBackgroundColor(ContextCompat.getColor(this, R.color.text_color));
            btnSubscribe.setTextColor(ContextCompat.getColor(this, R.color.system_color));
            btnSubscribe.setText(R.string.unsubscribe);
            currentCreator.setSubs_count(String.valueOf(subCount + 1));
            isSubscribe = true;
        } else {
            currentUser.removeFromSubs(currentCreator);
            btnSubscribe.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            btnSubscribe.setTextColor(ContextCompat.getColor(this, R.color.white));
            btnSubscribe.setText(R.string.subscribe);
            currentCreator.setSubs_count(String.valueOf(subCount - 1));
            isSubscribe = false;
        }
        db.userDao().update(currentUser);
        db.userDao().update(currentCreator);
    }
}
