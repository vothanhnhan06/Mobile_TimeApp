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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private TextView txtLogin;
    private EditText email, pass, username, pass2;
    private boolean isPasswordVisible=false;
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

    private void registerUser() {
        String str_email = email.getText().toString().trim();
        String str_username = username.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();

        compositeDisposable.add(apiTimeApp.register(str_email, str_username, str_pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            // Xử lý khi API thành công
                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                            // Chuyển sang OTPconfirm Activity chỉ khi API thành công
                            Intent intent = new Intent(RegisterActivity.this, OTPconfirmRegister.class);
                            intent.putExtra("email", str_email);
                            startActivity(intent);
                            finish();
                        },
                        throwable -> {
                            // Xử lý khi API thất bại
                            Toast.makeText(RegisterActivity.this, "Lỗi đăng ký: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("RegisterActivity", "Lỗi API: ", throwable);
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

    /*@Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }*/

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}