package com.example.timerapp.activity;

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


public class OTPconfirmReset extends AppCompatActivity {
    private TextView emailSend;
    private EditText otp;
    Button btnSendAgain, btnConfirm;
    //true moi khi qua 60s
    private boolean resendEnable =false;
    private int resendTime=60;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_confirm_reset);

        btnSendAgain=findViewById(R.id.btnSendAgain);
        emailSend=findViewById(R.id.txtEmail);
        otp=findViewById(R.id.edtOTP);

        //Lay email tu man hinh dang ky
        String email=getIntent().getStringExtra("email");


        //Hien thi ten email
        emailSend.setText(email);

        //confirm
        btnConfirm=findViewById(R.id.btnComfirm);
        otp=findViewById(R.id.edtOTP);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkCode()){

                }
            }
        });


        btnSendAgain=findViewById(R.id.btnSendAgain);
        startCountTimer();
        btnSendAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resendEnable){
                    //check OTP
                    //sau do dem nguoc lai
                    startCountTimer();
                }
            }
        });
    }

    private boolean checkCode() {
        String str_otp=otp.getText().toString().trim();
        String code=getIntent().getStringExtra("code");
        if(str_otp.equals(code)){
            Toast.makeText(getApplicationContext(),"Ma code dung!", Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            Toast.makeText(getApplicationContext(),"Ma code dung!", Toast.LENGTH_SHORT).show();
            return false;
        }
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
