package com.antonin.multijeux.games.game_of_life;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.antonin.multijeux.R;
import com.antonin.multijeux.activities.GameOfLifeActivity;
import com.antonin.multijeux.games.curve_tracking.CurveTrackingGame;
import com.antonin.multijeux.games.curve_tracking.CurveTrackingView;

public class GameOfLifeGame extends Activity {
    // +------------------+
    // | ATTRIBUTS PRIVÉS |
    // +------------------+

    private GameOfLifeView gameView;

    private GameManager gameManager;


    // +--------------------------+
    // | MÉTHODES DU CYCLE DE VIE |
    // +--------------------------+

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_game_of_life);

        gameView = new GameOfLifeView(this, getIntent().getIntExtra("SIZE", 20), getIntent().getDoubleExtra("DENSITY", 0.5));

        FrameLayout gameContainer = findViewById(R.id.gameContainer);
        gameContainer.addView(gameView);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view ->
        {
            Intent intent = new Intent(GameOfLifeGame.this, GameOfLifeActivity.class);
            intent.putExtra("SIZE", getSize());
            intent.putExtra("DENSITY", getDensity());
            startActivity(intent);
            finish();
        });

        ImageButton btnReplay = findViewById(R.id.btnReplay);
        btnReplay.setOnClickListener(view ->
        {
            Intent intent = new Intent(GameOfLifeGame.this, GameOfLifeGame.class);
            intent.putExtra("SIZE", getSize());
            intent.putExtra("DENSITY", getDensity());
            startActivity(intent);
            finish();
        });

        gameManager = new GameManager(this);
    }


    // +---------+
    // | GETTERS |
    // +---------+

    public GameManager getGameManager() {
        return gameManager;
    }

    public int getSize() {
        return getIntent().getIntExtra("SIZE", 20);
    }

    public double getDensity() {
        return getIntent().getDoubleExtra("DENSITY", 0.5);
    }




    // +--------------------+
    // | MÉTHODES PUBLIQUES |
    // +--------------------+

    public void updateIterationsText(int iterations)
    {
        TextView textIterations = findViewById(R.id.textIterations);
        textIterations.setText(String.valueOf(iterations));
    }

    public GameOfLifeView getGameView() {
        return gameView;
    }
}
