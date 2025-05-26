package com.example.timerapp.model;

import java.util.ArrayList;
import java.util.List;

public class taskModel {
    boolean success;
    String message;
    List<Task> result;

    public taskModel(boolean success, String message, List<Task> result) {
        this.success = success;
        this.message = message;
        this.result = result;
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

    public List<Task> getResult() {

        return result;
    }

    public void setResult(List<Task> result) {
        this.result = result;
    }


}
