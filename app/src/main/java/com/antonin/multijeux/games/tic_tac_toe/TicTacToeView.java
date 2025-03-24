package com.antonin.multijeux.games.tic_tac_toe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.antonin.multijeux.R;

public class TicTacToeView extends View
{
    private Paint paint;
    private int cellSize;
    private int nbCell;
    private TicTacToeGame game;
    private OnCellClickListener listener;
    private TextView txtWin;
    private int offsetX;
    private int offsetY;


    public TicTacToeView(Context context, int nbCell, TicTacToeGame game, TextView txtWin)
    {
        super(context);
        paint = new Paint();

        if(nbCell > 4)
        {
            nbCell = 4;
        }

        if(nbCell < 0)
        {
            nbCell = 3;
        }

        this.nbCell = nbCell;
        this.game = game;
        this.txtWin = txtWin;
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        // Taille des cases
        cellSize = Math.min(width, height) / (this.nbCell + 1); // +1 pour éviter que ça touche les bords

        // Calcul du décalage pour centrer la grille
        int gridSize = cellSize * this.nbCell;
        this.offsetX = (width - gridSize) / 2;
        this.offsetY = (height - gridSize) / 2;

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);

        // Dessiner la grille centrée
        for (int i = 1; i < this.nbCell; i++)
        {
            // Lignes verticales
            canvas.drawLine(this.offsetX + i * cellSize, this.offsetY, this.offsetX + i * cellSize, this.offsetY + gridSize, paint);
            // Lignes horizontales
            canvas.drawLine(this.offsetX, this.offsetY + i * cellSize, this.offsetX + gridSize, this.offsetY + i * cellSize, paint);
        }

        // Dessiner les X et O en fonction du jeu
        for (int row = 0; row < this.nbCell; row++)
        {
            for (int col = 0; col < this.nbCell; col++)
            {
                if (game.getMap()[row][col] == 'X')
                {
                    drawX(canvas, col, row);
                } else if (game.getMap()[row][col] == 'O')
                {
                    drawO(canvas, col, row);
                }
            }
        }

        // Mise à jour du texte d'état de la partie
        if (!this.game.isWin() && !this.game.isGameBlocked())
        {
            this.txtWin.setText("Tour : " + this.game.getJoueur());
        } else if (this.game.isWin())
        {
            this.txtWin.setText("Gagnant : " + this.game.getJoueur());
        } else
        {
            this.txtWin.setText("Partie bloquée ! Match nul");
        }
    }

    // Dessiner un X centré dans sa case
    private void drawX(Canvas canvas, int col, int row)
    {
        paint.setColor(Color.RED);
        float startX = this.offsetX + col * cellSize;
        float startY = this.offsetY + row * cellSize;

        canvas.drawLine(startX + 50, startY + 50, startX + cellSize - 50, startY + cellSize - 50, paint);
        canvas.drawLine(startX + 50, startY + cellSize - 50, startX + cellSize - 50, startY + 50, paint);
    }

    // Dessiner un O centré dans sa case
    private void drawO(Canvas canvas, int col, int row) {
        paint.setColor(Color.BLUE);
        float centerX = this.offsetX + col * cellSize + cellSize / 2;
        float centerY = this.offsetY + row * cellSize + cellSize / 2;
        canvas.drawCircle(centerX, centerY, cellSize / 3, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            // Calculer la position relative à la grille
            int col = (int) ((event.getX() - this.offsetX) / cellSize);
            int row = (int) ((event.getY() - this.offsetY) / cellSize);

            // Vérifier que le clic est bien dans la grille
            if (col >= 0 && col < this.nbCell && row >= 0 && row < this.nbCell)
            {
                if (game.getMap()[row][col] == '\u0000')
                { // Vérifier si la case est vide
                    if (listener != null)
                    {
                        listener.onCellClick(row, col);
                    }
                }
            }
        }
        return true;
    }

    public void setOnCellClickListener(OnCellClickListener listener)
    {
        this.listener = listener;
    }

    public interface OnCellClickListener
    {
        void onCellClick(int row, int col);
    }

    public void updateGrid(int row, int col)
    {
        this.game.setMap(row,col);
        invalidate();
    }
}
