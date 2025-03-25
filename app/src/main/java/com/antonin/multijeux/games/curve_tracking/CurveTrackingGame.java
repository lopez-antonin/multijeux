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

public class CurveTrackingGame extends Activity
{
    // +-----------+
    // | ATTRIBUTS |
    // +-----------+

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
        if (sensorHelper != null)
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

    public void updateScoreText(String scoreText)
    {
        TextView textScore = findViewById(R.id.textScore);
        textScore.setText(scoreText);
    }



    public void updateBestScoreText(String bestScoreText)
    {
        TextView textBestScore = findViewById(R.id.textBestScore);
        textBestScore.setText(bestScoreText);
    }
}

