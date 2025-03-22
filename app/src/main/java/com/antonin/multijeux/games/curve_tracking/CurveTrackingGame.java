package com.antonin.multijeux.games.curve_tracking;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;
import com.antonin.multijeux.R;
import com.antonin.multijeux.utils.SensorHelper;

public class CurveTrackingGame extends Activity {

    private CurveTrackingView gameView;
    private SensorHelper sensorHelper;
    private int difficultyLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_curve_tracking);

        difficultyLevel = getIntent().getIntExtra("LEVEL", 1);

        gameView = new CurveTrackingView(this, difficultyLevel);

        FrameLayout gameContainer = findViewById(R.id.gameContainer);
        gameContainer.addView(gameView);

        sensorHelper = new SensorHelper(this, gameView::updateMovement);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorHelper != null) {
            sensorHelper.register();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorHelper != null) {
            sensorHelper.unregister();
        }
    }
}