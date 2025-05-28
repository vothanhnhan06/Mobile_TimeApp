package com.example.timerapp.activity;

import android.annotation.SuppressLint;
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
import com.example.timerapp.adapter.TaskAdapter;
import com.example.timerapp.model.Folder;
import com.example.timerapp.model.Task;
import com.example.timerapp.retrofit.ApiTimeApp;
import com.example.timerapp.retrofit.RetrofitClient;
import com.example.timerapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoriteActivity extends Fragment implements SearchableFragment {
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    ApiTimeApp apiTimeApp;
    private List<Task> filteredTaskList;
    CompositeDisposable compositeDisposable=new CompositeDisposable();

    @SuppressLint({"NotifyDataSetChanged", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_favourite, container, false);
        apiTimeApp = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiTimeApp.class);

        recyclerView = view.findViewById(R.id.recyclerViewFavorite);

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
        compositeDisposable.add(apiTimeApp.getTaskFavorite(Utils.user_current.getUser_id())
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
    public void filter(String query) {
        List<Task> newFilteredList = new ArrayList<>();
        if (query.isEmpty()) {
            newFilteredList.addAll(taskList);
        } else {
            String[] searchWords = query.trim().toLowerCase().split("\\s+");
            for (Task task : taskList) {
                String title = task.getTitle().toLowerCase();
                for (String word : searchWords) {
                    if (title.contains(word)) {
                        newFilteredList.add(task);
                        break;
                    }
                }
            }
        }
        taskAdapter.setTasks(newFilteredList); // Use DiffUtil to update
    }

    public void refresh() {
        getTask(); // Gọi lại để tải dữ liệu mới
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear(); // Dọn dẹp RxJava
    }
}
