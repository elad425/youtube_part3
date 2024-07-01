package com.example.youtube.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;
import com.example.youtube.entities.user;
import com.example.youtube.entities.video;
import com.example.youtube.screens.LogIn;
import com.example.youtube.screens.VideoPlayerActivity;
import com.example.youtube.utils.GeneralUtils;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {
    private final LayoutInflater mInflater;
    private ArrayList<video> videos;
    private ArrayList<user> users;
    private ArrayList<video> filteredVideos;
    private final Context context;
    private final user user;

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        private final TextView video_name;
        private final TextView creator;
        private final TextView views;
        private final TextView publish_date;
        private final ImageView thumbnail;
        private final TextView video_length;
        private final ImageButton video_options;
        private final ShapeableImageView creator_pic;

        private VideoViewHolder(View itemView) {
            super(itemView);
            video_name = itemView.findViewById(R.id.video_name);
            creator = itemView.findViewById(R.id.creator);
            views = itemView.findViewById(R.id.views);
            publish_date = itemView.findViewById(R.id.publish_date);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            video_length = itemView.findViewById(R.id.video_length);
            video_options = itemView.findViewById(R.id.tv_video_option);
            creator_pic = itemView.findViewById(R.id.creator_pic);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setVideos(ArrayList<video> v, ArrayList<user> u) {
        videos = v;
        users = u;
        filteredVideos = new ArrayList<>(v);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filteredVideos != null ? filteredVideos.size() : 0;
    }

    public VideoListAdapter(Context context, user user) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.user = user;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        if (filteredVideos != null) {
            final video current = filteredVideos.get(position);
            String formatViews = GeneralUtils.getViews(current.getViews()) + " views";
            holder.video_name.setText(current.getVideo_name());
            holder.creator.setText(current.getCreator().getName());
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
            String creatorPic = current.getCreator().getProfile_pic();
            int creatorPicId = mInflater.getContext().getResources().getIdentifier(creatorPic, "drawable", mInflater.getContext().getPackageName());
            if (creatorPicId != 0) {
                holder.creator_pic.setImageResource(creatorPicId);
            } else {
                holder.creator_pic.setImageURI(Uri.parse(creatorPic));
            }
        }

        holder.itemView.setOnClickListener(v -> {
            video clickedVideoItem = filteredVideos.get(holder.getAdapterPosition());
            Intent i = new Intent(mInflater.getContext(), VideoPlayerActivity.class);
            // add one view per click
            int videoViews = Integer.parseInt(clickedVideoItem.getViews()) + 1;
            clickedVideoItem.setViews(Integer.toString(videoViews));

            i.putExtra("video_item", clickedVideoItem);
            i.putExtra("user", user);
            i.putParcelableArrayListExtra("video_list", new ArrayList<>(videos));
            i.putParcelableArrayListExtra("users", users);
            mInflater.getContext().startActivity(i);
        });

        holder.video_options.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.getMenuInflater().inflate(R.menu.video_options_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_delete_video) {
                    if (user == null) {
                        Toast.makeText(context, "please login in order to download or delete a video", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, LogIn.class);
                        intent.putParcelableArrayListExtra("video_list", videos);
                        intent.putParcelableArrayListExtra("users", users);
                        context.startActivity(intent);
                    } else {
                        videos.remove(position);
                        filteredVideos.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, filteredVideos.size());
                    }
                    return true;
                } else if (item.getItemId() == R.id.action_download) {
                    Toast.makeText(context, "sign up to youtube premium for downloading videos", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }

    public void filter(video objectToExclude) {
        if (objectToExclude == null) {
            resetFilter();
        } else {
            filteredVideos.clear();
            for (video vid : videos) {
                if (!vid.getVideo_name().equals(objectToExclude.getVideo_name())) {
                    filteredVideos.add(vid);
                }
            }
            notifyDataSetChanged();
        }
    }

    public void resetFilter() {
        filteredVideos = new ArrayList<>(videos);
        notifyDataSetChanged();
    }
}
