package com.antonin.multijeux.games.game_of_life;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.antonin.multijeux.games.game_of_life.model.Cell;
import com.antonin.multijeux.games.game_of_life.model.Grid;
import java.util.LinkedList;

public class GameOfLifeView extends SurfaceView implements Runnable
{
    private int size;
    private Grid grid;
    private Paint cellPaint;
    private boolean isRunning;
    private Thread gameThread;
    private SurfaceHolder holder;

    // Historique des grilles (10 dernières grilles)
    private LinkedList<Grid> gridHistory;
    private static final int HISTORY_SIZE = 3;

    public GameOfLifeView(Context context, int size, double density)
    {
        super(context);
        this.size = size;
        this.grid = new Grid(size, size, density);
        this.holder = getHolder(); // Récupère le SurfaceHolder pour gérer le dessin

        // Initialisation de l'historique des grilles
        this.gridHistory = new LinkedList<>();

        cellPaint = new Paint();
        cellPaint.setColor(0xFFA1674A);
        cellPaint.setStyle(Paint.Style.FILL);
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

            // Mise à jour de la grille
            updateGridHistory();  // Ajout de la grille actuelle à l'historique
            grid.updateGrid();

            GameOfLifeGame gameActivity = (GameOfLifeGame) getContext();
            gameActivity.getGameManager().updateIterations();

            if (getContext() instanceof GameOfLifeGame)
            {
                if (gameActivity.getGameManager().isGameCompleted(this))
                {
                    break;
                }
            }

            try {
                Thread.sleep(16); // Rafraîchissement à 5 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        isRunning = false;
        try
        {
            gameThread.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void stopDrawing()
    {
        isRunning = false;
    }

    private void drawGame(Canvas canvas)
    {
        int w = getWidth();
        int h = getHeight();
        int cellSize = Math.min(w, h) / size;

        // Calcul des décalages pour centrer la grille
        int gridWidth = cellSize * size;
        int gridHeight = cellSize * size;
        int offsetX = (w - gridWidth) / 2;
        int offsetY = (h - gridHeight) / 2;

        canvas.drawColor(0xFFF0E7D8);

        // Cellules vivantes
        Cell[][] cells = grid.getCells();
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                if (cells[i][j].isAlive())
                {
                    canvas.drawRect(
                            offsetX + j * cellSize, offsetY + i * cellSize,
                            offsetX + (j + 1) * cellSize, offsetY + (i + 1) * cellSize,
                            cellPaint
                    );
                }
            }
        }
    }



    private void updateGridHistory() {
        if (gridHistory.size() >= HISTORY_SIZE) {
            gridHistory.removeFirst();
        }
        gridHistory.add(grid.clone()); // Ajouter une copie de la grille
    }




    public LinkedList<Grid> getGridHistory() {
        return gridHistory;
    }
}
