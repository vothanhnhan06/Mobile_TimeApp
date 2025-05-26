package com.example.timerapp.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timerapp.R;
import com.example.timerapp.adapter.TaskAdapter;
import com.example.timerapp.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment {
    private static final String ARG_FOLDER_NAME = "folder_name";
    private String folderName;

    private RecyclerView recyclerView;
    private List<Task> taskList = new ArrayList<>(); // Giả sử task là danh sách String thời gian
    private TaskAdapter taskAdapter;
    public static TaskFragment newInstance(String folderName) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FOLDER_NAME, folderName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            folderName = getArguments().getString(ARG_FOLDER_NAME);
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
        imgBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // TODO: Hiển thị danh sách thời gian (task) ở đây
        taskList = new ArrayList<>();
        taskList.add(new Task( "Học bài", 1, "08:00:00"));

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        TaskAdapter adapter = new TaskAdapter(getContext(), taskList);
        recyclerView.setAdapter(adapter);

        return view;
    }

}
