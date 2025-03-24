package com.antonin.multijeux.games.game_of_life;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.antonin.multijeux.R;
import com.antonin.multijeux.activities.GameOfLifeActivity;

public class GameOfLifeGame extends Activity
{
    // +------------------+
    // | ATTRIBUTS PRIVÉS |
    // +------------------+

    private GameOfLifeView    gameView   ;

    private GameManager       gameManager;




    // +--------------------------+
    // | MÉTHODES DU CYCLE DE VIE |
    // +--------------------------+

    /**
     * L'activité principale pour le jeu de suivi de courbe.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_game_of_life);

        gameView = new GameOfLifeView(this, getIntent().getIntExtra("SIZE", 20), getIntent().getDoubleExtra("DENSITY", 0.5));

        FrameLayout gameContainer = findViewById(R.id.gameContainer);
        gameContainer.addView(gameView);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view ->
        {
            Intent intent = new Intent(GameOfLifeGame.this, GameOfLifeActivity.class);
            startActivity(intent);
            finish();
        });

        gameManager = new GameManager(this);
    }




    // +---------+
    // | GETTERS |
    // +---------+

    public GameManager getGameManager() { return gameManager; }

    public int getSize() { return getIntent().getIntExtra("SIZE", 20); }

    public double getDensity() { return getIntent().getDoubleExtra("DENSITY", 0.5); }

    public GameOfLifeView getGameView()
    {
        return gameView;
    }

}
