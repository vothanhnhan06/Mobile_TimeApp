package com.example.timerapp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.timerapp.Interface.SearchableFragment;
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
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        // Load username
        if (Utils.user_current != null) {
            txtUserName.setText(Utils.user_current.getUsername());
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
        }

        // Load HomeFragment mặc định
        if (savedInstanceState == null) {
            currentFragment = new HomeActivity();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, currentFragment)
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
                searchBar.setText(""); // Xóa truy vấn
                if (currentFragment instanceof SearchableFragment) {
                    ((SearchableFragment) currentFragment).filterTasks(""); // Khôi phục danh sách gốc
                }
            } else {
                searchBar.setVisibility(View.VISIBLE);
                searchBar.requestFocus();
                // Mở bàn phím ảo
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchBar, InputMethodManager.SHOW_IMPLICIT);
            }
            isSearchVisible = !isSearchVisible;
        });

        // Lắng nghe văn bản nhập vào searchBar
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (currentFragment instanceof SearchableFragment) {
                    ((SearchableFragment) currentFragment).filterTasks(s.toString());
                }
            }
        });

        // Xử lý nút add task
        imgAdd.setOnClickListener(v -> showAddTaskDialog());
    }

    private void selectFragment(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_home) {
            currentFragment = new HomeActivity();
            headerLayout.setVisibility(View.VISIBLE);
        } else if (id == R.id.menu_library) {
            currentFragment = new LibraryActivity();
            headerLayout.setVisibility(View.VISIBLE);
        } else if (id == R.id.menu_favourite) {
            currentFragment = new FavoriteActivity();
            headerLayout.setVisibility(View.VISIBLE);
        } else if (id == R.id.menu_personal) {
            currentFragment = new ProfileActivity();
            headerLayout.setVisibility(View.GONE);
        }

        if (currentFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, currentFragment)
                    .commit();
        }

        // Xóa truy vấn tìm kiếm khi chuyển Fragment
        searchBar.setText("");
        searchBar.setVisibility(View.GONE);
        isSearchVisible = false;
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

    private void init() {
        headerLayout = findViewById(R.id.headerLayout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        searchBar = findViewById(R.id.searchBar);
        imgAdd = headerLayout.findViewById(R.id.imgAdd);
        imgSearch = findViewById(R.id.imgSearch);
        txtUserName = findViewById(R.id.txtUserName);
    }
}