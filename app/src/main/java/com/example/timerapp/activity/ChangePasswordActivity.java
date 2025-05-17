package com.example.timerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.app.AlertDialog;
import android.os.Handler;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timerapp.R;
import com.example.timerapp.retrofit.ApiTimeApp;
import com.example.timerapp.retrofit.RetrofitClient;
import com.example.timerapp.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChangePasswordActivity extends AppCompatActivity {
    Button btnChange;
    EditText edtPwd, edtPwdAgain;
    ApiTimeApp apiTimeApp;
    CompositeDisposable compositeDisposable=new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        apiTimeApp= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiTimeApp.class);


        btnChange = findViewById(R.id.btnChange);
        edtPwd=findViewById(R.id.edtPwd);
        edtPwdAgain=findViewById(R.id.edtPwdAgain);

        btnChange.setOnClickListener(v -> {
            String str_email=getIntent().getStringExtra("email");
            String str_pwd=edtPwd.getText().toString().trim();
            String str_pwdagain=edtPwdAgain.getText().toString().trim();
            if(checkPass(str_pwd,str_pwdagain)){
                changePassword(str_email,str_pwd);
            }
        });
    }

    private void changePassword(String str_email, String str_pass) {

        compositeDisposable.add(apiTimeApp.resetPass(str_email,str_pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()) {
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
                            }else{
                                Toast.makeText(ChangePasswordActivity.this, userModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            // Xử lý khi API thất bại
                            Toast.makeText(ChangePasswordActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                )
        );


    }

    private boolean checkPass(String pwd, String pwdagain) {
        if(!pwd.equals(pwdagain)){
            edtPwdAgain.setBackgroundResource(R.drawable.red_edittex);
            Toast.makeText(ChangePasswordActivity.this, "Mật khẩu không khớp, vui lòng nhập lại!", Toast.LENGTH_SHORT).show();
            edtPwdAgain.requestFocus();
            return false;
        }
        return true;
    }


}
