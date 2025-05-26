package com.example.timerapp.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timerapp.R;
import com.example.timerapp.adapter.FolderAdapter;
import com.example.timerapp.model.Folder;

import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends Fragment {
    private RecyclerView recyclerView;
    private FolderAdapter folderAdapter;
    private List<Folder> folderList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_library, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFolders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        folderList = new ArrayList<>();
        // Thêm folder mẫu
        folderList.add(new Folder("Công việc"));
        folderList.add(new Folder("Học tập"));
        folderList.add(new Folder("Gia đình"));

        folderAdapter = new FolderAdapter(getContext(), folderList, folder -> {
            // Xử lý khi click vào folder
            Toast.makeText(getContext(), "Chọn thư mục: " + folder.getFolderName(), Toast.LENGTH_SHORT).show();

            TaskFragment taskFragment = TaskFragment.newInstance(folder.getFolderName());
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, taskFragment)
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView.setAdapter(folderAdapter);

        return view;
    }
}
