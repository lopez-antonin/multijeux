package com.antonin.multijeux.games.game_of_life;

import android.content.Context;
import android.view.View;

public class GameOfLifeView extends View
{
    // +------------------+
    // | ATTRIBUTS PRIVÉS |
    // +------------------+

    private int size;
    private double density;
    private boolean isRunning;




    // +--------------+
    // | CONSTRUCTEUR |
    // +--------------+

    public GameOfLifeView(Context context, int size, double density)
    {
        super(context);

        this.size = size;
        this.density = density;
    }




    // +--------------------------+
    // | MÉTHODES DU CYCLE DE VIE |
    // +--------------------------+

}
