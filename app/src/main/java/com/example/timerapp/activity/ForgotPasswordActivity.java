package com.example.timerapp.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timerapp.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextView txtBackToLogin;
    Button btnSendReset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

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
        btnSendReset.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, ConfirmationActivity.class);
            startActivity(intent);
            finish();
        });
    }
}