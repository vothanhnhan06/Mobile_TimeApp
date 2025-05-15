package com.example.timerapp.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timerapp.R;

public class ProfileActivity extends AppCompatActivity {

    ImageView imgBack;
    TextView tvUsername;
    EditText edtEmail, edtUsername, edtCurrentPassword, edtNewPassword;
    Button btnSaveChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Ánh xạ
        imgBack = findViewById(R.id.imgBack);
        tvUsername = findViewById(R.id.tvUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtUsername = findViewById(R.id.edtUsername);
        edtCurrentPassword = findViewById(R.id.edtCurrentPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        // Thoát
        imgBack.setOnClickListener(v -> finish());

        // Gán dữ liệu mẫu
        tvUsername.setText("Amy Young");
        edtEmail.setText("AmyYoung@gmail.com");
        edtUsername.setText("Amy Young");

        // Xử lý lưu
        btnSaveChanges.setOnClickListener(v -> {
            // TODO: xử lý logic lưu thay đổi ở đây
            Toast.makeText(this, "Thay đổi đã được lưu", Toast.LENGTH_SHORT).show();
        });
    }
}
