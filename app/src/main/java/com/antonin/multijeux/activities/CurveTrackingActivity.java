package com.antonin.multijeux.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

import com.antonin.multijeux.R;
import com.antonin.multijeux.games.curve_tracking.CurveTrackingGame;

public class CurveTrackingActivity extends AppCompatActivity
{
    // +--------------------------+
    // | MÉTHODES DU CYCLE DE VIE |
    // +--------------------------+

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curve_tracking);

        ImageButton btnBack   = findViewById(R.id.btnBack  );
        Button      btnLevel1 = findViewById(R.id.btnLevel1);
        Button      btnLevel2 = findViewById(R.id.btnLevel2);
        Button      btnLevel3 = findViewById(R.id.btnLevel3);
        Button      btnLevel4 = findViewById(R.id.btnLevel4);

        btnBack.setOnClickListener(view ->
        {
            Intent intent = new Intent(CurveTrackingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnLevel1.setOnClickListener(view -> startGame(1));
        btnLevel2.setOnClickListener(view -> startGame(2));
        btnLevel3.setOnClickListener(view -> startGame(3));
        btnLevel4.setOnClickListener(view -> startGame(4));
    }




    // +------------------+
    // | MÉTHODES PRIVÉES |
    // +------------------+

    private void startGame(int level)
    {
        Intent intent = new Intent(CurveTrackingActivity.this, CurveTrackingGame.class);
        intent.putExtra("LEVEL", level);
        startActivity(intent);
        finish();
    }
}
