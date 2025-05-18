package com.example.timerapp.activity;

import android.content.Context;
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
import com.example.timerapp.retrofit.ApiTimeApp;
import com.example.timerapp.retrofit.RetrofitClient;
import com.example.timerapp.utils.Utils;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProfileActivity extends Fragment {

    private TextView tvUsernameHeader;
    private EditText edtEmail, edtUsername, edtCurrentPassword, edtNewPassword;
    private Button btnSaveChanges;
    private LinearLayout btnLogout;
    ApiTimeApp apiTimeApp;
    CompositeDisposable compositeDisposable=new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        apiTimeApp= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiTimeApp.class);
        // Ánh xạ view

        tvUsernameHeader = view.findViewById(R.id.tvUserNameHeader);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtUsername = view.findViewById(R.id.edtUsername);
        edtCurrentPassword = view.findViewById(R.id.edtCurrentPassword);
        edtNewPassword = view.findViewById(R.id.edtNewPassword);
        btnSaveChanges = view.findViewById(R.id.btnSaveChanges);
        btnLogout = view.findViewById(R.id.btnLogout);

        // Gán dữ liệu mẫu
        tvUsernameHeader.setText(Utils.user_current.getUsername());
        edtEmail.setText(Utils.user_current.getEmail());
        edtUsername.setText(Utils.user_current.getUsername());

        // Xử lý khi edtUsername mất focus
        edtUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String input = edtUsername.getText().toString().trim();
                if (input.isEmpty() && Utils.user_current != null) {
                    String originalUsername = Utils.user_current.getUsername() != null ? Utils.user_current.getUsername() : "Người dùng";
                    edtUsername.setText(originalUsername);
                }
            }
        });

        // Xử lý lưu
        btnSaveChanges.setOnClickListener(v -> {
            resetPass();
        });

        //Xử  lý đăng xuất
        btnLogout.setOnClickListener(v -> showLogoutConfirmationDialog());
        return view;
    }

    private void resetPass() {
        String str_email= edtEmail.getText().toString();
        String str_username=edtUsername.getText().toString().trim();
        String str_pass=edtNewPassword.getText().toString();
        String str_oldpass=edtCurrentPassword.getText().toString();
        compositeDisposable.add(apiTimeApp.resetPassProfile(str_email,str_pass,str_oldpass,str_username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                Toast.makeText(getContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                                tvUsernameHeader.setText(str_username);

                                edtNewPassword.getText().clear();
                                edtCurrentPassword.getText().clear();
                            }
                            else{
                                Toast.makeText(getContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                                edtNewPassword.getText().clear();
                                edtCurrentPassword.getText().clear();

                                edtNewPassword.requestFocus();
                            }
                        }, throwable -> {
                            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
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
