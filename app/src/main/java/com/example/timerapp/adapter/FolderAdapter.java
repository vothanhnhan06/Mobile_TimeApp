package com.example.timerapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DiffUtil;
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
        holder.tvFolderName.setText(folder.getName_folder());

        String imgPath = folder.getImage_path();
        if (imgPath != null && !imgPath.isEmpty()) {
            @SuppressLint("DiscouragedApi") int resourceId = context.getResources().getIdentifier(imgPath, "drawable", context.getPackageName());
            if (resourceId != 0) {
                holder.imgFolderIcon.setImageResource(resourceId);
            } else {
                holder.imgFolderIcon.setImageResource(R.drawable.folder_default);
                // Tùy chọn: Hiển thị thông báo lỗi (bỏ comment nếu cần)
                // Toast.makeText(context, "Không tìm thấy ảnh: " + imgPath, Toast.LENGTH_SHORT).show();
            }
        } else {
            holder.imgFolderIcon.setImageResource(R.drawable.folder_default);
        }

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
        ImageView imgFolderIcon;
        public FolderViewHolder(View itemView) {
            super(itemView);
            tvFolderName = itemView.findViewById(R.id.tvFolderName);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgFolderIcon=itemView.findViewById(R.id.imgFolderIcon);
        }
    }

    public void setFolder(List<Folder> newTasks) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return folderList.size();
            }

            @Override
            public int getNewListSize() {
                return newTasks.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return folderList.get(oldItemPosition).getId() == newTasks.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return folderList.get(oldItemPosition).equals(newTasks.get(newItemPosition));
            }
        });
        folderList.clear();
        folderList.addAll(newTasks);
        diffResult.dispatchUpdatesTo(this);
    }
}
