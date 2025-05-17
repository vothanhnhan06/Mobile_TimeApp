package com.example.timerapp.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.timerapp.R;

public class FavoriteActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout: đổi tên layout thành fragment_favorite.xml
        return inflater.inflate(R.layout.activity_favourite, container, false);
    }
}
