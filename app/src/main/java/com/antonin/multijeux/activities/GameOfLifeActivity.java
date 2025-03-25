package com.antonin.multijeux.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

        ImageButton btnBack     = findViewById(R.id.btnBack    );
        Button      btnStart    = findViewById(R.id.btnStart   );
        EditText    editDensity = findViewById(R.id.editDensity);
        EditText    editSize    = findViewById(R.id.editSize   );


        if (getIntent().hasExtra("SIZE")) {
            int size = getIntent().getIntExtra("SIZE", 20);
            editSize.setText(String.valueOf(size));
        }

        if (getIntent().hasExtra("DENSITY")) {
            double density = getIntent().getDoubleExtra("DENSITY", 0.5);
            editDensity.setText(String.valueOf(density));
        }

        btnBack.setOnClickListener(view ->
        {
            Intent intent = new Intent(GameOfLifeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnStart.setOnClickListener(view ->
        {
            if (!editDensity.getText().toString().isEmpty() && !editSize.getText().toString().isEmpty())
            {
                double density = Double.parseDouble(editDensity.getText().toString());
                int size = Integer.parseInt(editSize.getText().toString());

                if (density >= 0 && density <= 1 && size > 0)
                {
                    startGame(size, density);
                }
            }

        });

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