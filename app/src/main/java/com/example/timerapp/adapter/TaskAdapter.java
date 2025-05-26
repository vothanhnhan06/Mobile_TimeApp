package com.example.timerapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timerapp.R;
import com.example.timerapp.activity.CountTimerActivity;
import com.example.timerapp.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private Context context;
    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    public TaskAdapter(){

    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.txtTitle.setText(task.getTitle());
        holder.txtTime.setText(task.getTime());


        // Hiển thị đúng màu theo trạng thái yêu thích
        if (task.isFavorite()) {
            holder.imgBookmark.setColorFilter(ContextCompat.getColor(context, R.color.yellow));
        } else {
            holder.imgBookmark.setColorFilter(ContextCompat.getColor(context, R.color.white));
        }

        holder.imgBookmark.setOnClickListener(v -> {
            boolean isFavorite = !task.isFavorite();
            task.setFavorite(isFavorite);
            notifyItemChanged(position); // cập nhật lại item sau khi đổi trạng thái
            String message = isFavorite ? "Đã thêm vào yêu thích" : "Đã xóa khỏi yêu thích";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        });

        //Hiển thị thời gian đếm ngược
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CountTimerActivity.class);
            intent.putExtra("title", task.getTitle());
            intent.putExtra("time", task.getTime());
            context.startActivity(intent);
        });
        //Xóa thời gian đếm ngược
        holder.imgDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            // Inflate layout tùy chỉnh
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_delete, null);
            builder.setView(dialogView);

            builder.setPositiveButton("Có", (dialog, which) -> {
                taskList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, taskList.size());
            });

            builder.setNegativeButton("Không", null);

            AlertDialog dialog = builder.create();

            // Tùy chỉnh nút sau khi dialog hiển thị
            dialog.setOnShowListener(dialogInterface -> {
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                if (positiveButton != null) {
                    positiveButton.setTextColor(Color.parseColor("#355E3B")); // màu xanh
                    positiveButton.setAllCaps(false);
                    positiveButton.setText("Có");
                }

                if (negativeButton != null) {
                    negativeButton.setTextColor(Color.parseColor("#FF0000")); // màu đỏ
                    negativeButton.setAllCaps(false);
                    negativeButton.setText("Không");
                }
            });

            dialog.show();
        });


    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView txtTime, txtTitle;
        ImageView imgBookmark, imgDelete;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtTime = itemView.findViewById(R.id.txtTime);
            imgBookmark = itemView.findViewById(R.id.imgBookmark);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}