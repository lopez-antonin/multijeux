package com.antonin.multijeux.games.curve_tracking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.antonin.multijeux.R;
import com.antonin.multijeux.activities.CurveTrackingActivity;
import com.antonin.multijeux.utils.SensorHelper;

public class CurveTrackingGame extends Activity {

    private CurveTrackingView gameView;
    private GameManager gameManager;  // Déclaration de GameManager
    private SensorHelper sensorHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_curve_tracking);

        // Initialisation de la vue de jeu et du GameManager
        gameView = new CurveTrackingView(this, getIntent().getIntExtra("LEVEL", 1));
        gameManager = new GameManager(this);

        // Ajout de la vue dans le conteneur
        FrameLayout gameContainer = findViewById(R.id.gameContainer);
        gameContainer.addView(gameView);

        // Initialisation du helper pour les capteurs
        sensorHelper = new SensorHelper(this, gameView::updateMovement);

        // Gestion des boutons et de l'interface
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(CurveTrackingGame.this, CurveTrackingActivity.class);
            startActivity(intent);
            finish();
        });

        TextView textLevel = findViewById(R.id.textLevel);
        textLevel.setText(getString(R.string.level) + " " + getLevel());
    }

    // Ajout d'un getter pour GameManager
    public GameManager getGameManager() {
        return gameManager;
    }

    // Mise à jour du score dans l'UI
    public void updateScoreText(String scoreText) {
        TextView textScore = findViewById(R.id.textScore);
        textScore.setText(scoreText);
    }

    // Cette méthode expose le niveau de l'activité
    public int getLevel() {
        return getIntent().getIntExtra("LEVEL", 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorHelper != null && !gameManager.isGameOver()) {
            sensorHelper.register();  // Enregistrement des capteurs si le jeu n'est pas terminé
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorHelper != null) {
            sensorHelper.unregister(); // Désenregistrement des capteurs lorsque l'activité est en pause
        }
    }

    public CurveTrackingView getGameView() {
        return gameView;
    }
}

