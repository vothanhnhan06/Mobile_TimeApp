package com.example.timerapp.model;

import java.util.List;

public class taskModel {
    boolean success;
    String message;
    List<Task> resutl;

    public taskModel(boolean success, String message, List<Task> resutl) {
        this.success = success;
        this.message = message;
        this.resutl = resutl;
    }

    public taskModel(){

    }
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Task> getResutl() {
        return resutl;
    }

    public void setResutl(List<Task> resutl) {
        this.resutl = resutl;
    }
}
