package com.example.timerapp.model;


public class Task {
    private String title;
    private int id;
    private int id_user;
    private String time;
    private boolean isFavorite; // trạng thái yêu thích
    private String image_path;

    public Task(String title, int id_user, String time) {
        this.title = title;
        this.id_user = id_user;
        this.time = time;
    }

    public Task(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
