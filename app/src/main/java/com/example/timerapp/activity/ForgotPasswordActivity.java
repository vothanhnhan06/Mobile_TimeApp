package com.example.timerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.timerapp.R;
import com.example.timerapp.retrofit.ApiTimeApp;
import com.example.timerapp.retrofit.RetrofitClient;
import com.example.timerapp.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextView txtBackToLogin;
    EditText edtEmailForgot;
    Button btnSendReset;
    int code;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    ApiTimeApp apiTimeApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        apiTimeApp= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiTimeApp.class);

        txtBackToLogin = findViewById(R.id.txtBackToLogin);
        txtBackToLogin.setPaintFlags(txtBackToLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSendReset = findViewById(R.id.btnSendReset);
        edtEmailForgot=findViewById(R.id.edtEmailForgot);
        btnSendReset.setOnClickListener(v -> {
            String email=edtEmailForgot.getText().toString().trim();
            sendVerifyEmail(email);
        });
    }

    private void sendVerifyEmail(String email) {
        Random random = new Random();
        code = random.nextInt(8999) + 1000;
        String codeStr = String.valueOf(code);
        compositeDisposable.add(apiTimeApp.sendEmail(email,codeStr)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ForgotPasswordActivity.this, OTPconfirmReset.class);
                                intent.putExtra("email",email);
                                intent.putExtra("code",codeStr);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),userModel.getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        }, throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }
}