package com.example.timerapp.model;

public class User {
    String email;
    String pass;
    String usernam;

    public User(String email, String pass, String usernam) {
        this.email = email;
        this.pass = pass;
        this.usernam = usernam;
    }

    public User() {

    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public String getUsernam() {

        return usernam;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setUsernam(String usernam) {
        this.usernam = usernam;
    }
}
