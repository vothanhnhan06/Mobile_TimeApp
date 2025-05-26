package com.example.timerapp.retrofit;

import com.example.timerapp.model.UserModel;
import com.example.timerapp.model.folderModel;
import com.example.timerapp.model.taskModel;

import io.reactivex.rxjava3.core.Observable;
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

    @FormUrlEncoded
    @POST("resetPass.php")
    Single<UserModel> resetPass(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("getTask.php")
    Single<taskModel> getTask(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("getTaskFavorite.php")
    Single<taskModel> getTaskFavorite(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("deleteTask.php")
    Single<taskModel> deleteTask(
            @Field("task_id") int task_id
    );

    @FormUrlEncoded
    @POST("resetPassProfile.php")
    Single<UserModel> resetPassProfile(
            @Field("email") String email,
            @Field("password") String password,
            @Field("old_password") String old_password,
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("updateFavoriteTask.php")
    Observable<taskModel> updateFavorite(
            @Field("task_id") int taskId,
            @Field("is_favorite") int isFavorite
    );

    @FormUrlEncoded
    @POST("getFolder.php")
    Single<folderModel> getFolder(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("getTaskFolder.php")
    Single<taskModel> getTaskFolder(
            @Field("id_folder") int id_folder
    );

    @FormUrlEncoded
    @POST("updateTask.php")
    Single<taskModel> updateTask(
            @Field("task_id") int task_id,
            @Field("time") String time
    );
}