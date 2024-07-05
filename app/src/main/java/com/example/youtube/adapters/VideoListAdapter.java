package com.example.youtube.adapters;

import static com.example.youtube.utils.GeneralUtils.getUserById;
import static com.example.youtube.utils.GeneralUtils.removeVideo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;
import com.example.youtube.entities.user;
import com.example.youtube.entities.video;
import com.example.youtube.screens.VideoPlayerActivity;
import com.example.youtube.utils.GeneralUtils;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {
    private final Context context;
    private List<video> videos;
    private final List<user> users;
    private final video currentVideo;


    public VideoListAdapter(Context context, video currentVideo, List<user> users) {
        this.context = context;
        this.users = users;
        this.currentVideo = currentVideo;
        this.videos = new ArrayList<>();
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        video currentVideo = videos.get(position);
        user creator = getUserById(users, currentVideo.getCreatorId());
        String formatViews = GeneralUtils.getViews(currentVideo.getViews()) + " views";
        holder.video_name.setText(currentVideo.getVideo_name());
        assert creator != null;
        holder.creator.setText(creator.getName());
        holder.views.setText(formatViews);
        holder.publish_date.setText(GeneralUtils.timeAgo(currentVideo.getDate_of_release()));
        holder.video_length.setText(currentVideo.getVideo_length());

        // Load thumbnail
        String thumbnailName = currentVideo.getThumbnail();
        int thumbnailId = context.getResources().getIdentifier(thumbnailName, "drawable", context.getPackageName());
        if (thumbnailId != 0) {
            holder.thumbnail.setImageResource(thumbnailId);
        } else {
            holder.thumbnail.setImageURI(Uri.parse(thumbnailName));
        }

        // Load creator picture
        String creatorPic = creator.getProfile_pic();
        int creatorPicId = context.getResources().getIdentifier(creatorPic, "drawable", context.getPackageName());
        if (creatorPicId != 0) {
            holder.creator_pic.setImageResource(creatorPicId);
        } else {
            holder.creator_pic.setImageURI(Uri.parse(creatorPic));
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VideoPlayerActivity.class);
            intent.putExtra("video_item", currentVideo.getVideoId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public void setVideos(List<video> videos) {
        this.videos.clear();
        this.videos.addAll(videos);
        this.videos = removeVideo(this.videos,currentVideo);
        notifyDataSetChanged();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView video_name;
        TextView creator;
        TextView views;
        TextView publish_date;
        ImageView thumbnail;
        TextView video_length;
        ShapeableImageView creator_pic;

        VideoViewHolder(View itemView) {
            super(itemView);
            video_name = itemView.findViewById(R.id.video_name);
            creator = itemView.findViewById(R.id.creator);
            views = itemView.findViewById(R.id.views);
            publish_date = itemView.findViewById(R.id.publish_date);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            video_length = itemView.findViewById(R.id.video_length);
            creator_pic = itemView.findViewById(R.id.creator_pic);
        }
    }
}