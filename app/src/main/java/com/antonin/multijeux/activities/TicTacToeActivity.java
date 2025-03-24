package com.antonin.multijeux.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.antonin.multijeux.R;
import com.antonin.multijeux.games.tic_tac_toe.TicTacToeGame;

public class TicTacToeActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);


        Button btnPVP = findViewById(R.id.btnPVP);
        Button btnEasy = findViewById(R.id.btnEasy);
        Button btnMedium = findViewById(R.id.btnMedium);
        Button btnImpossible = findViewById(R.id.btnImpossible);

        ImageButton btnBack = findViewById(R.id.btnBack);

        btnPVP.setOnClickListener(view -> {startGame(0);});
        btnEasy.setOnClickListener(view -> {startGame(1);});
        btnMedium.setOnClickListener(view -> {startGame(2);});
        btnImpossible.setOnClickListener(view -> {startGame(3);});

        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(TicTacToeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }


    private void startGame(int lvl)
    {
        Intent intent = new Intent(TicTacToeActivity.this, TicTacToeGame.class);
        intent.putExtra("LEVEL", lvl);
        startActivity(intent);
        finish();
    }
}