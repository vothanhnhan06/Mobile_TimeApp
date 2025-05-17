package com.example.timerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timerapp.R;
import com.example.timerapp.retrofit.ApiTimeApp;
import com.example.timerapp.retrofit.RetrofitClient;
import com.example.timerapp.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WelcomeActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    ApiTimeApp apiTimeApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        apiTimeApp = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiTimeApp.class);

        if(isConnected(this)){
            Toast.makeText(getApplicationContext(),"OK", Toast.LENGTH_SHORT).show();
            getUser();
        }
        else{
            Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT).show();
        }
        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi != null && wifi.isConnected()) || (mobile!=null && mobile.isConnected())){
            return true;
        }else{
            return false;
        }

    }

    private void getUser(){

    }
}