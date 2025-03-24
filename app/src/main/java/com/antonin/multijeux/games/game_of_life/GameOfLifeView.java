package com.antonin.multijeux.games.game_of_life;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.antonin.multijeux.games.game_of_life.model.Cell;
import com.antonin.multijeux.games.game_of_life.model.Grid;

public class GameOfLifeView extends View {
    private int size;
    private Grid grid;
    private Paint cellPaint;
    private Paint gridPaint;

    public GameOfLifeView(Context context, int size, double density) {
        super(context);
        this.size = size;
        this.grid = new Grid(size, size, density);

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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

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
}
