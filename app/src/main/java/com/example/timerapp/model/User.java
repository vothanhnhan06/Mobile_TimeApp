package com.example.timerapp.model;

public class User {
    String email;
    String pass;
    String name;
    String image_path;
    String user_id;

    public User(String email, String pass, String name, String image_path, String user_id) {
        this.email = email;
        this.pass = pass;
        this.name = name;
        this.image_path=image_path;
        this.user_id=user_id;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public User() {

    }

    public String getEmail() {
        return email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPass() {
        return pass;
    }

    public String getUsername() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setUsernam(String username) {
        this.name = username;
    }
}
