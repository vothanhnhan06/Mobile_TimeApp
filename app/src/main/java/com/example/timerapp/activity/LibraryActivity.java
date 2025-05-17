package com.example.timerapp.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.timerapp.R;

public class LibraryActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout: activity_library.xml → đổi tên thành fragment_library.xml để đúng ngữ nghĩa
        return inflater.inflate(R.layout.activity_library, container, false);
    }
}
