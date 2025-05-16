package com.example.timerapp.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.timerapp.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextView txtBackToLogin;
    EditText edtEmailForgot;
    Button btnSendReset;
    int code;
    String BASE_URL="https://192.168.1.7/timeapp/sendEmail.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        txtBackToLogin = findViewById(R.id.txtBackToLogin);
        txtBackToLogin.setPaintFlags(txtBackToLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSendReset = findViewById(R.id.btnSendReset);
        edtEmailForgot=findViewById(R.id.edtEmailForgot);
        btnSendReset.setOnClickListener(v -> {
            String email=edtEmailForgot.getText().toString().trim();

            //sendVerifyEmail(email);
            Intent intent = new Intent(ForgotPasswordActivity.this, OTPconfirmReset.class);
            intent.putExtra("email", email);
            intent.putExtra("code", code);
            startActivity(intent);
            finish();
        });
    }

    private void sendVerifyEmail(String email) {
        Random random = new Random();
        code = random.nextInt(8999) + 1000;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                response -> {
                    // Check server response
                    if (response.contains("success") || response.contains("sent")) {
                        Toast.makeText(ForgotPasswordActivity.this, "Verification code sent", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgotPasswordActivity.this, OTPconfirmReset.class);
                        intent.putExtra("email", email);
                        intent.putExtra("code", code);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(ForgotPasswordActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("code", String.valueOf(code));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}