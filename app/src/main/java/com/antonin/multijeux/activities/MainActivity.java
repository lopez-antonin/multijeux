package com.antonin.multijeux.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.antonin.multijeux.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnTicTacToe = findViewById(R.id.btnTicTacToe);
        Button btnGameOfLife = findViewById(R.id.btnGameOfLife);
        Button btnCurveTracking = findViewById(R.id.btnCurveTracking);

        btnTicTacToe.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TicTacToeActivity.class);
            startActivity(intent);
        });

        btnGameOfLife.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, GameOfLifeActivity.class);
            startActivity(intent);
        });

        btnCurveTracking.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CurveTrackingActivity.class);
            startActivity(intent);
        });
    }
}
