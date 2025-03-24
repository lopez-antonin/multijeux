package com.antonin.multijeux.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.antonin.multijeux.R;
import com.antonin.multijeux.games.game_of_life.GameOfLifeGame;

public class GameOfLifeActivity extends AppCompatActivity
{
    // +--------------------------+
    // | MÉTHODES DU CYCLE DE VIE |
    // +--------------------------+

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_of_life);

        ImageButton btnBack  = findViewById(R.id.btnBack );
        Button      btnStart = findViewById(R.id.btnStart);

        btnBack.setOnClickListener(view ->
        {
            Intent intent = new Intent(GameOfLifeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnStart.setOnClickListener(view -> startGame(20, 0.5));
    }




    // +------------------+
    // | MÉTHODES PRIVÉES |
    // +------------------+

    private void startGame(int size, double density)
    {
        Intent intent = new Intent(GameOfLifeActivity.this, GameOfLifeGame.class);
        intent.putExtra("SIZE", size);
        intent.putExtra("DENSITY", density);
        startActivity(intent);
        finish();
    }
}