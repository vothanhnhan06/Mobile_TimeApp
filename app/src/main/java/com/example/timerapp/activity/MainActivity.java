package com.example.timerapp.activity;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timerapp.R;
import com.example.timerapp.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private View headerLayout;
    private EditText searchBar;
    private ImageView imgAdd, imgSearch;
    private TextView txtUserName;
    private boolean isSearchVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();


        //Load username
        if (Utils.user_current != null) {
            String userName = Utils.user_current.getUsername();
            txtUserName.setText(userName);
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
        }

        // Load HomeFragment mặc định
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new HomeActivity())
                    .commit();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            selectFragment(item);
            return true;
        });


        // Xử lý bật tắt search bar
        imgSearch.setOnClickListener(v -> {
            if (isSearchVisible) {
                searchBar.setVisibility(View.GONE);
            } else {
                searchBar.setVisibility(View.VISIBLE);
                searchBar.requestFocus();
            }
            isSearchVisible = !isSearchVisible;
        });

        // Xử lý nút add task
        imgAdd.setOnClickListener(v -> {

            showAddTaskDialog();
        });
    }

    private void selectFragment(MenuItem item) {
        Fragment selectedFragment = null;
        int id = item.getItemId();

        if (id == R.id.menu_home) {
            selectedFragment = new HomeActivity();
            headerLayout.setVisibility(View.VISIBLE);
        } else if (id == R.id.menu_library) {
            selectedFragment = new LibraryActivity();
            headerLayout.setVisibility(View.VISIBLE);
        } else if (id == R.id.menu_favourite) {
            selectedFragment = new FavoriteActivity();

            headerLayout.setVisibility(View.VISIBLE);
        } else if (id == R.id.menu_personal) {
            selectedFragment = new ProfileActivity();
            headerLayout.setVisibility(View.GONE);
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, selectedFragment)
                    .commit();
        }
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        Button btnSave = dialogView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(view -> {
            dialog.dismiss();

            AlertDialog.Builder successBuilder = new AlertDialog.Builder(this);
            View dialogNotification = LayoutInflater.from(this).inflate(R.layout.dialog_notification_success, null);
            successBuilder.setView(dialogNotification);
            successBuilder.setCancelable(false);

            AlertDialog dialogSave = successBuilder.create();
            dialogSave.show();

            TextView txtTitle = dialogNotification.findViewById(R.id.txtDialogTitle);
            TextView txtMessage = dialogNotification.findViewById(R.id.txtDialogMessage);
            txtTitle.setText("Thông báo");
            txtMessage.setText("Đã thêm vào thư viện của bạn!");

            new Handler().postDelayed(dialogSave::dismiss, 2500);
        });

        ImageView imgClose = dialogView.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void init(){
        headerLayout = findViewById(R.id.headerLayout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        searchBar = findViewById(R.id.searchBar);
        imgAdd = headerLayout.findViewById(R.id.imgAdd);
        imgSearch = findViewById(R.id.imgSearch);
        txtUserName = findViewById(R.id.txtUserName);
    }
}


