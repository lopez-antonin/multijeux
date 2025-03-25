package com.antonin.multijeux.games.curve_tracking;

import android.content.SharedPreferences;
import android.os.SystemClock;

public class GameManager
{
    // +-----------+
    // | ATTRIBUTS |
    // +-----------+

    private CurveTrackingGame gameActivity;

    private double            score       ;
    private double            bestScore   ;

    private boolean           gameOver    ;




    // +--------------+
    // | CONSTRUCTEUR |
    // +--------------+

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

    public void updateScore(double penalty)
    {
        score -= penalty                        ;
        score  = Math.max  (score,  0   )       ;
        score  = Math.round(score * 10.0) / 10.0;

        gameActivity.updateScoreText(String.valueOf(score));
    }



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



    private void saveBestScore()
    {
        int level = gameActivity.getGameView().getLevel();

        SharedPreferences sharedPreferences = gameActivity.getSharedPreferences("GamePrefs", gameActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("bestScore" + level, (float) bestScore);
        editor.apply();
    }



    private void loadBestScore()
    {
        int level = gameActivity.getGameView().getLevel();

        SharedPreferences sharedPreferences = gameActivity.getSharedPreferences("GamePrefs", gameActivity.MODE_PRIVATE);
        bestScore = sharedPreferences.getFloat("bestScore" + level, 0);
    }



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
