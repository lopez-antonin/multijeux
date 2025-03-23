package com.antonin.multijeux.games.curve_tracking;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CurveTrackingView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private Thread gameThread;
    private boolean isRunning;
    private SurfaceHolder holder;
    private Paint pathPaint;
    private Paint playerPaint;
    private Path path;
    private Path playerPath;
    private float playerX, playerY;
    private int level;
    private float pathStrokeWidth;
    private float playerStrokeWidth;
    private boolean canDraw = true;
    private boolean playerPathVisible = true;

    public CurveTrackingView(Context context, int level) {
        super(context);
        this.level = level;
        holder = getHolder();
        holder.addCallback(this);

        this.pathStrokeWidth = 50 - this.level * 10;
        pathPaint = new Paint();
        pathPaint.setColor(0xFFA63A50);
        pathPaint.setStrokeWidth(pathStrokeWidth);
        pathPaint.setStyle(Paint.Style.STROKE);

        this.playerStrokeWidth = 50 - this.level * 10;
        playerPaint = new Paint();
        playerPaint.setColor(0xFFBA6E6E);
        playerPaint.setStrokeWidth(playerStrokeWidth);
        playerPaint.setStyle(Paint.Style.STROKE);

        path = new Path();
        playerPath = new Path();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        int w = getWidth();
        int h = getHeight();
        int centerX = w / 2;
        int centerY = h / 2;
        int radius = Math.min(w, h) / 3;

        path.reset();
        path.addCircle(centerX, centerY, radius, Path.Direction.CW);

        playerX = centerX;
        playerY = centerY - radius;

        playerPath.moveTo(playerX, playerY);

        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
        try {
            if (gameThread != null) {
                gameThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            if (!holder.getSurface().isValid()) continue;
            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                drawGame(canvas);
                holder.unlockCanvasAndPost(canvas);
            }

            // Vérification de la fin du jeu ici
            if (getContext() instanceof CurveTrackingGame) {
                CurveTrackingGame gameActivity = (CurveTrackingGame) getContext();
                if (gameActivity.getGameManager().isGameCompleted(this)) {
                    break;
                }
            }

            try {
                Thread.sleep(8); // 120 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void drawGame(Canvas canvas) {
        canvas.drawColor(0xFFF0E7D8);
        canvas.drawPath(path, pathPaint);
        if (playerPathVisible) {
            canvas.drawPath(playerPath, playerPaint);
        }
    }

    public void stopDrawing() {
        canDraw = false;
    }

    public void togglePlayerPathVisibility() {
        playerPathVisible = !playerPathVisible;
    }

    private boolean isOutsidePath(float x, float y) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(getWidth(), getHeight()) / 3;

        float distance = (float) Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));

        return distance > radius + this.pathStrokeWidth / 2 + this.playerStrokeWidth / 2 || distance < radius - this.pathStrokeWidth / 2 - this.playerStrokeWidth / 2;
    }


    public void updateMovement(float dx, float dy) {
        if (!canDraw) return;

        if (Math.abs(dx) < 0.5f && Math.abs(dy) < 0.5f) {
            return;
        }

        playerX += dx;
        playerY += dy;

        if (isOutsidePath(playerX, playerY)) {
            CurveTrackingGame gameActivity = (CurveTrackingGame) getContext();
            gameActivity.getGameManager().updateScore(0.1);
        }

        playerPath.lineTo(playerX, playerY);
    }


    public float getPlayerX() {
        return playerX;
    }

    public float getPlayerY() {
        return playerY;
    }
}
