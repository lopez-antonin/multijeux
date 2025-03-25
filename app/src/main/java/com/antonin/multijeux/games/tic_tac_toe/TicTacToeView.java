package com.antonin.multijeux.games.tic_tac_toe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class TicTacToeView extends View
{
    // +-----------+
    // | ATTRIBUTS |
    // +-----------+

    private int                 cellSize;
    private int                 nbCell  ;
    private int                 offsetX ;
    private int                 offsetY ;
    private Paint               paint   ;
    private TicTacToeGame       game    ;
    private OnCellClickListener listener;
    private TextView            txtWin  ;




    // +--------------+
    // | CONSTRUCTEUR |
    // +--------------+

    public TicTacToeView(Context context, int nbCell, TicTacToeGame game, TextView txtWin)
    {
        super(context);
        paint = new Paint();

        if(nbCell < 0)
        {
            nbCell = 3;
        }

        this.nbCell = nbCell;
        this.game = game;
        this.txtWin = txtWin;
    }




    // +--------------------------+
    // | MÉTHODES DU CYCLE DE VIE |
    // +--------------------------+

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        cellSize = Math.min(width, height) / (this.nbCell + 1);

        int gridSize = cellSize * this.nbCell;
        this.offsetX = (width - gridSize) / 2;
        this.offsetY = (height - gridSize) / 2;

        canvas.drawColor(0xFFF0E7D8);

        paint.setColor(0xFFA63A50);
        paint.setStrokeWidth(10);

        for (int i = 1; i < this.nbCell; i++)
        {
            canvas.drawLine(this.offsetX + i * cellSize, this.offsetY, this.offsetX + i * cellSize, this.offsetY + gridSize, paint);
            canvas.drawLine(this.offsetX, this.offsetY + i * cellSize, this.offsetX + gridSize, this.offsetY + i * cellSize, paint);
        }

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

        if (!this.game.isWin() && !this.game.isGameBlocked())
        {
            this.txtWin.setText("Tour : " + this.game.getJoueur());
        }
        else if (this.game.isWin())
        {
            this.txtWin.setText("Gagnant : " + this.game.getJoueur());
        }
        else
        {
            this.txtWin.setText("Partie bloquée ! Match nul");
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            int col = (int) ((event.getX() - this.offsetX) / this.cellSize);
            int row = (int) ((event.getY() - this.offsetY) / this.cellSize);

            if (col >= 0 && col < this.nbCell && row >= 0 && row < this.nbCell)
            {
                if (this.game.getMap()[row][col] == '\u0000')
                {
                    if (this.listener != null)
                    {
                        this.listener.onCellClick(row, col);
                    }
                }
            }
        }
        return true;
    }




    // +--------------------+
    // | MÉTHODES PUBLIQUES |
    // +--------------------+

    private void drawX(Canvas canvas, int col, int row)
    {
        paint.setColor(0xFFAB9B96);
        float startX = this.offsetX + col * cellSize;
        float startY = this.offsetY + row * cellSize;

        float spacing = (float) (cellSize * 0.1);

        canvas.drawLine(startX + spacing, startY + spacing, startX + cellSize - spacing, startY + cellSize - spacing, paint);
        canvas.drawLine(startX + spacing, startY + cellSize - spacing, startX + cellSize - spacing, startY + spacing, paint);
    }



    private void drawO(Canvas canvas, int col, int row)
    {
        paint.setColor(0xFFAB9B96);
        float centerX = this.offsetX + col * cellSize + cellSize / 2;
        float centerY = this.offsetY + row * cellSize + cellSize / 2;
        canvas.drawCircle(centerX, centerY, (float) (cellSize / 2.5), paint);
    }



    public void setOnCellClickListener(OnCellClickListener listener)
    {
        this.listener = listener;
    }



    public interface OnCellClickListener { void onCellClick(int row, int col); }



    public void updateFrame()
    {
        invalidate();
    }



    public void updateGrid(int row, int col)
    {
        this.game.setMap(row,col);
        invalidate();
    }
}
