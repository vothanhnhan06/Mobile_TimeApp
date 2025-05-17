package com.example.timerapp.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.timerapp.R;
import com.example.timerapp.activity.LoginActivity;

public class ProfileActivity extends Fragment {

    private TextView tvUsername;
    private EditText edtEmail, edtUsername, edtCurrentPassword, edtNewPassword;
    private Button btnSaveChanges;
    private LinearLayout btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        // Ánh xạ view
        tvUsername = view.findViewById(R.id.tvUsername);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtUsername = view.findViewById(R.id.edtUsername);
        edtCurrentPassword = view.findViewById(R.id.edtCurrentPassword);
        edtNewPassword = view.findViewById(R.id.edtNewPassword);
        btnSaveChanges = view.findViewById(R.id.btnSaveChanges);
        btnLogout = view.findViewById(R.id.btnLogout);

        // Gán dữ liệu mẫu
        tvUsername.setText("Amy Young");
        edtEmail.setText("AmyYoung@gmail.com");
        edtUsername.setText("Amy Young");

        // Xử lý lưu
        btnSaveChanges.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Thay đổi đã được lưu", Toast.LENGTH_SHORT).show();
        });

        //Xử  lý đăng xuất
        btnLogout.setOnClickListener(v -> showLogoutConfirmationDialog());
        return view;
    }
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_custom, null);
        builder.setView(dialogView);

        builder.setPositiveButton("Có", (dialog, which) -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        builder.setNegativeButton("Không", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Tùy chỉnh nút sau khi dialog hiển thị
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        if (positiveButton != null) {
            positiveButton.setTextColor(Color.parseColor("#355E3B"));
            positiveButton.setAllCaps(false);  //
        }

        if (negativeButton != null) {
            negativeButton.setTextColor(Color.parseColor("#FF0000"));
            negativeButton.setAllCaps(false);  //
        }
    }



}
