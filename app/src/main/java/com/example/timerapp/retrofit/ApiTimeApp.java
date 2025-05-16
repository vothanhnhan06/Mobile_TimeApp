package com.example.timerapp.retrofit;

import com.example.timerapp.model.UserModel;
import io.reactivex.rxjava3.core.Single; // Switch to Single for a single response
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiTimeApp {
    @FormUrlEncoded
    @POST("register.php")
    Single<UserModel> register(
            @Field("email") String email,
            @Field("username") String username,
            @Field("password") String pass
    );

    @FormUrlEncoded
    @POST("login.php")
    Single<UserModel> login(
            @Field("email") String email,
            @Field("password") String pass
    );

    @FormUrlEncoded
    @POST("sendEmail.php")
    Single<UserModel> sendEmail(
            @Field("email") String email,
            @Field("code") String code
    );

    @FormUrlEncoded
    @POST("checkOTP.php")
    Single<UserModel> checkOTP(
            @Field("email") String email,
            @Field("code") String code
    );
}