package com.antonin.multijeux.games.curve_tracking;

import android.os.Handler;

/**
 * GameManager gère la logique du jeu et le système de score.
 *
 * - Diminue le score lorsque le joueur sort du chemin.
 * - Vérifie si le jeu est terminé en analysant la position du joueur.
 * - Déclenche l'effet de clignotement lorsque le joueur termine le parcours.
 * - Empêche la mise à jour du score après la fin du jeu.
 *
 * Cette classe est le coeur du gameplay et de la gestion de l'état du jeu.
 */

public class GameManager
{
    // +------------------+
    // | ATTRIBUTS PRIVÉS |
    // +------------------+

    private CurveTrackingGame gameActivity;

    private double            score       ;

    private boolean           gameOver    ;




    // +--------------+
    // | CONSTRUCTEUR |
    // +--------------+

    public GameManager(CurveTrackingGame gameActivity)
    {
        this.gameActivity = gameActivity;
        this.score        = 100         ;
        this.gameOver     = false       ;
    }




    // +--------------------+
    // | MÉTHODES PUBLIQUES |
    // +--------------------+

    /**
     * Met à jour le score du joueur en appliquant une pénalité.
     */
    public void updateScore(double penalty)
    {
        score -= penalty                        ;
        score  = Math.max  (score,  0   )       ;
        score  = Math.round(score * 10.0) / 10.0;

        gameActivity.updateScoreText(String.valueOf(score));
    }



    /**
     * Vérifie si le joueur a complété le niveau.
     */
    public boolean isGameCompleted(CurveTrackingView gameView)
    {
        int centerX = gameView.getWidth () / 2;
        int centerY = gameView.getHeight() / 2;

        float deltaX = gameView.getPlayerX() - centerX;
        float deltaY = gameView.getPlayerY() - centerY;

        float distance = (float) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

        if (distance < 20)
        {
            endGame();
            return true;
        }

        return false;
    }



    /**
     * Indique si la partie est terminée.
     */
    public boolean isGameOver()
    {
        return gameOver;
    }




    // +------------------+
    // | MÉTHODES PRIVÉES |
    // +------------------+

    /**
     * Met fin à la partie en arrêtant les mouvements du joueur et
     * en lançant l'effet de clignotement
     */
    private void endGame()
    {
        gameOver = true;
        gameActivity.getGameView().stopDrawing();
        startBlinkingEffect();
    }



    /**
     * Fait clignoter le tracé du joueur toutes les 500ms après la fin du jeu.
     */
    private void startBlinkingEffect()
    {
        Handler handler = new Handler();

        Runnable blinkRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                if (gameOver)
                {
                    gameActivity.getGameView().togglePlayerPathVisibility();
                    handler.postDelayed(this, 500);  // 500ms
                }
            }
        };

        handler.post(blinkRunnable);
    }
}
