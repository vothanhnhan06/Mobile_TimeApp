package com.example.timerapp.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timerapp.R;

public class ConfirmationActivity extends AppCompatActivity {
    private Button btnChangePassword;
    TextView txtBackToEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        txtBackToEmail = findViewById(R.id.txtBackToEmail);
        txtBackToEmail.setPaintFlags(txtBackToEmail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtBackToEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmationActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(ConfirmationActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            finish();
        });

    }
}
