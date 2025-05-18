package com.example.timerapp.model;

import java.util.List;

public class UserModel {
    boolean success;
    String message;
    List<User> result;

    public UserModel(boolean success, List<User> result,String message) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

    public UserModel(){

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
    public List<User> getResult(){
        return result;
    }
    public void setResult(List<User> result){
        this.result=result;
    }
}
