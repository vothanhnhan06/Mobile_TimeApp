package com.example.timerapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.timerapp.R;
import com.example.timerapp.retrofit.ApiTimeApp;
import com.example.timerapp.retrofit.RetrofitClient;
import com.example.timerapp.utils.Utils;

import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class OTPconfirmReset extends AppCompatActivity {
    private TextView emailSend;
    private EditText otp;
    Button btnSendAgain, btnConfirm;
    //true moi khi qua 60s
    private boolean resendEnable =false;
    private int resendTime=60;

    CompositeDisposable compositeDisposable=new CompositeDisposable();
    ApiTimeApp apiTimeApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_confirm_reset);
        apiTimeApp= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiTimeApp.class);

        btnSendAgain=findViewById(R.id.btnSendAgain);
        emailSend=findViewById(R.id.txtEmail);
        //Lay email tu man hinh dang ky
        String str_email=getIntent().getStringExtra("email");


        //Hien thi ten email
        emailSend.setText(str_email);

        //confirm
        btnConfirm=findViewById(R.id.btnComfirm);
        otp=findViewById(R.id.edtOTP);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_otp=otp.getText().toString().trim();
                checkCode(str_email,str_otp);
            }
        });

        startCountTimer();
        btnSendAgain=findViewById(R.id.btnSendAgain);
        btnSendAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resendEnable) {
                    sendEmail(str_email);
                } else {
                    Toast.makeText(getApplicationContext(), "Vui lòng đợi để gửi lại mã", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkCode(String email,String str_otp) {
        compositeDisposable.add(apiTimeApp.checkOTP(email,str_otp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                Toast.makeText(getApplicationContext(),"Success" , Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(OTPconfirmReset.this, ChangePasswordActivity.class);
                                intent.putExtra("email",email);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                otp.setBackgroundResource(R.drawable.red_edittex);
                                otp.requestFocus();
                                Toast.makeText(getApplicationContext(),"Error"+userModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }, throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void startCountTimer() {
        resendEnable = false;
        btnSendAgain.setTextColor(Color.parseColor("#E0C6C6"));

        new CountDownTimer(resendTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnSendAgain.setText("Gửi lại mã sau (" + (millisUntilFinished / 1000) + ")");
            }

            @Override
            public void onFinish() {
                resendEnable = true;
                btnSendAgain.setText("Gửi lại mã OTP");
                btnSendAgain.setTextColor(Color.parseColor("#355E3B"));
            }
        }.start();
    }

    private void sendEmail(String str_email) {
        Random random = new Random();
        int code = random.nextInt(8999) + 1000;
        String codeStr = String.valueOf(code);

        compositeDisposable.add(apiTimeApp.sendEmail(str_email, codeStr)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()) {
                                Toast.makeText(getApplicationContext(), "Sended", Toast.LENGTH_SHORT).show();
                                startCountTimer();
                            } else {
                                Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }, throwable -> {
                            Toast.makeText(getApplicationContext(), "Error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                )
        );
    }

}
