package com.example.timerapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timerapp.R;
import com.example.timerapp.retrofit.ApiTimeApp;
import com.example.timerapp.retrofit.RetrofitClient;
import com.example.timerapp.utils.Utils;

import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private TextView txtLogin;
    private EditText email, pass, username, pass2;
    private boolean isPasswordVisible=false;
    private boolean isPasswordVisible2=false;
    ApiTimeApp apiTimeApp;
    CompositeDisposable compositeDisposable=new CompositeDisposable();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

        pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP){
                    //Kiem tra xem cham vao vung drawableEnd khong
                    if(event.getRawX()>= (pass.getRight()-pass.getCompoundDrawables()[2].getBounds().width())){
                        togglePasswordVisibility();
                        return true;
                    }
                }
                return false;
            }
        });

        pass2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP){
                    //Kiem tra xem cham vao vung drawableEnd khong
                    if(event.getRawX()>= (pass2.getRight()-pass2.getCompoundDrawables()[2].getBounds().width())){
                        togglePasswordVisibilityPass2();
                        return true;
                    }
                }
                return false;
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput() && checkPass()) {
                    registerUser();
                }
            }
        });


        txtLogin.setPaintFlags(txtLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
    private void sendEmail(String str_email){
        Random random = new Random();
        int code = random.nextInt(8999) + 1000;
        String codeStr = String.valueOf(code);

        compositeDisposable.add(apiTimeApp.sendEmail(str_email,codeStr)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                Intent intent = new Intent(RegisterActivity.this, OTPconfirmRegister.class);
                                intent.putExtra("email", str_email);
                                intent.putExtra("code", codeStr);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),userModel.getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            // Xử lý khi API thất bại
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }

    private void registerUser() {
        String str_email = email.getText().toString().trim();
        String str_username = username.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();

        compositeDisposable.add(apiTimeApp.register(str_email,str_username,str_pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                sendEmail(str_email);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),userModel.getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            // Xử lý khi API thất bại
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }

    private boolean checkPass() {
        String passInput = pass.getText().toString();
        String pass2Input = pass2.getText().toString();
        if (!passInput.equals(pass2Input)) {
            pass2.setBackgroundResource(R.drawable.red_edittex);
            Toast.makeText(RegisterActivity.this, "Mật khẩu không khớp, vui lòng nhập lại!", Toast.LENGTH_SHORT).show();
            pass2.requestFocus();
            return false;
        }
        return true;
    }


    private void initView() {
        apiTimeApp= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiTimeApp.class);
        email=findViewById(R.id.edtEmail);
        pass=findViewById(R.id.edtPassword);
        username=findViewById(R.id.edtUsername);
        pass2=findViewById(R.id.edtPasswordAgain);
        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Ẩn mật khẩu
            pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            pass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_close_layout, 0);
        } else {
            // Hiện mật khẩu
            pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            pass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_open_layout, 0);
        }
        isPasswordVisible = !isPasswordVisible;
        // Di chuyển con trỏ đến cuối văn bản
        pass.setSelection(pass.getText().length());
    }

    private void togglePasswordVisibilityPass2() {
        if (isPasswordVisible2) {
            // Ẩn mật khẩu
            pass2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            pass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_close_layout, 0);
        } else {
            // Hiện mật khẩu
            pass2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            pass2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_open_layout, 0);
        }
        isPasswordVisible2 = !isPasswordVisible2;
        // Di chuyển con trỏ đến cuối văn bản
        pass2.setSelection(pass2.getText().length());
    }

    private boolean checkInput() {
        String emailInput=email.getText().toString();
        String usernameInput=username.getText().toString();
        String passInput=pass.getText().toString();
        String pass2Input=pass2.getText().toString();

        // Kiểm tra từng EditText
        if (emailInput.isEmpty()|| !isValidEmail(emailInput)) {
            email.setBackgroundResource(R.drawable.red_edittex);
            email.requestFocus();
            return false;
        }else {
            email.setBackgroundResource(R.drawable.green_edittex);
        }
        if (usernameInput.isEmpty()) {
            username.setBackgroundResource(R.drawable.red_edittex);
            username.requestFocus();
            return false;
        }else {
            username.setBackgroundResource(R.drawable.green_edittex);
        }
        if (passInput.isEmpty()) {
            pass.setBackgroundResource(R.drawable.red_edittex);
            pass.requestFocus();
            return false;
        }else {
            pass.setBackgroundResource(R.drawable.green_edittex);
        }
        if (pass2Input.isEmpty()) {
            pass2.setBackgroundResource(R.drawable.red_edittex);
            pass2.requestFocus();
            return false;
        }else {
            pass2.setBackgroundResource(R.drawable.green_edittex);
        }
        return true;
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}