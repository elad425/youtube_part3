package com.example.youtube.viewmodels;


import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.entities.Comment;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;
import com.example.youtube.repositories.MediaRepository;
import com.example.youtube.repositories.VideoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VideoPlayerViewModel extends AndroidViewModel {
    private final VideoRepository videoRepository;
    private final MediaRepository mediaRepository;
    private final MutableLiveData<Bitmap> bitmap = new MutableLiveData<>();
    private final MutableLiveData<Video> currentVideo = new MutableLiveData<>();
    private final MutableLiveData<User> currentUser = new MutableLiveData<>();
    private final MutableLiveData<User> currentCreator = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLiked = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isDisliked = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isEditVideoVisible = new MutableLiveData<>(false);
    private MutableLiveData<List<Comment>> commentList = new MutableLiveData<>(new ArrayList<>());

    public VideoPlayerViewModel(Application application) {
        super(application);
        videoRepository = new VideoRepository(application);
        mediaRepository = new MediaRepository(application);
    }

    public LiveData<List<Video>> getVideos(){
        return videoRepository.getAllVideosLive();
    }

    public LiveData<byte[]> getVideoLive(){return mediaRepository.getVideoLive();}

    public MediaRepository getMediaRepository() {
        return mediaRepository;
    }

    public void loadVideo(String videoId) {
        Video loadedVideo = videoRepository.getVideoById(videoId);
        currentVideo.setValue(loadedVideo);
        if (loadedVideo != null) {
            currentCreator.setValue(loadedVideo.getUserDetails());
            commentList = videoRepository.getCommentByVideoId(
                    Objects.requireNonNull(currentVideo.getValue()).get_id());
            Bitmap b = mediaRepository.getImage(Objects.requireNonNull(currentVideo.getValue().getUserDetails().getIcon()));
            bitmap.setValue(b);
        }
    }

    public void loadUser(User user) {
        currentUser.setValue(user);
        if (user != null && currentVideo.getValue() != null) {
            isLiked.setValue(currentVideo.getValue().isLiked(user.get_id()));
            isDisliked.setValue(currentVideo.getValue().isDisLiked(user.get_id()));
            if ((Objects.requireNonNull(getCurrentUser().getValue()).get_id().equals(
                    Objects.requireNonNull(getCurrentVideo().getValue()).getUserDetails().get_id()))){
                isEditVideoVisible.setValue(true);
            }
        }
    }

    public void initCommentsMedia(List<Comment> comments){
        mediaRepository.initCommentMedia(comments);
    }

    public void incrementViews() {
        Video video = currentVideo.getValue();
        if (video != null) {
            video.setViews(video.getViews() + 1);
            videoRepository.updateVideo(video);
            currentVideo.setValue(video);
        }
    }

    public void toggleLike() {
        User user = currentUser.getValue();
        Video video = currentVideo.getValue();
        if (user != null && video != null) {
            if (Boolean.FALSE.equals(isLiked.getValue())) {
                video.addToLiked(user.get_id());
                video.removeFromDisliked(user.get_id());
                isLiked.setValue(true);
                isDisliked.setValue(false);
            } else {
                video.removeFromLiked(user.get_id());
                isLiked.setValue(false);
            }
            videoRepository.updateVideo(video);
        }
    }

    public void toggleDislike() {
        User user = currentUser.getValue();
        Video video = currentVideo.getValue();
        if (user != null && video != null) {
            if (Boolean.FALSE.equals(isDisliked.getValue())) {
                video.addToDisliked(user.get_id());
                video.removeFromLiked(user.get_id());
                isDisliked.setValue(true);
                isLiked.setValue(false);
            } else {
                video.removeFromDisliked(user.get_id());
                isDisliked.setValue(false);
            }
            videoRepository.updateVideo(video);
        }
    }

    public void addComment(String commentText) {
        User user = currentUser.getValue();
        Video video = currentVideo.getValue();
        if (user != null && video != null) {
            Comment newComment = new Comment(commentText, user, video.get_id());
            List<Comment> updatedComments = commentList.getValue();
            if (updatedComments != null) {
                videoRepository.addComment(newComment);
                updatedComments.add(newComment);
                commentList.setValue(updatedComments);
            }
        }
    }

    public void removeComment(int position) {
        Video video = currentVideo.getValue();
        if (video != null) {
            List<Comment> updatedComments = commentList.getValue();
            if (updatedComments != null && position < updatedComments.size()) {
                videoRepository.deleteComment(updatedComments.get(position));
                updatedComments.remove(position);
                commentList.setValue(updatedComments);
            }
        }
    }

    public void editComment(int position, String editedCommentText) {
        Video video = currentVideo.getValue();
        if (video != null) {
            List<Comment> updatedComments = commentList.getValue();
            if (updatedComments != null && position < updatedComments.size()) {
                Comment editedComment = updatedComments.get(position);
                editedComment.setCommentMessage(editedCommentText);
                videoRepository.updateComment(editedComment);
                commentList.setValue(updatedComments);
            }
        }
    }

    public void updateVideoDetails(String newName, Uri newThumbnailUri, Uri newVideoUri) {
        Video video = currentVideo.getValue();
        if (video != null) {
            if (!newName.isEmpty()) {
                video.setTitle(newName);
            }
            if (newThumbnailUri != null) {
                video.setThumbnail(newThumbnailUri.toString());
            }
            if (newVideoUri != null) {
                video.setVideo_src(newVideoUri.toString());
            }
            videoRepository.updateVideo(video);
            currentVideo.setValue(video);
        }
    }

    public void deleteVideo() {
        Video video = currentVideo.getValue();
        if (video != null) {
            videoRepository.deleteVideo(video);
        }
    }

    // Getters for LiveData
    public LiveData<Video> getCurrentVideo() { return currentVideo; }
    public LiveData<User> getCurrentUser() { return currentUser; }
    public LiveData<User> getCurrentCreator() { return currentCreator; }
    public LiveData<Boolean> isLiked() { return isLiked; }
    public LiveData<Boolean> isDisliked() { return isDisliked; }
    public LiveData<Boolean> isEditVideoVisible() { return isEditVideoVisible; }
    public LiveData<List<Comment>> getCommentList() { return commentList; }
    public MutableLiveData<Bitmap> getBitmap() {return bitmap;}
}