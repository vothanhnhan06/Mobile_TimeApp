package com.example.timerapp;


public class Task {
    private String title;
    private String time;
    private boolean isFavorite; // trạng thái yêu thích

    public Task(String title, String time, boolean isFavorite) {
        this.title = title;
        this.time = time;
        this.isFavorite = isFavorite;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
