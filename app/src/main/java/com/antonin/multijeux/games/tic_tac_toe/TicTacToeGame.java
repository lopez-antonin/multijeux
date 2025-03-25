package com.antonin.multijeux.games.tic_tac_toe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.antonin.multijeux.R;
import com.antonin.multijeux.activities.MainActivity;
import com.antonin.multijeux.activities.TicTacToeActivity;


public class TicTacToeGame extends Activity
{
    // +-----------+
    // | ATTRIBUTS |
    // +-----------+

    private TicTacToeView  ticTacToeView ;

    private TicTacToeLogic ticTacToeLogic;




    // +--------------------------+
    // | MÉTHODES DU CYCLE DE VIE |
    // +--------------------------+

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_tic_tac_toe);

        this.ticTacToeLogic = new TicTacToeLogic(getIntent().getIntExtra("TAILLE", 3), this, this);

        this.ticTacToeView = new TicTacToeView(this, getIntent().getIntExtra("TAILLE", 3), this, findViewById(R.id.textGagner));

        ImageButton btnBack = findViewById(R.id.btnBack);

        FrameLayout frameLayout = findViewById(R.id.gameContainerTicTacToe);
        frameLayout.addView(this.ticTacToeView);

        TextView txtLevel = findViewById(R.id.textLevel);

        switch(getIntent().getIntExtra("LEVEL", 0))
        {
            case 0 : txtLevel.setText("Level : JcJ");       this.ticTacToeLogic.setLevel(0); break;
            case 1 : txtLevel.setText("Level : Facile");    this.ticTacToeLogic.setLevel(1); break;
            case 2 : txtLevel.setText("Level : Moyen");     this.ticTacToeLogic.setLevel(2); break;
            case 3 : txtLevel.setText("Level : Impossible");this.ticTacToeLogic.setLevel(3); break;
        }

        ticTacToeView.setOnCellClickListener((row, col) ->
        {
            if (!this.ticTacToeLogic.isAIPlaying())
            {
                ticTacToeView.updateGrid(row, col);
            }
            else
            {
                Toast.makeText(this, "L'IA est en train de réfléchir, patientez... ", Toast.LENGTH_LONG);
            }
        });

        btnBack.setOnClickListener(view ->
        {
            Intent intent = new Intent(TicTacToeGame.this, TicTacToeActivity.class);
            startActivity(intent);
            finish();
        });
    }


    // +-------------------+
    // | GETTERS / SETTERS |
    // +-------------------+

    public char[][] getMap() { return this.ticTacToeLogic.getMap(); }

    public char getJoueur() { return this.ticTacToeLogic.getJoueur(); }

    public boolean isWin() { return this.ticTacToeLogic.isWin(); }

    public boolean isGameBlocked() { return this.ticTacToeLogic.isGameBlocked(); }

    public void setMap(int lig, int col) { this.ticTacToeLogic.setMap(lig, col); }




    // +--------------------+
    // | MÉTHODES PUBLIQUES |
    // +--------------------+

    public void updateFrame() { this.ticTacToeView.updateFrame(); }
}
