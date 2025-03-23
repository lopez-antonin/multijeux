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

/**
 * CurveTrackingGame est l'activité principale du jeu de suivi de courbe.
 *
 * - Initialise la vue du jeu et l'affiche dans un conteneur.
 * - Gère la communication entre la vue et le GameManager.
 * - Utilise SensorHelper pour capter les mouvements du joueur.
 * - Met à jour l'affichage du score et gère le retour à l'écran principal.
 *
 * Cette classe orchestre le déroulement du jeu et l'interface utilisateur.
 */
public class CurveTrackingGame extends Activity
{
    // +------------------+
    // | ATTRIBUTS PRIVÉS |
    // +------------------+

    private CurveTrackingView gameView    ;

    private GameManager       gameManager ;

    private SensorHelper      sensorHelper;




    // +--------------------------+
    // | MÉTHODES DU CYCLE DE VIE |
    // +--------------------------+

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_curve_tracking);

        FrameLayout gameContainer = findViewById(R.id.gameContainer);
        gameContainer.addView(gameView);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view ->
        {
            Intent intent = new Intent(CurveTrackingGame.this, CurveTrackingActivity.class);
            startActivity(intent);
            finish();
        });

        TextView textLevel = findViewById(R.id.textLevel);
        textLevel.setText(getString(R.string.level) + " " + getLevel());

        gameView = new CurveTrackingView(this, getIntent().getIntExtra("LEVEL", 1));
        gameManager = new GameManager(this);
        sensorHelper = new SensorHelper(this, gameView::updateMovement);
    }



    @Override
    protected void onPause()
    {
        super.onPause();
        if (sensorHelper != null)
        {
            sensorHelper.unregister();
        }
    }



    @Override
    protected void onResume()
    {
        super.onResume();
        if (sensorHelper != null && !gameManager.isGameOver())
        {
            sensorHelper.register();
        }
    }




    // +---------+
    // | GETTERS |
    // +---------+

    public GameManager getGameManager()
    {
        return gameManager;
    }

    public int getLevel()
    {
        return getIntent().getIntExtra("LEVEL", 1);
    }

    public CurveTrackingView getGameView()
    {
        return gameView;
    }




    // +--------------------+
    // | MÉTHODES PUBLIQUES |
    // +--------------------+

    /**
     * Met à jour le texte affiché dans le TextView du score.
     *
     * Cette méthode prend une chaîne de caractères représentant le score et la définit comme
     * contenu textuel du TextView avec l'ID 'textScore' défini dans le fichier de mise en page
     * (R.id.textScore).
     *
     * @param scoreText Le nouveau texte du score à afficher. Il doit s'agir d'une chaîne de
     *                  caractères représentant le score actuel, par exemple, "Score : 10", "0"
     *                  ou "Meilleur score : 100".
     * @throws NullPointerException Si le TextView avec l'ID 'textScore' est introuvable dans
     *                              la mise en page actuelle.
     */
    public void updateScoreText(String scoreText)
    {
        TextView textScore = findViewById(R.id.textScore);
        textScore.setText(scoreText);
    }
}

