package com.antonin.multijeux.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.antonin.multijeux.R;

public class TicTacToeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        ImageButton btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(TicTacToeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}