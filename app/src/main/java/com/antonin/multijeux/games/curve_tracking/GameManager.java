package com.antonin.multijeux.games.curve_tracking;

import android.os.Handler;

public class GameManager {
    private CurveTrackingGame gameActivity;
    private double score;
    private boolean gameOver;

    public GameManager(CurveTrackingGame gameActivity) {
        this.gameActivity = gameActivity;
        this.score = 100;
        this.gameOver = false;
    }

    // Cette méthode est appelée depuis l'activité pour mettre à jour le score
    public void updateScore(double penalty) {
        score -= penalty;
        score = Math.max(score, 0);
        score = Math.round(score * 10.0) / 10.0;
        gameActivity.updateScoreText(String.valueOf(score));  // Met à jour le score sur l'UI
    }

    // Cette méthode vérifie si le jeu est terminé
    public boolean isGameCompleted(CurveTrackingView gameView) {
        int centerX = gameView.getWidth() / 2;
        int centerY = gameView.getHeight() / 2;

        float distance = (float) Math.sqrt(Math.pow(gameView.getPlayerX() - centerX, 2) + Math.pow(gameView.getPlayerY() - centerY, 2));
        if (distance < 20) {
            endGame();
            return true;
        }
        return false;
    }

    private void endGame() {
        gameOver = true;
        gameActivity.getGameView().stopDrawing();  // Stoppe le dessin
        startBlinkingEffect();
    }

    private void startBlinkingEffect() {
        Handler handler = new Handler();
        Runnable blinkRunnable = new Runnable() {
            @Override
            public void run() {
                if (gameOver) {
                    gameActivity.getGameView().togglePlayerPathVisibility();
                    handler.postDelayed(this, 500);  // Clignote toutes les 500ms
                }
            }
        };
        handler.post(blinkRunnable);
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
