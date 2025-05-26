package com.example.timerapp.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timerapp.Interface.SearchableFragment;
import com.example.timerapp.R;
import com.example.timerapp.model.Task;
import com.example.timerapp.adapter.TaskAdapter;
import com.example.timerapp.retrofit.ApiTimeApp;
import com.example.timerapp.retrofit.RetrofitClient;
import com.example.timerapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeActivity extends Fragment implements SearchableFragment {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    ApiTimeApp apiTimeApp;
    private List<Task> filteredTaskList;
    CompositeDisposable compositeDisposable=new CompositeDisposable();

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);
        apiTimeApp = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiTimeApp.class);

        recyclerView = view.findViewById(R.id.recyclerViewTasks);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskList=new ArrayList<>();
        filteredTaskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(getContext(), taskList);
        recyclerView.setAdapter(taskAdapter);
        getTask();
        return view;

    }

    @SuppressLint("NotifyDataSetChanged")
    private void getTask(){
        compositeDisposable.add(apiTimeApp.getTask(Utils.user_current.getUser_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        taskModel -> {
                            if (taskModel.isSuccess()) {
                                taskList.clear();
                                taskList.addAll(taskModel.getResult());
                                filteredTaskList.clear();
                                filteredTaskList.addAll(taskList);
                                taskAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(requireContext(), taskModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }, throwable -> {
                            Toast.makeText(requireContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void filterTasks(String query) {
        filteredTaskList.clear();
        if (query.isEmpty()) {
            filteredTaskList.addAll(taskList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            filteredTaskList.addAll(taskList.stream()
                    .filter(task -> task.getTitle().toLowerCase().contains(lowerCaseQuery))
                    .collect(Collectors.toList()));
        }
        taskAdapter.notifyDataSetChanged();
        if (filteredTaskList.isEmpty() && !query.isEmpty()) {
            Toast.makeText(getContext(), "Không tìm thấy task nào", Toast.LENGTH_SHORT).show();
        }
    }


}

