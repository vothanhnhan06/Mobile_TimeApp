package com.example.timerapp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.text.InputType;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.timerapp.R;
import com.example.timerapp.retrofit.ApiTimeApp;
import com.example.timerapp.retrofit.RetrofitClient;
import com.example.timerapp.utils.Utils;

import java.net.NetworkInterface;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    private TextView txtForgot;
    private TextView txtRegister;
    private EditText edtEmail, edtPassword;
    private boolean isPasswordVisible=false;
    ApiTimeApp apiTimeApp;
    CompositeDisposable compositeDisposable=new CompositeDisposable();


    RequestQueue requestQueue;
    AppCompatButton txtLogin;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        //icon eye password
        // Thiết lập sự kiện chạm
        edtPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP){
                    //Kiem tra xem cham vao vung drawableEnd khong
                    if(event.getRawX()>= (edtPassword.getRight()-edtPassword.getCompoundDrawables()[2].getBounds().width())){
                        togglePasswordVisibility();
                        return true;
                    }
                }
                return false;
            }
        });

        //Forgot password

        txtForgot.setPaintFlags(txtForgot.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang ForgotPasswordActivity
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Register
        txtRegister.setPaintFlags(txtRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        //Login


        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInput()){
                    loginUser();
                }
            }
        });
    }

    private void loginUser() {
        String str_email= edtEmail.getText().toString();
        String str_pass=edtPassword.getText().toString();
        compositeDisposable.add(apiTimeApp.login(str_email,str_pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                Paper.book().write("email",str_email);
                                Paper.book().write("pass",str_pass);
                                Utils.user_current=userModel.getResult().get(0);
                                Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }, throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private boolean checkInput() {
        String email= edtEmail.getText().toString();
        String pass=edtPassword.getText().toString();
        if (email.isEmpty()){
            edtEmail.setBackgroundResource(R.drawable.red_edittex);
            edtEmail.requestFocus();
            Toast.makeText(LoginActivity.this, "Nhập email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pass.isEmpty()){
            edtPassword.setBackgroundResource(R.drawable.red_edittex);
            edtPassword.requestFocus();
            Toast.makeText(LoginActivity.this, "Nhập pass", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Ẩn mật khẩu
            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            edtPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_close_layout, 0);
        } else {
            // Hiện mật khẩu
            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            edtPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_open_layout, 0);
        }
        isPasswordVisible = !isPasswordVisible;
        // Di chuyển con trỏ đến cuối văn bản
        edtPassword.setSelection(edtPassword.getText().length());
    }

    private void initView(){
        Paper.init(this);
        apiTimeApp= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiTimeApp.class);
        edtPassword=findViewById(R.id.edtPassword);
        txtRegister = findViewById(R.id.txtRegister);
        txtLogin = findViewById(R.id.txtLogin);
        edtEmail=findViewById(R.id.edtEmail);
        txtForgot = findViewById(R.id.txtForgotPassword);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(Utils.user_current.getEmail()!=null && Utils.user_current.getPass()!=null){
            edtEmail.setText(Utils.user_current.getEmail());
            edtPassword.setText(Utils.user_current.getPass());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
