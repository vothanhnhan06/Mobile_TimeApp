package com.example.timerapp.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timerapp.R;
import com.example.timerapp.model.Task;
import com.example.timerapp.adapter.TaskAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Fragment {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTasks);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(getContext(), taskList);
        recyclerView.setAdapter(taskAdapter);

        taskList.add(new Task("Học bài", 1, "00:01:00"));
        taskList.add(new Task("Đọc sách", 1, "01:00:00"));
        taskAdapter.notifyDataSetChanged();



        return view;
    }
}

