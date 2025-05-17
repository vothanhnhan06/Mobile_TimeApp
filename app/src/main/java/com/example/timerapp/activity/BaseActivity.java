//package com.example.timerapp.activity;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.Layout;
//import android.text.SpannableString;
//import android.text.style.AlignmentSpan;
//import android.text.style.ForegroundColorSpan;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.view.GravityCompat;
//import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import android.app.AlertDialog;
//import android.widget.Button;
//import android.widget.TextView;
//import android.view.Gravity;
//import android.view.ViewGroup;
//import android.widget.Toolbar;
//
//
//import com.example.timerapp.R;
//import com.example.timerapp.Task;
//import com.example.timerapp.TaskAdapter;
//import com.google.android.material.navigation.NavigationView;
//
//import java.util.ArrayList;
//import java.util.List;
//public abstract class BaseActivity extends AppCompatActivity {
//    protected EditText searchBar;
//    protected boolean isSearchVisible = false;
//    protected ImageView imgAdd;
//    protected ImageView imgClose;
//    protected RecyclerView recyclerView;
//    protected TaskAdapter taskAdapter;
//    protected List<Task> taskList;
//    protected DrawerLayout drawerLayout;
//    protected ImageView imgMenu;
//    protected TextView tvLogout;
//    protected NavigationView bottomNavigationView;
//    protected Toolbar toolbar;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(getLayoutResourceId());
//
//        initCommonViews();
//        setupRecyclerView();
//        setupSearchBar();
//        setupAddTaskDialog();
//        setupDrawerNavigation();
//    }
//
//    protected abstract int getLayoutResourceId();
//
//    protected void initCommonViews() {
//        recyclerView = findViewById(R.id.recyclerViewTasks);
//        drawerLayout = findViewById(R.id.drawerLayout);
//        imgMenu = findViewById(R.id.imgMenu);
//        imgAdd = findViewById(R.id.imgAdd);
//        searchBar = findViewById(R.id.searchBar);
//        tvLogout = findViewById(R.id.tvLogout);
//        navigationView = findViewById(R.id.navigationView);
//
//        imgMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
//        navigationView = findViewById(R.id.navigationView);
//        navigationView.setNavigationItemSelectedListener(item -> {
//            int id = item.getItemId();
//            if (id == R.id.menu_home) {
//                navigateTo(HomeActivity.class);
//            } else if (id == R.id.menu_library) {
//                navigateTo(LibraryActivity.class);
//            } else if (id == R.id.menu_favorite) {
//                navigateTo(FavouriteActivity.class);
//            } else if (id == R.id.menu_profile) {
//                navigateTo(ProfileActivity.class); // đây là activity bạn đã làm mới
//            }
//            drawerLayout.closeDrawer(GravityCompat.START);
//            return true;
//        });
//
//        tvLogout.setOnClickListener(v -> confirmLogout());
//    }
//
//    protected void setupRecyclerView() {
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        taskList = new ArrayList<>();
//        taskAdapter = new TaskAdapter(this, taskList);
//        recyclerView.setAdapter(taskAdapter);
//    }
//
//    protected void setupSearchBar() {
//        ImageView imgSearch = findViewById(R.id.imgSearch);
//        imgSearch.setOnClickListener(v -> {
//            if (isSearchVisible) {
//                searchBar.setVisibility(View.GONE);
//            } else {
//                searchBar.setVisibility(View.VISIBLE);
//                searchBar.requestFocus();
//            }
//            isSearchVisible = !isSearchVisible;
//        });
//    }
//
//    protected void setupAddTaskDialog() {
//        imgAdd.setOnClickListener(v -> {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
//            builder.setView(dialogView);
//            AlertDialog dialog = builder.create();
//
//            Button btnSave = dialogView.findViewById(R.id.btnSave);
//            btnSave.setOnClickListener(view -> {
//                dialog.dismiss();
//
//                AlertDialog.Builder successBuilder = new AlertDialog.Builder(this);
//                View dialogNotification = getLayoutInflater().inflate(R.layout.dialog_notification_success, null);
//                successBuilder.setView(dialogNotification);
//                successBuilder.setCancelable(false);
//
//                AlertDialog dialogSave = successBuilder.create();
//                dialogSave.show();
//
//                TextView txtTitle = dialogNotification.findViewById(R.id.txtDialogTitle);
//                TextView txtMessage = dialogNotification.findViewById(R.id.txtDialogMessage);
//                txtTitle.setText("Thông báo");
//                txtMessage.setText("Đã thêm vào thư viện của bạn!");
//
//                new Handler().postDelayed(dialogSave::dismiss, 2500);
//            });
//
//            imgClose = dialogView.findViewById(R.id.imgClose);
//            imgClose.setOnClickListener(view -> dialog.dismiss());
//
//            dialog.show();
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog.getWindow().setGravity(Gravity.BOTTOM);
//            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        });
//    }
//
//    protected void setupDrawerNavigation() {
//        // Already handled in initCommonViews()
//    }
//
//    protected void navigateTo(Class<?> activityClass) {
//        Intent intent = new Intent(this, activityClass);
//        startActivity(intent);
//    }
//
//    protected void confirmLogout() {
//        SpannableString title = new SpannableString("Xác nhận đăng xuất");
//        title.setSpan(new ForegroundColorSpan(Color.parseColor("#355E3B")), 0, title.length(), 0);
//        title.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length(), 0);
//
//        SpannableString message = new SpannableString("Bạn có chắc chắn muốn đăng xuất không?");
//        message.setSpan(new ForegroundColorSpan(Color.parseColor("#355E3B")), 0, message.length(), 0);
//
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle(title)
//                .setMessage(message)
//                .setPositiveButton("Đăng xuất", (dialogInterface, which) -> {
//                    Intent intent = new Intent(this, LoginActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();
//                })
//                .setNegativeButton("Hủy", null)
//                .show();
//
//        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//
//        positiveButton.setAllCaps(false);
//        positiveButton.setTextColor(Color.RED);
//        negativeButton.setAllCaps(false);
//        negativeButton.setTextColor(Color.GRAY);
//    }
//}
//
