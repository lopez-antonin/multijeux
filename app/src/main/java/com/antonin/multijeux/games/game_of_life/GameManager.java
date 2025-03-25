package com.antonin.multijeux.games.game_of_life;

import com.antonin.multijeux.games.game_of_life.model.Grid;

import java.util.LinkedList;

public class GameManager
{
    // +------------------+
    // | ATTRIBUTS PRIVÉS |
    // +------------------+

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

        // Si l'historique est moins de 3, on ne peut pas tester les cycles périodiques
        if (gridHistory.size() < 3) {
            return false;
        }

        // Récupération des 3 dernières grilles
        Grid firstGrid = gridHistory.get(0);
        Grid secondGrid = gridHistory.get(1);
        Grid thirdGrid = gridHistory.get(2);

        // Condition 1 : Vérification du jeu qui ne bouge plus (deux grilles identiques consécutives)
        if (firstGrid.equals(secondGrid)) {
            endGame();
            return true;
        }

        // Condition 2 : Vérification d'un jeu périodique (A, B, A dans les 3 dernières grilles)
        if (firstGrid.equals(thirdGrid)) {
            endGame();
            return true;
        }

        // Condition 3 : Vérification du nombre d'itérations
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
