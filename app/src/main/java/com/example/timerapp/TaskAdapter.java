package com.example.timerapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timerapp.activity.CountTimerActivity;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private Context context;

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
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
            task.setFavorite(!task.isFavorite());
            notifyItemChanged(position); // cập nhật lại item sau khi đổi trạng thái
        });

        //Hiển thị thời gian đếm ngược
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CountTimerActivity.class);
            intent.putExtra("title", task.getTitle());
            intent.putExtra("time", task.getTime());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView txtTime, txtTitle;
        ImageView imgBookmark, imgMore;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtTime = itemView.findViewById(R.id.txtTime);
            imgBookmark = itemView.findViewById(R.id.imgBookmark);
            imgMore = itemView.findViewById(R.id.imgMore);
        }
    }
}