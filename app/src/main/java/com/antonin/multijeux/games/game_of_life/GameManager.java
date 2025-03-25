package com.antonin.multijeux.games.game_of_life;

import com.antonin.multijeux.games.game_of_life.model.Grid;

import java.util.LinkedList;

public class GameManager
{
    // +-----------+
    // | ATTRIBUTS |
    // +-----------+

    private GameOfLifeGame gameActivity;

    private int            iterations  ;

    private boolean        gameOver    ;




    // +--------------+
    // | CONSTRUCTEUR |
    // +--------------+

    public GameManager(GameOfLifeGame gameActivity)
    {
        this.gameActivity = gameActivity;
        this.iterations   = 0           ;
        this.gameOver     = false       ;
    }




    // +--------------------+
    // | MÉTHODES PUBLIQUES |
    // +--------------------+

    public boolean isGameCompleted(GameOfLifeView gameView)
    {
        if (gameOver) return true;

        LinkedList<Grid> gridHistory = gameView.getGridHistory();

        if (gridHistory.size() < 3)
        {
            return false;
        }

        Grid firstGrid = gridHistory.get(0);
        Grid secondGrid = gridHistory.get(1);
        Grid thirdGrid = gridHistory.get(2);

        if (firstGrid.equals(secondGrid))
        {
            endGame();
            return true;
        }

        if (firstGrid.equals(thirdGrid))
        {
            endGame();
            return true;
        }

        if (this.iterations >= 1000)
        {
            endGame();
            return true;
        }

        return false;
    }



    private void endGame()
    {
        gameOver = true;
        gameActivity.getGameView().stopDrawing();
    }



    public void updateIterations()
    {
        this.iterations++;
        gameActivity.runOnUiThread(() -> gameActivity.updateIterationsText(iterations));
    }
}
