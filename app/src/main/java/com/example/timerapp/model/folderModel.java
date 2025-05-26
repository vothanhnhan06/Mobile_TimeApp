package com.example.timerapp.model;

import java.util.List;

public class folderModel {
    boolean success;
    String message;
    List<Folder> result;

    public folderModel(boolean success, List<Folder> result,String message) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

    public folderModel(){

    }

    public boolean isSuccess(){
        return success;
    }

    public void setSuccess(boolean success){
        this.success=success;
    }

    public String getMessage(){
        return message;
    }
    public  void setMessage(String message){
        this.message=message;
    }
    public List<Folder> getResult(){
        return result;
    }
    public void setResult(List<Folder> result){
        this.result=result;
    }
}
