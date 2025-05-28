package com.example.timerapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timerapp.R;

import com.example.timerapp.retrofit.ApiTimeApp;
import com.example.timerapp.retrofit.RetrofitClient;
import com.example.timerapp.utils.Utils;
import com.google.android.gms.maps.model.Circle;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.sql.Time;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;

import java.util.concurrent.TimeUnit;
import nl.dionsegijn.konfetti.xml.KonfettiView;
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
    ImageView imgTask;
    KonfettiView konfettiView;
    TextView txtCompleted;
    Ringtone ringtone;
    ApiTimeApp apiTimeApp;
    CompositeDisposable compositeDisposable=new CompositeDisposable();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counttimer);
        apiTimeApp = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiTimeApp.class);



        container = findViewById(R.id.circleContainer);
        CircleCountdownView circleView = new CircleCountdownView(this, null);
        container.addView(circleView);
        progressCircle = circleView;

        imgTask=findViewById(R.id.imgTask);
        txtTimer = findViewById(R.id.txtTimer);
        txtTitle = findViewById(R.id.txtTitle);
        btnStart = findViewById(R.id.btnPause);

        konfettiView = findViewById(R.id.konfettiView);
        txtCompleted = findViewById(R.id.txtCompleted);

        // Nhận thời gian từ Intent
        String timeString = getIntent().getStringExtra("time");
        String title = getIntent().getStringExtra("title");
        String imgPath = getIntent().getStringExtra("imgTask");
        int task_id = getIntent().getIntExtra("task_id",-1);

        txtTimer.setText(timeString);
        txtTitle.setText(title);
        if (imgPath != null && !imgPath.isEmpty()) {
            // Lấy ID tài nguyên từ tên chuỗi
            @SuppressLint("DiscouragedApi") int resourceId = getResources().getIdentifier(imgPath, "drawable", getPackageName());
            if (resourceId != 0) { // Kiểm tra tài nguyên có tồn tại
                imgTask.setImageResource(resourceId);
            } else {
                // Nếu không tìm thấy, đặt ảnh mặc định
                imgTask.setImageResource(R.drawable.default_image);
                Toast.makeText(this, "Không tìm thấy ảnh: " + imgPath, Toast.LENGTH_SHORT).show();
            }
        } else {
            // Nếu imgPath null hoặc rỗng, đặt ảnh mặc định
            imgTask.setImageResource(R.drawable.default_image);
        }

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
            intent.putExtra("shouldReload", true);
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
            // Dừng chuông nếu đang phát
            if (ringtone != null && ringtone.isPlaying()) {
                ringtone.stop();
            }
        });
        //Edit thời gian
        btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(v -> {
            showTimePickerDialog(task_id);
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
                //Bắn pháo giấy
                List<Party> parties = new ArrayList<>();

                EmitterConfig emitterConfig = new Emitter(50, TimeUnit.MILLISECONDS).perSecond(500);

                parties.add(new PartyFactory(emitterConfig)
                        .angle(270)
                        .spread(360)
                        .setSpeedBetween(5f, 15f)
                        .timeToLive(3000L)
                        .position(new Position.Relative(0.1, 0.1))
                        .build());

                parties.add(new PartyFactory(emitterConfig)
                        .angle(270)
                        .spread(360)
                        .setSpeedBetween(5f, 15f)
                        .timeToLive(3000L)
                        .position(new Position.Relative(0.9, 0.1))
                        .build());

                parties.add(new PartyFactory(emitterConfig)
                        .angle(270)
                        .spread(360)
                        .setSpeedBetween(5f, 15f)
                        .timeToLive(3000L)
                        .position(new Position.Relative(0.5, 0.5))
                        .build());

                for (Party party : parties) {
                    konfettiView.start(party);
                }
                // Phát nhạc chuông khi hết giờ
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
                if (ringtone != null && !ringtone.isPlaying()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ringtone.setLooping(true); // lặp lại
                    }
                    ringtone.play();
                }

                // Dừng tiếng chuông sau 10 giây
                new Handler().postDelayed(() -> {
                    if (ringtone != null && ringtone.isPlaying()) {
                        ringtone.stop();
                    }
                }, 10000);
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
    private void showTimePickerDialog(int task_id) {
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
            String new_time=String.format("%02d:%02d:%02d", hours, minutes, seconds);
            txtTimer.setText(new_time);
            updateTask(task_id,new_time);
            editDialog.dismiss();
        });

        editDialog.show();
        //Đóng edit thời gian
        btnCloseEdit = view.findViewById(R.id.btnCloseEdit);
        btnCloseEdit.setOnClickListener(v -> {
            editDialog.dismiss();
        });
    }

    private void updateTask(int task_id, String new_time) {
        compositeDisposable.add(apiTimeApp.updateTask(task_id, new_time )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        taskModel -> {
                            if (taskModel.isSuccess()) {
                                Toast.makeText(this,taskModel.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this,taskModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }, throwable -> {
                            Toast.makeText(this, "new time"+new_time+"task_id"+task_id, Toast.LENGTH_SHORT).show();
                        }
                ));
    }
}
