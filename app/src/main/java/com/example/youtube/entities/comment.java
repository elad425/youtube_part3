package com.example.youtube.entities;

public class comment {
    private String comment;
    private int userId;
    private String date;
    private boolean edited;
    public comment(String comment, int userId, String date) {
        this.comment = comment;
        this.userId = userId;
        this.date = date;
        this.edited = false;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }
}
