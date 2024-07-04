package com.example.youtube.adapters;

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

import com.example.youtube.AppDatabase;
import com.example.youtube.R;
import com.example.youtube.entities.user;
import com.example.youtube.entities.video;
import com.example.youtube.screens.VideoPlayerActivity;
import com.example.youtube.utils.GeneralUtils;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class MainVideoListAdapter extends RecyclerView.Adapter<MainVideoListAdapter.VideoViewHolder> {
    private final LayoutInflater mInflater;
    private List<video> videos;
    private final AppDatabase db;

    public MainVideoListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.db = AppDatabase.getInstance(context);
        this.videos = new ArrayList<>();
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        if (videos != null) {
            final video current = videos.get(position);
            user currentCreator = db.userDao().getUserById(current.getCreatorId());
            String formatViews = GeneralUtils.getViews(current.getViews()) + " views";
            holder.video_name.setText(current.getVideo_name());
            holder.creator.setText(currentCreator.getName());
            holder.views.setText(formatViews);
            holder.publish_date.setText(GeneralUtils.timeAgo(current.getDate_of_release()));
            holder.video_length.setText(current.getVideo_length());

            // Load thumbnail
            String thumbnailName = current.getThumbnail();
            int thumbnailId = mInflater.getContext().getResources().getIdentifier(thumbnailName, "drawable", mInflater.getContext().getPackageName());
            if (thumbnailId != 0) {
                holder.thumbnail.setImageResource(thumbnailId);
            } else {
                holder.thumbnail.setImageURI(Uri.parse(thumbnailName));
            }
            // Load creator picture
            String creatorPic = currentCreator.getProfile_pic();
            int creatorPicId = mInflater.getContext().getResources().getIdentifier(creatorPic, "drawable", mInflater.getContext().getPackageName());
            if (creatorPicId != 0) {
                holder.creator_pic.setImageResource(creatorPicId);
            } else {
                holder.creator_pic.setImageURI(Uri.parse(creatorPic));
            }
        }

        holder.itemView.setOnClickListener(v -> {
            video clickedVideoItem = videos.get(holder.getAdapterPosition());
            Intent i = new Intent(mInflater.getContext(), VideoPlayerActivity.class);
            i.putExtra("video_item", clickedVideoItem.getVideoId());
            mInflater.getContext().startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return videos != null ? videos.size() : 0;
    }

    public void setVideos(List<video> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        private final TextView video_name;
        private final TextView creator;
        private final TextView views;
        private final TextView publish_date;
        private final ImageView thumbnail;
        private final TextView video_length;
        private final ShapeableImageView creator_pic;

        private VideoViewHolder(View itemView) {
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