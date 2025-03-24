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

    /**
     * L'activité principale pour le jeu de suivi de courbe.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_curve_tracking);

        gameView = new CurveTrackingView(this, getIntent().getIntExtra("LEVEL", 1));

        FrameLayout gameContainer = findViewById(R.id.gameContainer);
        gameContainer.addView(gameView);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view ->
        {
            Intent intent = new Intent(CurveTrackingGame.this, CurveTrackingActivity.class);
            startActivity(intent);
            finish();
        });

        ImageButton btnReplay = findViewById(R.id.btnReplay);
        btnReplay.setOnClickListener(view ->
        {
            Intent intent = new Intent(CurveTrackingGame.this, CurveTrackingGame.class);
            intent.putExtra("LEVEL", getLevel());
            startActivity(intent);
            finish();
        });

        TextView textLevel = findViewById(R.id.textLevel);
        textLevel.setText(getString(R.string.level) + " " + getLevel());

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

    public ImageButton getBtnReplay()
    {
        return findViewById(R.id.btnReplay);
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



    /**
     * Met à jour le texte affiché dans le TextView "Meilleur Score".
     *
     * Cette méthode prend une chaîne de caractères représentant le meilleur score
     * et la définit comme contenu textuel du TextView avec l'ID `textBestScore`.
     * Cela permet une mise à jour dynamique du meilleur score affiché à l'utilisateur.
     *
     * @param bestScoreText La chaîne de caractères à afficher comme meilleur score.
     *                      Cette chaîne doit généralement représenter le score le
     *                      plus élevé atteint par l'utilisateur. Par exemple "Meilleur: 100".
     * @throws NullPointerException Si aucun TextView avec R.id.textBestScore n'a été trouvé dans la mise en page.
     * @throws IllegalArgumentException Si bestScoreText est null.
     */
    public void updateBestScoreText(String bestScoreText)
    {
        TextView textBestScore = findViewById(R.id.textBestScore);
        textBestScore.setText(bestScoreText);
    }
}

