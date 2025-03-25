package com.antonin.multijeux.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import com.antonin.multijeux.R;
import com.antonin.multijeux.games.tic_tac_toe.TicTacToeGame;

public class TicTacToeActivity extends AppCompatActivity
{
    // +--------------------------+
    // | MÉTHODES DU CYCLE DE VIE |
    // +--------------------------+

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        Button btnPVP        = findViewById(R.id.btnPVP       );
        Button btnEasy       = findViewById(R.id.btnEasy      );
        Button btnMedium     = findViewById(R.id.btnMedium    );
        Button btnImpossible = findViewById(R.id.btnImpossible);
        ImageButton btnBack  = findViewById(R.id.btnBack      );
        EditText editSize    = findViewById(R.id.editSize     );

        editSize.setText("3"); // Valeur par défaut
        editSize.setSelection(editSize.getText().length());

        editSize.setOnFocusChangeListener((v, hasFocus) ->
        {
            if (!hasFocus) {
                int value = getValidSize(editSize);
                editSize.setText(String.valueOf(value));
            }
        });

        btnPVP       .setOnClickListener(view -> { startGame(0, getValidSize(editSize)); });
        btnEasy      .setOnClickListener(view -> { startGame(1, getValidSize(editSize)); });
        btnMedium    .setOnClickListener(view -> { startGame(2, getValidSize(editSize)); });
        btnImpossible.setOnClickListener(view -> { startGame(3, getValidSize(editSize)); });

        btnBack.setOnClickListener(view ->
        {
            Intent intent = new Intent(TicTacToeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }




    // +------------------+
    // | MÉTHODES PRIVÉES |
    // +------------------+

    private void startGame(int lvl, int tailleCases)
    {
        Intent intent = new Intent(TicTacToeActivity.this, TicTacToeGame.class);
        intent.putExtra("LEVEL", lvl);
        intent.putExtra("TAILLE", tailleCases);
        startActivity(intent);
        finish();
    }



    private int getValidSize(EditText editText)
    {
        try
        {
            int size = Integer.parseInt(editText.getText().toString());
            if (size < 3)
            {
                return 3;
            }
            else if (size > 8)
            {
                return 8;
            }
            return size;
        }
        catch (NumberFormatException e)
        {
            return 3; // Mauvaise entrée
        }
    }
}
