package com.example.youtube.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;
import com.example.youtube.entities.user;
import com.example.youtube.entities.video;
import com.example.youtube.screens.VideoPlayerActivity;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.VideoViewHolder> {
    private ArrayList<video> filteredVideoList;
    private final ArrayList<video> videoList;
    private final ArrayList<user> users;
    private final LayoutInflater mInflater;
    private final user user;

    public SearchAdapter(ArrayList<video> videoList, ArrayList<video> filteredVideoList,
                         Context context, user user, ArrayList<user> users) {
        this.videoList = videoList;
        this.users = users;
        this.filteredVideoList = filteredVideoList;
        this.mInflater = LayoutInflater.from(context);
        this.user = user;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredList(ArrayList<video> filteredList){
        this.filteredVideoList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        video video = filteredVideoList.get(position);
        holder.searchResultTextView.setText(video.getVideo_name());

        holder.itemView.setOnClickListener(v -> {
            video clickedVideoItem = filteredVideoList.get(holder.getAdapterPosition());
            Intent i = new Intent(mInflater.getContext(), VideoPlayerActivity.class);
            i.putExtra("video_item", clickedVideoItem);
            i.putParcelableArrayListExtra("users", users);
            i.putParcelableArrayListExtra("video_list", videoList);
            i.putExtra("user", user);
            mInflater.getContext().startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return filteredVideoList.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView searchResultTextView;
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            searchResultTextView = itemView.findViewById(R.id.search_result);
        }
    }
}
