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


        Button btnEasy = findViewById(R.id.btnEasy);

        ImageButton btnBack = findViewById(R.id.btnBack);

        btnEasy.setOnClickListener(view -> {
            Intent intent = new Intent(TicTacToeActivity.this, TicTacToeGame.class);
            startActivity(intent);
            finish();
        });

        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(TicTacToeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}