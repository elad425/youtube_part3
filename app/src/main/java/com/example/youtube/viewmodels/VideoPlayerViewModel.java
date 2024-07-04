// VideoPlayerViewModel.java
package com.example.youtube.viewmodels;

import static com.example.youtube.utils.GeneralUtils.getTheDate;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.entities.comment;
import com.example.youtube.entities.user;
import com.example.youtube.entities.video;
import com.example.youtube.repositories.UserRepository;
import com.example.youtube.repositories.VideoRepository;

import java.util.ArrayList;
import java.util.List;

public class VideoPlayerViewModel extends AndroidViewModel {
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final MutableLiveData<video> currentVideo = new MutableLiveData<>();
    private final MutableLiveData<user> currentUser = new MutableLiveData<>();
    private final MutableLiveData<user> currentCreator = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLiked = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isDisliked = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isSubscribed = new MutableLiveData<>(false);
    private final MutableLiveData<ArrayList<comment>> commentList = new MutableLiveData<>(new ArrayList<>());

    public VideoPlayerViewModel(Application application) {
        super(application);
        videoRepository = new VideoRepository(application);
        userRepository = new UserRepository(application);
    }

    public List<user> getUsers(){
        return userRepository.getAllUsers();
    }

    public LiveData<List<video>> getVideos(){
        return videoRepository.getAllVideosLive();
    }

    public void loadVideo(int videoId) {
        video loadedVideo = videoRepository.getVideoById(videoId);
        currentVideo.setValue(loadedVideo);
        if (loadedVideo != null) {
            currentCreator.setValue(userRepository.getUserById(loadedVideo.getCreatorId()));
            commentList.setValue(loadedVideo.getComments());
        }
    }

    public void loadUser(int userId) {
        user loadedUser = userRepository.getUserById(userId);
        currentUser.setValue(loadedUser);
        if (loadedUser != null && currentVideo.getValue() != null) {
            isLiked.setValue(loadedUser.isLiked(currentVideo.getValue()));
            isDisliked.setValue(loadedUser.isDisLiked(currentVideo.getValue()));
            isSubscribed.setValue(loadedUser.isSubs(currentCreator.getValue()));
        }
    }

    public void incrementViews() {
        video video = currentVideo.getValue();
        if (video != null) {
            int views = Integer.parseInt(video.getViews()) + 1;
            video.setViews(Integer.toString(views));
            videoRepository.updateVideo(video);
            currentVideo.setValue(video);
        }
    }

    public void toggleLike() {
        user user = currentUser.getValue();
        video video = currentVideo.getValue();
        if (user != null && video != null) {
            if (Boolean.FALSE.equals(isLiked.getValue())) {
                user.addToLiked(video);
                user.removeFromDisLiked(video);
                isLiked.setValue(true);
                isDisliked.setValue(false);
            } else {
                user.removeFromLiked(video);
                isLiked.setValue(false);
            }
            userRepository.updateUser(user);
        }
    }

    public void toggleDislike() {
        user user = currentUser.getValue();
        video video = currentVideo.getValue();
        if (user != null && video != null) {
            if (Boolean.FALSE.equals(isDisliked.getValue())) {
                user.addToDisLiked(video);
                user.removeFromLiked(video);
                isDisliked.setValue(true);
                isLiked.setValue(false);
            } else {
                user.removeFromDisLiked(video);
                isDisliked.setValue(false);
            }
            userRepository.updateUser(user);
        }
    }

    public int toggleSubscribe() {
        user user = currentUser.getValue();
        user creator = currentCreator.getValue();
        if (user != null && creator != null) {
            int subCount = Integer.parseInt(creator.getSubs_count());
            if (Boolean.FALSE.equals(isSubscribed.getValue())) {
                user.addToSubs(creator);
                subCount++;
                creator.setSubs_count(String.valueOf(subCount));
                isSubscribed.setValue(true);
            } else {
                user.removeFromSubs(creator);
                subCount--;
                creator.setSubs_count(String.valueOf(subCount));
                isSubscribed.setValue(false);
            }
            userRepository.updateUser(user);
            userRepository.updateUser(creator);
            currentCreator.setValue(creator);
            return subCount;
        }
        return -1;
    }

    public void addComment(String commentText) {
        user user = currentUser.getValue();
        video video = currentVideo.getValue();
        if (user != null && video != null) {
            comment newComment = new comment(commentText, user.getId(), getTheDate());
            ArrayList<comment> updatedComments = commentList.getValue();
            if (updatedComments != null) {
                updatedComments.add(newComment);
                video.setComments(updatedComments);
                videoRepository.updateVideo(video);
                commentList.setValue(updatedComments);
            }
        }
    }

    public void removeComment(int position) {
        video video = currentVideo.getValue();
        if (video != null) {
            ArrayList<comment> updatedComments = commentList.getValue();
            if (updatedComments != null && position < updatedComments.size()) {
                updatedComments.remove(position);
                video.setComments(updatedComments);
                videoRepository.updateVideo(video);
                commentList.setValue(updatedComments);
            }
        }
    }

    public void editComment(int position, String editedCommentText) {
        video video = currentVideo.getValue();
        if (video != null) {
            ArrayList<comment> updatedComments = commentList.getValue();
            if (updatedComments != null && position < updatedComments.size()) {
                comment editedComment = updatedComments.get(position);
                editedComment.setComment(editedCommentText);
                editedComment.setDate(getTheDate());
                editedComment.setEdited(true);
                video.setComments(updatedComments);
                videoRepository.updateVideo(video);
                commentList.setValue(updatedComments);
            }
        }
    }

    public void updateVideoDetails(String newName, Uri newThumbnailUri, Uri newVideoUri) {
        video video = currentVideo.getValue();
        if (video != null) {
            if (!newName.isEmpty()) {
                video.setVideo_name(newName);
            }
            if (newThumbnailUri != null) {
                video.setThumbnail(newThumbnailUri.toString());
            }
            if (newVideoUri != null) {
                video.setVideo_path(newVideoUri.toString());
            }
            videoRepository.updateVideo(video);
            currentVideo.setValue(video);
        }
    }

    public void deleteVideo() {
        video video = currentVideo.getValue();
        if (video != null) {
            videoRepository.deleteVideo(video);
        }
    }

    // Getters for LiveData
    public LiveData<video> getCurrentVideo() { return currentVideo; }
    public LiveData<user> getCurrentUser() { return currentUser; }
    public LiveData<user> getCurrentCreator() { return currentCreator; }
    public LiveData<Boolean> isLiked() { return isLiked; }
    public LiveData<Boolean> isDisliked() { return isDisliked; }
    public LiveData<Boolean> isSubscribed() { return isSubscribed; }
    public LiveData<ArrayList<comment>> getCommentList() { return commentList; }
}