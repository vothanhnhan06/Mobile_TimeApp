package com.example.timerapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.timerapp.R;

public class ProfileFragment extends Fragment {

    private TextView tvUsername;
    private EditText edtEmail, edtUsername, edtCurrentPassword, edtNewPassword;
    private Button btnSaveChanges;

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


        // Gán dữ liệu mẫu
        tvUsername.setText("Amy Young");
        edtEmail.setText("AmyYoung@gmail.com");
        edtUsername.setText("Amy Young");

        // Xử lý lưu
        btnSaveChanges.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Thay đổi đã được lưu", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
