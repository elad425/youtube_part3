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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.MainActivity;
import com.example.youtube.R;
import com.example.youtube.UserSession;
import com.example.youtube.adapters.CommentsAdapter;
import com.example.youtube.adapters.VideoListAdapter;
import com.example.youtube.entities.comment;
import com.example.youtube.entities.user;
import com.example.youtube.entities.video;
import com.example.youtube.utils.GeneralUtils;
import com.example.youtube.viewmodels.VideoPlayerViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Objects;

public class VideoPlayerActivity extends AppCompatActivity {

    private static final int PICK_VIDEO_THUMBNAIL = 100;
    private static final int PICK_VIDEO_FILE = 101;
    private boolean areCommentsVisible = false;
    private RecyclerView rvComments;
    private TextView tvComments;
    private ImageButton ivToggleComments;
    private FloatingActionButton fabAddComment;
    private CommentsAdapter commentsAdapter;
    private Uri newThumbnailUri;
    private Uri newVideoUri;
    private EditText inputVideoName;
    private VideoPlayerViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        viewModel = new ViewModelProvider(this).get(VideoPlayerViewModel.class);
        viewModel.loadVideo(getIntent().getIntExtra("video_item", -1));
        viewModel.loadUser(UserSession.getInstance().getUserId());
        viewModel.incrementViews();

        initializeUI();
        setupVideoView();
        handleCommentsSection();
        handleActionButtons();
        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getCurrentVideo().observe(this, this::updateVideoInfo);
        viewModel.getCurrentCreator().observe(this, this::updateCreatorInfo);
        viewModel.isLiked().observe(this, this::updateLikeButton);
        viewModel.isDisliked().observe(this, this::updateDislikeButton);
        viewModel.isSubscribed().observe(this, this::updateSubscribeButton);
        viewModel.getCommentList().observe(this, this::updateComments);
    }

    private void initializeUI() {
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));

        RecyclerView lstVideos = findViewById(R.id.lstVideos);
        VideoListAdapter videoListAdapter = new VideoListAdapter(this,
                viewModel.getCurrentVideo().getValue(),viewModel.getUsers());
        lstVideos.setAdapter(videoListAdapter);
        lstVideos.setLayoutManager(new LinearLayoutManager(this));

        viewModel.getVideos().observe(this, videoListAdapter::setVideos);

        ImageButton btnBack = findViewById(R.id.tv_video_back);
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupVideoView() {
        final VideoView videoView = findViewById(R.id.tv_video_view);
        viewModel.getCurrentVideo().observe(this, currentVideo -> {
            if (currentVideo != null) {
                videoView.setVideoURI(Uri.parse(currentVideo.getVideo_path()));
                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                videoView.start();
            }
        });
    }

    private void updateVideoInfo(video currentVideo) {
        if (currentVideo != null) {
            TextView tvVideoName = findViewById(R.id.tv_video_name);
            TextView tvVideoViews = findViewById(R.id.tv_video_views);
            TextView tvPublishDate = findViewById(R.id.tv_publish_date);

            String formatViews = GeneralUtils.getViews(currentVideo.getViews()) + " views";
            tvVideoName.setText(currentVideo.getVideo_name());
            tvVideoViews.setText(formatViews);
            tvPublishDate.setText(GeneralUtils.timeAgo(currentVideo.getDate_of_release()));
        }
    }

    private void updateCreatorInfo(user currentCreator) {
        if (currentCreator != null) {
            TextView tvCreator = findViewById(R.id.tv_creator);
            TextView creatorSubCount = findViewById(R.id.tv_creator_subs);
            ShapeableImageView creatorPic = findViewById(R.id.creator_pic);

            tvCreator.setText(currentCreator.getName());
            creatorSubCount.setText(GeneralUtils.getViews(currentCreator.getSubs_count()));

            String creatorPicPath = currentCreator.getProfile_pic();
            int creatorPicId = getResources().getIdentifier(creatorPicPath, "drawable", getPackageName());
            if (creatorPicId != 0) {
                creatorPic.setImageResource(creatorPicId);
            } else {
                creatorPic.setImageURI(Uri.parse(creatorPicPath));
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private void handleCommentsSection() {
        tvComments = findViewById(R.id.tv_comments);
        rvComments = findViewById(R.id.rv_comments);
        ivToggleComments = findViewById(R.id.iv_toggle_comments);
        fabAddComment = findViewById(R.id.fab_add_comment);
        int userId;
        if (viewModel.getCurrentUser().getValue() == null){
            userId = 0;
        } else {
            userId = viewModel.getCurrentUser().getValue().getId();
        }

        ivToggleComments.setOnClickListener(v -> toggleComments());
        tvComments.setOnClickListener(v -> toggleComments());

        rvComments.setLayoutManager(new LinearLayoutManager(this));
        commentsAdapter = new CommentsAdapter(new ArrayList<>(), this,
                userId, viewModel.getUsers());
        rvComments.setAdapter(commentsAdapter);

        viewModel.getCommentList().observe(this, comments -> {
            commentsAdapter.updateComments(comments);
            tvComments.setText(String.format("Comments (%d)", comments.size()));
        });

        fabAddComment.setOnClickListener(v -> {
            if (viewModel.getCurrentUser().getValue() == null) {
                Toast.makeText(this, "Please login to add a comment", Toast.LENGTH_SHORT).show();
                goToLogIn();
            } else {
                showAddCommentDialog();
            }
        });
    }

    private void updateComments(ArrayList<comment> comments) {
        commentsAdapter.updateComments(comments);
        tvComments.setText(String.format("Comments (%d)", comments.size()));
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

    private void handleActionButtons() {
        ImageButton btnShare = findViewById(R.id.tv_btn_share);
        ImageButton btnLike = findViewById(R.id.tv_btn_like);
        ImageButton btnDislike = findViewById(R.id.tv_btn_dislike);
        ImageButton btnEdit = findViewById(R.id.edit_video);
        Button btnSubscribe = findViewById(R.id.btn_subscribe);

        viewModel.getCurrentUser().observe(this, currentUser ->
                viewModel.getCurrentCreator().observe(this, currentCreator -> {
            if (currentUser == null || currentCreator == null || currentCreator.getId() != currentUser.getId()) {
                btnEdit.setVisibility(View.GONE);
            }
            if (currentUser != null && currentCreator != null && currentCreator.getId() == currentUser.getId()) {
                btnSubscribe.setVisibility(View.GONE);
            }
        }));

        btnShare.setOnClickListener(v -> shareVideo());
        btnLike.setOnClickListener(v -> handleLikeAction());
        btnDislike.setOnClickListener(v -> handleDislikeAction());
        btnSubscribe.setOnClickListener(v -> handleSubscribeAction());
        btnEdit.setOnClickListener(this::showEditVideoOptions);
    }

    private void updateLikeButton(boolean isLiked) {
        ImageButton btnLike = findViewById(R.id.tv_btn_like);
        btnLike.setImageResource(isLiked ? R.drawable.ic_like_fill : R.drawable.ic_like);
    }

    private void updateDislikeButton(boolean isDisliked) {
        ImageButton btnDislike = findViewById(R.id.tv_btn_dislike);
        btnDislike.setImageResource(isDisliked ? R.drawable.ic_dislike_fill : R.drawable.ic_dislike);
    }

    private void updateSubscribeButton(boolean isSubscribed) {
        Button btnSubscribe = findViewById(R.id.btn_subscribe);
        if (isSubscribed) {
            btnSubscribe.setBackgroundColor(ContextCompat.getColor(this, R.color.text_color));
            btnSubscribe.setTextColor(ContextCompat.getColor(this, R.color.system_color));
            btnSubscribe.setText(R.string.unsubscribe);
        } else {
            btnSubscribe.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
            btnSubscribe.setTextColor(ContextCompat.getColor(this, R.color.white));
            btnSubscribe.setText(R.string.subscribe);
        }
    }

    private void showAddCommentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a comment");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String commentText = input.getText().toString().trim();
            if (!commentText.isEmpty()) {
                viewModel.addComment(commentText);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    public void removeComment(int position) {
        viewModel.removeComment(position);
    }

    public void editComment(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit comment");

        final EditText input = new EditText(this);
        input.setText(Objects.requireNonNull(viewModel.getCommentList().getValue()).get(position).getComment());
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String editedCommentText = input.getText().toString().trim();
            if (!editedCommentText.isEmpty()) {
                viewModel.editComment(position, editedCommentText);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showEditVideoOptions(View v) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.getMenuInflater().inflate(R.menu.video_options_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_delete_video) {
                if (viewModel.getCurrentUser().getValue() == null) {
                    Toast.makeText(this, "Please login to delete a video", Toast.LENGTH_SHORT).show();
                    goToLogIn();
                } else {
                    viewModel.deleteVideo();
                    Intent intent = new Intent(this, MainActivity.class);
                    this.startActivity(intent);
                    Toast.makeText(this, "Video deleted successfully", Toast.LENGTH_SHORT).show();
                }
                return true;
            } else if (item.getItemId() == R.id.action_edit_video) {
                showEditVideoDialog();
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void showEditVideoDialog() {
        if (viewModel.getCurrentUser().getValue() == null) {
            Toast.makeText(this, "Please login to edit a video", Toast.LENGTH_SHORT).show();
            goToLogIn();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Video");

        View view = getLayoutInflater().inflate(R.layout.dialog_edit_video, null);
        inputVideoName = view.findViewById(R.id.input_video_name);
        Button btnSelectThumbnail = view.findViewById(R.id.btn_select_thumbnail);
        Button btnSelectVideo = view.findViewById(R.id.btn_select_video);

        inputVideoName.setText(Objects.requireNonNull(viewModel.getCurrentVideo().getValue()).getVideo_name());

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
        viewModel.updateVideoDetails(newName, newThumbnailUri, newVideoUri);
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
        video currentVideo = viewModel.getCurrentVideo().getValue();
        if (currentVideo != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this video");
            shareIntent.putExtra(Intent.EXTRA_TEXT, currentVideo.getVideo_path());
            startActivity(Intent.createChooser(shareIntent, "Share Video"));
        }
    }

    private void handleLikeAction() {
        if (viewModel.getCurrentUser().getValue() == null) {
            Toast.makeText(this, "Please login to like the video", Toast.LENGTH_SHORT).show();
            goToLogIn();
            return;
        }
        viewModel.toggleLike();
    }

    private void handleDislikeAction() {
        if (viewModel.getCurrentUser().getValue() == null) {
            Toast.makeText(this, "Please login to dislike the video", Toast.LENGTH_SHORT).show();
            goToLogIn();
            return;
        }

        viewModel.toggleDislike();
    }

    private void handleSubscribeAction() {
        if (viewModel.getCurrentUser().getValue() == null) {
            Toast.makeText(this, "Please login to subscribe", Toast.LENGTH_SHORT).show();
            goToLogIn();
            return;
        }

        int newSubCount = viewModel.toggleSubscribe();
        if (newSubCount != -1) {
            TextView creatorSubCount = findViewById(R.id.tv_creator_subs);
            creatorSubCount.setText(GeneralUtils.getViews(String.valueOf(newSubCount)));
        }
    }

    private void goToLogIn() {
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }
}
