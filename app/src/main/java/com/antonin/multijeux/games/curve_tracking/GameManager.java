package com.antonin.multijeux.games.curve_tracking;

import android.os.Handler;

/**
 * La classe GameManager est responsable de la gestion de l'état du jeu, y compris le score du joueur,
 * la complétion du jeu et les conditions de fin de partie. Elle gère également la mise à jour de l'interface
 * utilisateur du jeu et déclenche l'effet de clignotement lorsque la partie se termine.
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

    /**
     * Constructeur pour le GameManager.
     * Initialise le gestionnaire de jeu avec l'activité de jeu fournie,
     * définit le score initial à 100 et l'état de fin de partie (game over) à faux.
     *
     * @param gameActivity L'activité CurveTrackingGame associée à ce gestionnaire de jeu.
     */
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
     * Met à jour le score actuel en soustrayant une pénalité, en s'assurant que le score reste non négatif,
     * en l'arrondissant à une décimale, et en mettant à jour le score affiché dans l'activité du jeu.
     *
     * @param penalty Le montant à soustraire du score actuel. Cette valeur doit être non négative.
     *                Si une valeur négative est passée, le score augmentera.
     */
    public void updateScore(double penalty)
    {
        score -= penalty                        ;
        score  = Math.max  (score,  0   )       ;
        score  = Math.round(score * 10.0) / 10.0;

        gameActivity.updateScoreText(String.valueOf(score));
    }



    /**
     * Vérifie si le jeu est terminé en fonction de la proximité du joueur par rapport au centre de la vue du jeu.
     * Le jeu est considéré comme terminé si le joueur a parcouru une distance au moins égale à la circonférence
     * du cercle tracé et si sa position actuelle est proche de son point de départ.
     * @param gameView La CurveTrackingView représentant la zone de jeu. Cette vue fournit les dimensions
     *                 et la position actuelle du joueur.
     * @return True si le jeu est terminé (le joueur a fait le tour du cercle et est proche de son point de départ),
     * false sinon.
     */
    public boolean isGameCompleted(CurveTrackingView gameView)
    {
        if (gameOver) return true;

        int radius = Math.min(gameView.getWidth(), gameView.getHeight()) / 2;
        double circumference = 2 * Math.PI * radius;

        if (gameView.getDistanceTraveled() < circumference)
        {
            return false;
        }

        float deltaX = gameView.getPlayerX() - gameView.getStartPlayerX();
        float deltaY = gameView.getPlayerY() - gameView.getStartPlayerY();

        float distance = (float) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

        if (distance < 20)
        {
            endGame();
            return true;
        }

         return false;
    }



    /**
     * Vérifie si la partie est terminée.
     *
     * @return {@code true} si la partie est terminée, {@code false} sinon.
     */
    public boolean isGameOver()
    {
        return gameOver;
    }




    // +------------------+
    // | MÉTHODES PRIVÉES |
    // +------------------+

    /**
     * Termine la partie en cours.
     *
     * Cette méthode effectue les actions nécessaires pour conclure la partie, notamment :
     * 1. Définir le drapeau 'gameOver' sur true, indiquant que la partie est terminée.
     * 2. Arrêter le processus de dessin dans la GameView pour empêcher tout rendu supplémentaire.
     * 3. Lancer un effet de clignotement pour signaler visuellement la fin de la partie au joueur.
     *
     * Cette méthode doit être appelée lorsque la condition de fin de partie est remplie.
     */
    private void endGame()
    {
        gameOver = true;
        gameActivity.getGameView().stopDrawing();
        startBlinkingEffect();
    }



    /**
     * Démarre un effet de clignotement pour le chemin du joueur lorsque la partie est terminée.
     *
     * Cette méthode utilise un Handler et un Runnable pour créer un effet de clignotement temporisé.
     * Lorsque la partie est terminée (gameOver est vrai), la visibilité du chemin du joueur
     * est activée et désactivée de manière répétée avec un délai de 500ms entre chaque basculement.
     *
     * L'effet de clignotement continue jusqu'à ce que le drapeau `gameOver` soit défini sur faux
     * (vraisemblablement ailleurs dans le code).
     *
     * @see Handler
     * @see Runnable
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
