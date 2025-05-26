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
import com.example.timerapp.retrofit.ApiTimeApp;
import com.example.timerapp.retrofit.RetrofitClient;
import com.example.timerapp.utils.Utils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private Context context;
    ApiTimeApp apiTimeApp;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
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
        int favorite = task.isFavorite();
        holder.txtTitle.setText(task.getTitle());
        holder.txtTime.setText(task.getTime());


        // Hiển thị đúng màu theo trạng thái yêu thích
        if (favorite==1) {
            holder.imgBookmark.setColorFilter(ContextCompat.getColor(context, R.color.yellow));

        } else {
            holder.imgBookmark.setColorFilter(ContextCompat.getColor(context, R.color.white));
        }

        holder.imgBookmark.setOnClickListener(v -> {
            int isFavorite = (favorite == 1) ? 0 : 1;
            task.setFavorite(isFavorite);
            notifyItemChanged(position);

            compositeDisposable.add(apiTimeApp.updateFavorite(task.getId(), isFavorite)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            taskModel -> {
                                String message = (isFavorite==1) ? "Đã thêm vào yêu thích" : "Đã xóa khỏi yêu thích";
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            },
                            throwable -> {
                                Toast.makeText(context, "Lỗi: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    ));
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
                int taskId = task.getId();
                removeTask(taskId);

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

    private void removeTask(int taskId) {
        compositeDisposable.add(apiTimeApp.deleteTask(taskId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        taskModel -> {

                        }, throwable -> {
                            Toast.makeText(context.getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
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
            apiTimeApp = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiTimeApp.class);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtTime = itemView.findViewById(R.id.txtTime);
            imgBookmark = itemView.findViewById(R.id.imgBookmark);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}