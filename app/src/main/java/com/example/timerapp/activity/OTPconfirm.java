package com.example.timerapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.timerapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;


public class OTPconfirm extends AppCompatActivity {
    private TextView emailSend;
    private EditText otp;
    Button btnSendAgain;
    //true moi khi qua 60s
    private boolean resendEnable =false;
    private int resendTime=60;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_confirm);

        btnSendAgain=findViewById(R.id.btnSendAgain);
        emailSend=findViewById(R.id.txtEmail);
        otp=findViewById(R.id.edtOTP);

        //Lay email tu man hinh dang ky
        final String email=getIntent().getStringExtra("email");

        //Hien thi ten email
        emailSend.setText(email);


        btnSendAgain=findViewById(R.id.btnSendAgain);
        startCountTimer();
        btnSendAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resendEnable){
                    //code

                    //sau do dem nguoc lai
                    startCountTimer();
                }
            }
        });
    }

    private void startCountTimer(){
        resendEnable=false;
        btnSendAgain.setTextColor(Color.parseColor("#E0C6C6"));

        new CountDownTimer(resendTime * 200, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnSendAgain.setText("Gửi lại mã sau ("+(millisUntilFinished / 60)+")");
            }

            @Override
            public void onFinish() {
                resendEnable=true;
                btnSendAgain.setText("Gửi lại mã OTP");
                btnSendAgain.setTextColor(Color.parseColor("#355E3B"));
            }
        }.start();
    }
}
