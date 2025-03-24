package com.antonin.multijeux.games.game_of_life;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.antonin.multijeux.games.curve_tracking.CurveTrackingGame;
import com.antonin.multijeux.games.game_of_life.model.Cell;
import com.antonin.multijeux.games.game_of_life.model.Grid;

public class GameOfLifeView extends SurfaceView implements Runnable {
    private int size;
    private Grid grid;
    private Paint cellPaint;
    private Paint gridPaint;
    private boolean isRunning;
    private Thread gameThread;
    private SurfaceHolder holder;

    public GameOfLifeView(Context context, int size, double density) {
        super(context);
        this.size = size;
        this.grid = new Grid(size, size, density);
        this.holder = getHolder(); // Récupère le SurfaceHolder pour gérer le dessin

        // Initialisation des pinceaux
        cellPaint = new Paint();
        cellPaint.setColor(Color.BLACK);
        cellPaint.setStyle(Paint.Style.FILL);

        gridPaint = new Paint();
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(2);
    }

    @Override
    public void run() {
        while (isRunning) {
            if (!holder.getSurface().isValid()) {
                continue;
            }

            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                synchronized (holder) {
                    drawGame(canvas);
                }
                holder.unlockCanvasAndPost(canvas);
            }

            // Vérification des conditions de fin
            if (getContext() instanceof GameOfLifeGame) {
                GameOfLifeGame gameActivity = (GameOfLifeGame) getContext();
//                if (gameActivity.getGameManager().isGameCompleted(this)) {
//                    isRunning = false;
//                    break;
//                }
            }

            try {
                Thread.sleep(500); // Rafraîchissement à 2 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            postInvalidate(); // Redessine la vue
        }
    }

    private void drawGame(Canvas canvas) {
        int w = getWidth();
        int h = getHeight();
        int cellSize = Math.min(w, h) / size;

        // Dessiner les cellules vivantes
        Cell[][] cells = grid.getCells();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (cells[i][j].isAlive()) {
                    canvas.drawRect(
                            j * cellSize, i * cellSize,
                            (j + 1) * cellSize, (i + 1) * cellSize,
                            cellPaint
                    );
                }
            }
        }

        // Dessiner la grille
        for (int i = 0; i <= size; i++) {
            canvas.drawLine(i * cellSize, 0, i * cellSize, h, gridPaint);
            canvas.drawLine(0, i * cellSize, w, i * cellSize, gridPaint);
        }
    }

    public void startGame() {
        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void stopGame() {
        isRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
