package com.antonin.multijeux.games.curve_tracking;

import android.content.SharedPreferences;
import android.os.SystemClock;

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
    private double            bestScore   ;

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

        loadBestScore();

        // Mise à jour de l'UI sur le thread principal
        bestScore = Math.round(bestScore * 10.0) / 10.0;
        gameActivity.runOnUiThread(() -> gameActivity.updateBestScoreText(String.valueOf(bestScore)));
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

        int    w       = gameView.getWidth ()       ;
        int    h       = gameView.getHeight()       ;
        double radius  = (Math.min(w, h) / 3) / 1.5 ;

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

        startEndGameAnimation();

        if (score > bestScore)
        {
            bestScore = score;
            saveBestScore();

            // Mise à jour de l'UI sur le thread principal
            bestScore = Math.round(bestScore * 10.0) / 10.0;
            gameActivity.runOnUiThread(() -> gameActivity.updateBestScoreText(String.valueOf(bestScore)));

        }
    }



    /**
     * Enregistre le meilleur score actuel dans les préférences partagées.
     *
     * Cette méthode persiste la valeur de la variable 'bestScore' dans les
     * préférences partagées de l'appareil, ce qui permet de la récupérer
     * même après la fermeture et la réouverture de l'application. Le meilleur
     * score est stocké sous forme de flottant (float) avec la clé "bestScore"
     * dans un fichier de préférences partagées nommé "GamePrefs".
     *
     * La méthode `apply()` est utilisée pour enregistrer les modifications de
     * manière asynchrone, améliorant ainsi les performances en évitant de
     * bloquer le thread principal.
     *
     * @see SharedPreferences
     * @see SharedPreferences.Editor
     * @see SharedPreferences.Editor#putFloat(String, float)
     * @see SharedPreferences.Editor#apply()
     */
    private void saveBestScore()
    {
        int level = gameActivity.getGameView().getLevel();

        SharedPreferences sharedPreferences = gameActivity.getSharedPreferences("GamePrefs", gameActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("bestScore" + level, (float) bestScore);
        editor.apply();
    }



    /**
     * Charge le meilleur score à partir des préférences partagées.
     */
    private void loadBestScore()
    {
        int level = gameActivity.getGameView().getLevel();

        SharedPreferences sharedPreferences = gameActivity.getSharedPreferences("GamePrefs", gameActivity.MODE_PRIVATE);
        bestScore = sharedPreferences.getFloat("bestScore" + level, 0);
    }



    /**
     * Démarre la séquence d'animation de fin de partie.
     *
     * Cette méthode lance une animation de clignotement pour le chemin du joueur sur le plateau de jeu,
     * signalant la fin de la partie. Le chemin clignotera en s'affichant et en disparaissant pendant une durée de 3 secondes.
     * Une fois l'animation de clignotement terminée, le bouton "Rejouer" devient visible et activé,
     * permettant à l'utilisateur de démarrer une nouvelle partie.
     *
     * L'animation est exécutée dans un thread séparé pour éviter de bloquer le thread principal de l'interface utilisateur.
     * L'effet de clignotement est obtenu en basculant de manière répétée la visibilité du chemin du joueur
     * avec un intervalle défini.
     *
     * Le bouton "Rejouer" est affiché et activé uniquement une fois l'animation de clignotement terminée.
     */
    private void startEndGameAnimation()
    {
        new Thread(() ->
        {
            long startTime = SystemClock.uptimeMillis();
            long duration = 3000;
            long blinkInterval = 500;
            while (SystemClock.uptimeMillis() - startTime < duration)
            {
                // Mise à jour de l'UI sur le thread principal
                gameActivity.runOnUiThread(() -> gameActivity.getGameView().togglePlayerPathVisibility());
                try
                {
                    Thread.sleep(blinkInterval);
                }
                catch (InterruptedException ignored) {}
            }
        }).start();
    }
}
