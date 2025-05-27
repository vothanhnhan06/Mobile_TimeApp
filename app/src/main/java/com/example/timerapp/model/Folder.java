package com.example.timerapp.model;

public class Folder {
    private String name_folder;
    private int id;
    private String image_path;
    // Có thể thêm id, mô tả, số lượng task, ...


    public Folder(String name_folder, int id, String image_path) {
        this.name_folder = name_folder;
        this.id = id;
        this.image_path = image_path;
    }

    public String getName_folder() {
        return name_folder;
    }

    public void setName_folder(String name_folder) {
        this.name_folder = name_folder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    @Override
    public String toString() {
        return name_folder; // chỉ hiển thị tên trong Spinner
    }
}