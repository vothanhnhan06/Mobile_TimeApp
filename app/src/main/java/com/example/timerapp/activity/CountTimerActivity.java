package com.example.timerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;

import com.example.timerapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;


public class CountTimerActivity extends AppCompatActivity {
    CircleCountdownView progressCircle;
    CountDownTimer countDownTimer;
    long totalTime = 60000;
    long interval = 1000;
    long timeLeft = 0;
    boolean isRunning = false;

    TextView txtTimer;
    TextView txtTitle;
    ImageView btnStart;
    FrameLayout container;
    boolean isFinished = false;
    ImageView btnBack;
    ImageView btnReset;
    ImageView btnEdit;
    ImageView btnCloseEdit;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        container = findViewById(R.id.circleContainer);
        CircleCountdownView circleView = new CircleCountdownView(this, null);
        container.addView(circleView);
        progressCircle = circleView;

        txtTimer = findViewById(R.id.txtTimer);
        txtTitle = findViewById(R.id.txtTitle);
        btnStart = findViewById(R.id.btnPause);


        // Nhận thời gian từ Intent
        String timeString = getIntent().getStringExtra("time");
        String title = getIntent().getStringExtra("title");

        txtTimer.setText(timeString);
        txtTitle.setText(title);

        // Chuyển đổi sang milliseconds
        totalTime = convertTimeToMillis(timeString);
        timeLeft = totalTime;

        // Sự kiện click
        pauseTimer();
        btnStart.setOnClickListener(v -> {
            if (isRunning) {
                pauseTimer();
            } else {
                if (isFinished) {
                    timeLeft = totalTime;                  // reset thời gian
                    progressCircle.setProgress(1.0f);      // reset vòng tròn đầy
                    isFinished = false;                    // reset flag
                }
                startTimer();
            }
        });
        //Trở về trang chủ
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(CountTimerActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
        //Reset lại thời gian đếm ngược
        btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(v -> {
            pauseTimer();
            timeLeft = totalTime;
            txtTimer.setText(formatTime(totalTime));
            progressCircle.setProgress(1.0f);
            isFinished = false;
        });
        //Edit thời gian
        btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(v -> {
            showTimePickerDialog();
        });
    }

    private long convertTimeToMillis(String timeString) {
        if (timeString == null) timeString = "00:00:00";
        String[] parts = timeString.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);
        return (hours * 3600 + minutes * 60 + seconds) * 1000L;
    }

    private void startTimer() {
        if (timeLeft <= 0) {
            timeLeft = totalTime;
            progressCircle.setProgress(1.0f); // reset vòng tròn về đầy
        }

        countDownTimer = new CountDownTimer(timeLeft, interval) {
            public void onTick(long millisUntilFinished) {

                timeLeft = millisUntilFinished;

                float progress = (float) millisUntilFinished / totalTime;
                progressCircle.setProgress(progress);


                long seconds = millisUntilFinished / 1000;
                long hours = seconds / 3600;
                long minutes = (seconds % 3600) / 60;
                long secs = seconds % 60;

                txtTimer.setText(String.format("%02d:%02d:%02d", hours, minutes, secs));
            }

            public void onFinish() {
                isFinished = true; // đánh dấu đã kết thúc
                isRunning = false;
                progressCircle.setProgress(0);
                txtTimer.setText("00:00:00");
                btnStart.setImageResource(R.drawable.ic_play); // đổi về icon phát

                // Phát nhạc chuông khi hết giờ
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
                if (ringtone != null) {
                    ringtone.play();
                }
            }
        }.start();

        isRunning = true;
        btnStart.setImageResource(R.drawable.ic_pause); // đổi icon sang tạm dừng
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isRunning = false;
        btnStart.setImageResource(R.drawable.ic_play); // đổi icon sang phát
    }
    private String formatTime(long millis) {
        long seconds = millis / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }
    private void showTimePickerDialog() {
        BottomSheetDialog editDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.activity_edit, null);
        editDialog.setContentView(view);

        NumberPicker pickerHours = view.findViewById(R.id.pickerHours);
        pickerHours.setMinValue(0);
        pickerHours.setMaxValue(23);

        NumberPicker pickerMinutes = view.findViewById(R.id.pickerMinutes);
        pickerMinutes.setMinValue(0);
        pickerMinutes.setMaxValue(59);

        NumberPicker pickerSeconds = view.findViewById(R.id.pickerSeconds);
        pickerSeconds.setMinValue(0);
        pickerSeconds.setMaxValue(59);
        Button btnSaveTime = view.findViewById(R.id.btnSaveTime);

        // Set format 2 chữ số
        NumberPicker.Formatter formatter = value -> String.format("%02d", value);
        pickerHours.setFormatter(formatter);
        pickerMinutes.setFormatter(formatter);
        pickerSeconds.setFormatter(formatter);

        btnSaveTime.setOnClickListener(v -> {
            int hours = pickerHours.getValue();
            int minutes = pickerMinutes.getValue();
            int seconds = pickerSeconds.getValue();

            long totalMillis = (hours * 3600 + minutes * 60 + seconds) * 1000L;
            totalTime = totalMillis;
            timeLeft = totalMillis;

            txtTimer.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));

            editDialog.dismiss();
        });

        editDialog.show();
        //Đóng edit thời gian
        btnCloseEdit = view.findViewById(R.id.btnCloseEdit);
        btnCloseEdit.setOnClickListener(v -> {
            editDialog.dismiss();
        });
    }
}
