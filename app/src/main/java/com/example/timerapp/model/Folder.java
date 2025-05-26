package com.example.timerapp.model;

public class Folder {
    private String folderName;
    // Có thể thêm id, mô tả, số lượng task, ...

    public Folder(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }

}