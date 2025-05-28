package com.example.timerapp.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timerapp.R;
import com.example.timerapp.adapter.TaskAdapter;
import com.example.timerapp.model.Task;
import com.example.timerapp.retrofit.ApiTimeApp;
import com.example.timerapp.retrofit.RetrofitClient;
import com.example.timerapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TaskFragment extends Fragment {
    private static final String ARG_FOLDER_NAME = "folder_name";
    private String folderName;
    private int id_folder;
    private RecyclerView recyclerView;
    private List<Task> filteredTaskList = new ArrayList<>(); // Danh sách đã lọc
    private List<Task> taskList = new ArrayList<>(); // Giả sử task là danh sách String thời gian
    private TaskAdapter taskAdapter;
    ApiTimeApp apiTimeApp;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    public static TaskFragment newInstance(String folderName, int id_folder) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FOLDER_NAME, folderName);
        args.putInt("id_folder", id_folder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiTimeApp = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiTimeApp.class);

        if (getArguments() != null) {
            folderName = getArguments().getString(ARG_FOLDER_NAME);
            id_folder= getArguments().getInt("id_folder");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        if (getArguments() != null) {
            folderName = getArguments().getString("folder_name");
        }

        // Gán tên thư mục vào TextView
        TextView txtFolderTitle = view.findViewById(R.id.txtFolderTitle);
        txtFolderTitle.setText(folderName);

        // Xử lý nút quay lại
        ImageView imgBack = view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> {
            // Xóa nội dung và ẩn searchBar trước khi quay lại
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).clearAndHideSearchBar();
            }
            // Quay lại fragment trước
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // TODO: Hiển thị danh sách thời gian (task) ở đây
        taskAdapter = new TaskAdapter(getContext(), filteredTaskList);
        getTask();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(taskAdapter);

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getTask() {
        compositeDisposable.add(apiTimeApp.getTaskFolder(id_folder)
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
                                Toast.makeText(requireContext(),taskModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }, throwable -> {
                            Toast.makeText(requireContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    public void refresh() {
        getTask(); // Gọi lại để tải dữ liệu mới
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear(); // Dọn dẹp RxJava
    }
}
