package com.antonin.multijeux.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.antonin.multijeux.R;

public class GameOfLifeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_of_life);

        ImageButton btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(GameOfLifeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}