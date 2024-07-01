package com.example.youtube.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;
import com.example.youtube.entities.comment;
import com.example.youtube.entities.user;
import com.example.youtube.entities.video;
import com.example.youtube.screens.LogIn;
import com.example.youtube.screens.VideoPlayerActivity;
import com.example.youtube.utils.GeneralUtils;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {
    private final ArrayList<comment> commentList;
    private final VideoPlayerActivity videoPlayerActivity;
    private final user user;
    private final Context context;
    private final LayoutInflater mInflater;
    private final ArrayList<video> videos;
    private final ArrayList<user> users;


    public CommentsAdapter(ArrayList<comment> commentList, VideoPlayerActivity videoPlayerActivity,
                           user user, Context context,ArrayList<video> videos, ArrayList<user> users) {
        mInflater = LayoutInflater.from(context);
        this.commentList = commentList;
        this.context = context;
        this.videoPlayerActivity = videoPlayerActivity;
        this.user = user;
        this.videos = videos;
        this.users = users;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        comment currentComment = commentList.get(position);
        holder.tvCommentUser.setText(currentComment.getUser().getName());
        holder.tvCommentText.setText(currentComment.getComment());
        holder.tvCommentDate.setText(GeneralUtils.timeAgo(currentComment.getDate()));

        String userPic = currentComment.getUser().getProfile_pic();
        int creatorPicId = mInflater.getContext().getResources().getIdentifier(userPic, "drawable", mInflater.getContext().getPackageName());
        if (creatorPicId != 0) {
            holder.user_pic.setImageResource(creatorPicId);
        } else {
            holder.user_pic.setImageURI(Uri.parse(userPic));
        }

        holder.tvEditComment.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.getMenuInflater().inflate(R.menu.comment_options_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                if (user == null){
                    Toast.makeText(context, "please login in order to edit or delete comments",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, LogIn.class);
                    intent.putParcelableArrayListExtra("video_list", videos);
                    intent.putParcelableArrayListExtra("users", users);
                    context.startActivity(intent);
                }else {
                    if (item.getItemId() == R.id.action_edit_comment) {
                        videoPlayerActivity.editComment(position);
                        return true;
                    } else if (item.getItemId() == R.id.action_delete_comment) {
                        videoPlayerActivity.removeComment(position);
                        return true;
                    }
                }
                return false;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvCommentUser;
        TextView tvCommentText;
        TextView tvCommentDate;
        ImageButton tvEditComment;
        ShapeableImageView user_pic;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCommentUser = itemView.findViewById(R.id.tv_comment_user);
            tvCommentText = itemView.findViewById(R.id.tv_comment_text);
            tvCommentDate = itemView.findViewById(R.id.tv_comment_date);
            tvEditComment = itemView.findViewById(R.id.tv_comment_options);
            user_pic = itemView.findViewById(R.id.user_pic);
        }
    }
}
