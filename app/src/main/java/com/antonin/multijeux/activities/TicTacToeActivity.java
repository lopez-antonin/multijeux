package com.antonin.multijeux.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;

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

        NumberPicker tailleCases = findViewById(R.id.nbCases);

        tailleCases.setMinValue(3);  // Valeur minimale
        tailleCases.setMaxValue(8);  // Valeur maximale
        tailleCases.setValue(3);     // Valeur par défaut

        tailleCases.setTextColor(getResources().getColor(R.color.antique_white));
        tailleCases.setBackgroundColor(getResources().getColor(R.color.cinereous));
        tailleCases.getChildAt(0).setBackgroundColor(getResources().getColor(R.color.cinereous));

        ImageButton btnBack = findViewById(R.id.btnBack);

        btnPVP.setOnClickListener(view -> {startGame(0, tailleCases.getValue());});
        btnEasy.setOnClickListener(view -> {startGame(1, tailleCases.getValue());});
        btnMedium.setOnClickListener(view -> {startGame(2, tailleCases.getValue());});
        btnImpossible.setOnClickListener(view -> {startGame(3, tailleCases.getValue());});

        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(TicTacToeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }


    private void startGame(int lvl, int tailleCases)
    {
        Intent intent = new Intent(TicTacToeActivity.this, TicTacToeGame.class);
        intent.putExtra("LEVEL", lvl);
        intent.putExtra("TAILLE", tailleCases);
        startActivity(intent);
        finish();
    }
}