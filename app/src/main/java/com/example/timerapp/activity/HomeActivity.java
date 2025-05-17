package com.example.timerapp.activity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timerapp.R;
import com.example.timerapp.Task;
import com.example.timerapp.TaskAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Fragment {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTasks);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(getContext(), taskList);
        recyclerView.setAdapter(taskAdapter);

        taskList.add(new Task("Học bài", "00:01:00", false));
        taskList.add(new Task("Đọc sách", "01:00:00", false));
        taskAdapter.notifyDataSetChanged();



        return view;
    }
}

