package com.antonin.multijeux.games.game_of_life;

import com.antonin.multijeux.games.curve_tracking.CurveTrackingGame;

public class GameManager
{
    // +------------------+
    // | ATTRIBUTS PRIVÉS |
    // +------------------+

    private GameOfLifeGame gameActivity;




    // +--------------+
    // | CONSTRUCTEUR |
    // +--------------+

    public GameManager(GameOfLifeGame gameActivity)
    {
        this.gameActivity = gameActivity;
    }
}
