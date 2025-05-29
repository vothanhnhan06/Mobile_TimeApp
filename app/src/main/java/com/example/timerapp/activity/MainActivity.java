package com.example.timerapp.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timerapp.R;
import com.example.timerapp.model.Folder;
import com.example.timerapp.retrofit.ApiTimeApp;
import com.example.timerapp.retrofit.RetrofitClient;
import com.example.timerapp.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private View headerLayout;
    private EditText searchBar;
    private ImageView imgAdd, imgSearch;
    private TextView txtUserName;
    private boolean isSearchVisible = false;
    ApiTimeApp apiTimeApp;
    CompositeDisposable compositeDisposable=new CompositeDisposable();

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        apiTimeApp = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiTimeApp.class);
        setIntent(intent); // Cập nhật Intent
        // Kiểm tra nếu cần reload
        if (intent.getBooleanExtra("shouldReload", false)) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (currentFragment instanceof HomeActivity) {
                ((HomeActivity) currentFragment).refresh();
            } else if (currentFragment instanceof FavoriteActivity) {
                ((FavoriteActivity) currentFragment).refresh();
            } else if (currentFragment instanceof TaskFragment) {
                ((TaskFragment) currentFragment).refresh();
            } else {
                // Mặc định chuyển về HomeActivity và reload
                loadHomeFragment(true);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiTimeApp = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiTimeApp.class); // Initialize here
        init();

        // Load username
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
                searchBar.setText(""); // Clear search text
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if (currentFragment instanceof HomeActivity) {
                    ((HomeActivity) currentFragment).filter("");
                } else if (currentFragment instanceof LibraryActivity) {
                    ((LibraryActivity) currentFragment).filter("");
                } else if (currentFragment instanceof TaskFragment) {
                    ((TaskFragment) currentFragment).filter("");
                }
            } else {
                searchBar.setVisibility(View.VISIBLE);
                searchBar.requestFocus();
            }
            isSearchVisible = !isSearchVisible;
        });

        // Add TextWatcher for real-time search
        searchBar.addTextChangedListener(new TextWatcher() {
            private final Handler handler = new Handler();
            private Runnable searchRunnable;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable);
                }
                searchRunnable = () -> {
                    String query = s.toString().trim().toLowerCase();
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
                    if (currentFragment instanceof HomeActivity) {
                        ((HomeActivity) currentFragment).filter(query);
                    } else if (currentFragment instanceof LibraryActivity) {
                        ((LibraryActivity) currentFragment).filter(query);
                    } else if (currentFragment instanceof TaskFragment) {
                        ((TaskFragment) currentFragment).filter(query);
                    } else if(currentFragment instanceof FavoriteActivity){
                        ((FavoriteActivity) currentFragment).filter(query);
                    }
                };
                handler.postDelayed(searchRunnable, 300); // Delay 300ms
            }
        });

        // Xử lý nút add task
        imgAdd.setOnClickListener(v -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (currentFragment instanceof HomeActivity) {
                showAddTaskDialog();
            } else if (currentFragment instanceof LibraryActivity) {
                showAddTaskDialogForLibrary();
            }else if (currentFragment instanceof TaskFragment) {
                int folderId = (currentFragment).getArguments().getInt("id_folder", -1);
                showAddTaskDialogInLibrary(String.valueOf(folderId));
            }
        });
    }

    private void selectFragment(MenuItem item) {
        searchBar.setText(""); // Clear search text
        searchBar.setVisibility(View.GONE);

        String userName = Utils.user_current.getUsername();
        txtUserName.setText(userName);
        Fragment selectedFragment = null;
        int id = item.getItemId();
        if (id == R.id.menu_home) {
            selectedFragment = new HomeActivity();
            headerLayout.setVisibility(View.VISIBLE);
            imgAdd.setVisibility(View.VISIBLE);
        } else if (id == R.id.menu_library) {
            selectedFragment = new LibraryActivity();
            headerLayout.setVisibility(View.VISIBLE);
            imgAdd.setVisibility(View.VISIBLE);
        } else if (id == R.id.menu_favourite) {
            selectedFragment = new FavoriteActivity();
            headerLayout.setVisibility(View.VISIBLE);
            imgAdd.setVisibility(View.GONE);
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

        Spinner spinnerFolder = dialogView.findViewById(R.id.spinnerFolder);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        ImageView imgClose = dialogView.findViewById(R.id.imgClose);
        EditText edtTaskNames=dialogView.findViewById(R.id.edtTaskName);
        EditText edtTimeHour=dialogView.findViewById(R.id.edtHour);
        EditText edtTimeMinute=dialogView.findViewById(R.id.edtMinute);
        EditText edtTimeSecond=dialogView.findViewById(R.id.edtSecond);
        CheckBox isFavorite=dialogView.findViewById(R.id.checkFavorite);



        final String[] selectedFolderId = {"0"};
        // Load folder list and show dialog only after spinner is populated
        loadFolderList(new FolderListCallback() {
            String folderName;
            @Override
            public void onSuccess(List<Folder> folders) {
                List<Folder> folderList = folders != null ? folders : new ArrayList<>();
                if (folderList.isEmpty()) {
                    folderList.add(new Folder("No folders available", -1, null));
                }
                ArrayAdapter<Folder> adapter = new ArrayAdapter<>(
                        MainActivity.this,
                        android.R.layout.simple_spinner_item,
                        folderList
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerFolder.setAdapter(adapter);

                spinnerFolder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Folder selectedFolder = (Folder) parent.getItemAtPosition(position);
                        if (selectedFolder.getId() != -1) {
                            folderName = selectedFolder.getName_folder();
                            selectedFolderId[0] = String.valueOf(selectedFolder.getId());
                        }
                        else{
                            selectedFolderId[0] = null;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Do nothing
                    }
                });

                // Show dialog after spinner is populated
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            @Override
            public void onError(String message) {
                // Handle error by setting a default option
                List<Folder> folderList = new ArrayList<>();
                folderList.add(new Folder("No folders available", -1, null));
                ArrayAdapter<Folder> adapter = new ArrayAdapter<>(
                        MainActivity.this,
                        android.R.layout.simple_spinner_item,
                        folderList
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerFolder.setAdapter(adapter);

                // Show dialog even if there's an error
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        // Set up button listeners before showing the dialog
        btnSave.setOnClickListener(view -> {
            String taskNames = edtTaskNames.getText().toString();
            String hour = edtTimeHour.getText().toString().trim();
            String minute = edtTimeMinute.getText().toString().trim();
            String second = edtTimeSecond.getText().toString().trim();
            String time = hour+":"+minute+":"+second;

            if(checkInput(taskNames, time)){
                dialog.dismiss();
                if(isFavorite.isChecked()){
                    insertTask(selectedFolderId[0], taskNames, time,1);
                }else{
                    insertTask(selectedFolderId[0], taskNames, time,0);
                }

            }
        });

        imgClose.setOnClickListener(view -> dialog.dismiss());
    }

    private boolean checkInput(String taskNames, String time) {
        if (taskNames.isEmpty()) {
            Toast.makeText(MainActivity.this, "Vui lòng nhập tên task", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (time.isEmpty()) {
            Toast.makeText(MainActivity.this, "Vui lòng nhập thời gian", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showAddTaskDialogForLibrary(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_library, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();


        EditText edtNameFolder = dialogView.findViewById(R.id.edtTaskName);
        Button btnSave = dialogView.findViewById(R.id.btnSave);
        ImageView imgClose = dialogView.findViewById(R.id.imgClose);


        btnSave.setOnClickListener(view -> {
            String taskNames = edtNameFolder.getText().toString();
            if(checkInputFolder(taskNames)){
                dialog.dismiss();
                insertFolder(taskNames);}
        });

        imgClose.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void insertFolder(String taskNames) {
        compositeDisposable.add(apiTimeApp.insertFolder(Utils.user_current.getUser_id(),taskNames)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(folderModel -> {
                    if (folderModel.isSuccess()) {
                        // Show success dialog
                        AlertDialog.Builder successBuilder = new AlertDialog.Builder(this);
                        View dialogNotification = LayoutInflater.from(this).inflate(R.layout.dialog_notification_success, null);
                        successBuilder.setView(dialogNotification);
                        successBuilder.setCancelable(false);

                        AlertDialog dialogSave = successBuilder.create();
                        dialogSave.show();

                        TextView txtTitle = dialogNotification.findViewById(R.id.txtDialogTitle);
                        TextView txtMessage = dialogNotification.findViewById(R.id.txtDialogMessage);
                        txtTitle.setText("Thông báo");
                        txtMessage.setText("Đã thêm mới thư mục!");

                        new Handler().postDelayed(() -> {
                            dialogSave.dismiss();
                            // Refresh the current fragment
                            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
                            if (currentFragment instanceof LibraryActivity) {
                                ((LibraryActivity) currentFragment).refresh();
                            }
                        }, 2500);
                    } else {
                        Toast.makeText(MainActivity.this, folderModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    Toast.makeText(MainActivity.this, "Lỗi: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }));
    }

    private boolean checkInputFolder(String taskNames) {
        if(taskNames.isEmpty()){
            Toast.makeText(MainActivity.this, "Vui lòng nhập tên folder", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void init(){
        headerLayout = findViewById(R.id.headerLayout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        searchBar = findViewById(R.id.searchBar);
        imgAdd = headerLayout.findViewById(R.id.imgAdd);
        imgSearch = findViewById(R.id.imgSearch);
        txtUserName = findViewById(R.id.txtUserName);
    }

    private void loadHomeFragment(boolean shouldReload) {
        Fragment homeFragment = new HomeActivity();
        if (shouldReload) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("shouldReload", true);
            homeFragment.setArguments(bundle);
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, homeFragment)
                .commit();
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
        headerLayout.setVisibility(View.VISIBLE);
    }

    public void clearAndHideSearchBar() {
        searchBar.setText(""); // Xóa nội dung
        searchBar.setVisibility(View.GONE); // Ẩn searchBar
        isSearchVisible = false;
    }

    private interface FolderListCallback {
        void onSuccess(List<Folder> folderList);
        void onError(String message);
    }

    private void loadFolderList(FolderListCallback callback) {
        if (Utils.user_current == null) {
            callback.onError("Người dùng chưa đăng nhập");
            return;
        }
        compositeDisposable.add(apiTimeApp.getFolder(Utils.user_current.getUser_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(folderModel -> {
                    if (folderModel.isSuccess() && folderModel.getResult() != null) {
                        callback.onSuccess(folderModel.getResult());
                    } else {
                        callback.onError(folderModel.getMessage());
                    }
                }, throwable -> {
                    callback.onError(throwable.getMessage());
                }));
    }

    private void insertTask(String folderID, String taskName, String time, int isFavorite){
        // Call API to insert task
        compositeDisposable.add(apiTimeApp.insertTask(Utils.user_current.getUser_id(),folderID,taskName,time, isFavorite)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(taskModel -> {
                    if (taskModel.isSuccess()) {
                        // Show success dialog
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



                        new Handler().postDelayed(() -> {
                            dialogSave.dismiss();
                            // Refresh the current fragment
                            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
                            if (currentFragment instanceof HomeActivity) {
                                ((HomeActivity) currentFragment).refresh();
                            }
                        }, 2500);
                    } else {
                        Toast.makeText(MainActivity.this, taskModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    Toast.makeText(MainActivity.this, "Lỗi: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void showAddTaskDialogInLibrary(String folderID){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task_in_library, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();


        EditText edtTaskNames=dialogView.findViewById(R.id.edtTaskName);
        EditText edtTimeHour=dialogView.findViewById(R.id.edtHour);
        EditText edtTimeMinute=dialogView.findViewById(R.id.edtMinute);
        EditText edtTimeSecond=dialogView.findViewById(R.id.edtSecond);
        CheckBox isFavorite=dialogView.findViewById(R.id.checkFavorite);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(view -> {
            String taskNames = edtTaskNames.getText().toString();
            String hour = edtTimeHour.getText().toString().trim();
            String minute = edtTimeMinute.getText().toString().trim();
            String second = edtTimeSecond.getText().toString().trim();
            String time = hour+":"+minute+":"+second;

            if(checkInput(taskNames, time)){
                dialog.dismiss();
                if(isFavorite.isChecked()){
                    insertTaskFolder(folderID, taskNames, time,1);
                }else{
                    insertTaskFolder(folderID, taskNames, time,0);
                }
            }
        });

        ImageView imgClose = dialogView.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    private void insertTaskFolder(String folderID, String taskName, String time, int isFavorite){
        // Call API to insert task
        compositeDisposable.add(apiTimeApp.insertTask(Utils.user_current.getUser_id(),folderID,taskName,time, isFavorite)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(taskModel -> {
                    if (taskModel.isSuccess()) {
                        // Show success dialog
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



                        new Handler().postDelayed(() -> {
                            dialogSave.dismiss();
                            // Refresh the current fragment
                            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
                            if (currentFragment instanceof TaskFragment) {
                                ((TaskFragment) currentFragment).refresh();
                            }
                        }, 2500);
                    } else {
                        Toast.makeText(MainActivity.this, taskModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    Toast.makeText(MainActivity.this, "Lỗi: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }));
    }
}




