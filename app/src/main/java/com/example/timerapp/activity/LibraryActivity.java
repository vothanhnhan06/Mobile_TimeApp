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

import com.example.timerapp.R;
import com.example.timerapp.adapter.FolderAdapter;
import com.example.timerapp.model.Folder;
import com.example.timerapp.retrofit.ApiTimeApp;
import com.example.timerapp.retrofit.RetrofitClient;
import com.example.timerapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LibraryActivity extends Fragment {
    private RecyclerView recyclerView;
    private FolderAdapter folderAdapter;
    private List<Folder> folderList;
    ApiTimeApp apiTimeApp;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_library, container, false);
        apiTimeApp = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiTimeApp.class);

        recyclerView = view.findViewById(R.id.recyclerViewFolders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        folderList = new ArrayList<>();
        // Thêm folder mẫu
        getFolder();

        folderAdapter = new FolderAdapter(getContext(), folderList, folder -> {
            // Xử lý khi click vào folder
            TaskFragment taskFragment = TaskFragment.newInstance(folder.getName_folder(), folder.getId());
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, taskFragment)
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView.setAdapter(folderAdapter);

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getFolder() {
        compositeDisposable.add(apiTimeApp.getFolder(Utils.user_current.getUser_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        folderModel -> {
                            if (folderModel.isSuccess()) {
                                folderList.clear();
                                folderList.addAll(folderModel.getResult());
                                folderAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(requireContext(),folderModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }, throwable -> {
                            Toast.makeText(requireContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear(); // Dọn dẹp RxJava
    }
}
