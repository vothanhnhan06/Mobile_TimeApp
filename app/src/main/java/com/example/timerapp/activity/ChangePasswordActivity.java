package com.example.timerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.AlertDialog;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.example.timerapp.R;

public class ChangePasswordActivity extends AppCompatActivity {
    Button btnChange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        btnChange = findViewById(R.id.btnChange);

        btnChange.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_notification_success, null);
            builder.setView(dialogView);
            builder.setCancelable(false); // Không cho bấm ngoài để tắt

            AlertDialog dialog = builder.create();
            dialog.show();

            new Handler().postDelayed(() -> {
                dialog.dismiss();
                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }, 2500);
        });
    }
}
