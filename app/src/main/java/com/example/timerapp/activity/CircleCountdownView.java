package com.example.timerapp.activity;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

public class CircleCountdownView extends View {
    private Paint bgPaint;
    private Paint progressPaint;
    private RectF rectF;

    private float progress = 1f; // từ 1.0 đến 0.0

    public CircleCountdownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        bgPaint = new Paint();
        bgPaint.setColor(Color.LTGRAY);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeWidth(30);
        bgPaint.setAntiAlias(true);

        progressPaint = new Paint();
        progressPaint.setColor(Color.parseColor("#91B526")); // màu xanh
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(30);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setAntiAlias(true);

        rectF = new RectF();
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate(); // vẽ lại
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float padding = 40;
        float left = padding;
        float top = padding;
        float right = getWidth() - padding;
        float bottom = getHeight() - padding;
        rectF.set(left, top, right, bottom);

        // Vẽ nền xám
        canvas.drawArc(rectF, 0, 360, false, bgPaint);
        // Vẽ phần tiến trình màu xanh
        canvas.drawArc(rectF, 90, -360 * progress, false, progressPaint);
    }
}
