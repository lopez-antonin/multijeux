package com.antonin.multijeux.games.curve_tracking;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.SurfaceView;

public class CurveTrackingView extends SurfaceView {

    private Paint pathPaint;
    private Paint playerPaint;
    private Path path;
    private float playerX, playerY;
    private int level;

    public CurveTrackingView(Context context, int level) {
        super(context);
        this.level = level;

        pathPaint = new Paint();
        pathPaint.setColor(0xFFA63A50);
        pathPaint.setStrokeWidth(10 - this.level * 2);
        pathPaint.setStyle(Paint.Style.STROKE);

        playerPaint = new Paint();
        playerPaint.setColor(0xFFBA6E6E);

        path = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int centerX = w / 2;
        int centerY = h / 2;
        int radius = Math.min(w, h) / 3;

        path.reset();
        path.addCircle(centerX, centerY, radius, Path.Direction.CW);

        playerX = centerX;
        playerY = centerY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(path, pathPaint);

        canvas.drawCircle(playerX, playerY, 20, playerPaint);
    }

    public void updateMovement(float dx, float dy) {
        playerX += dx;
        playerY += dy;
        invalidate();
    }
}
