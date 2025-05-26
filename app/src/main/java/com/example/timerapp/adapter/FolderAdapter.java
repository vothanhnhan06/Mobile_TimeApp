package com.example.timerapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timerapp.R;
import com.example.timerapp.model.Folder;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {

    private List<Folder> folderList;
    private Context context;
    private OnItemClickListener listener;


    public interface OnItemClickListener {
        void onItemClick(Folder folder);
    }

    public FolderAdapter(Context context, List<Folder> folderList, OnItemClickListener listener) {
        this.context = context;
        this.folderList = folderList;
        this.listener = listener;
    }

    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_folder, parent, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FolderViewHolder holder, int position) {
        Folder folder = folderList.get(position);
        holder.tvFolderName.setText(folder.getFolderName());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(folder);
        });


    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }

    static class FolderViewHolder extends RecyclerView.ViewHolder {
        TextView tvFolderName;
        ImageView imgDelete;
        public FolderViewHolder(View itemView) {
            super(itemView);
            tvFolderName = itemView.findViewById(R.id.tvFolderName);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}
