package com.example.timerapp.activity;


import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.timerapp.R;
import com.example.timerapp.Task;
import com.example.timerapp.TaskAdapter;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_homepage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        recyclerView = findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskList = new ArrayList<>();
        taskList.add(new Task("Học bài", "00:01:00",false));
        taskList.add(new Task("Đọc sách", "01:00:00",false));
        taskList.add(new Task("Thiền", "02:00:00",false));
        taskList.add(new Task("Tập gym", "02:00:00",false));
        taskList.add(new Task("Yoga", "02:00:00",false));

        taskAdapter = new TaskAdapter(this, taskList);
        recyclerView.setAdapter(taskAdapter);
    }
}
