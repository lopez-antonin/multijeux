package com.antonin.multijeux.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

import com.antonin.multijeux.R;
import com.antonin.multijeux.games.curve_tracking.CurveTrackingGame;

/**
 * CurveTrackingActivity :
 * Cette activité représente l'écran de sélection de niveau pour le jeu Curve Tracking.
 * Elle permet à l'utilisateur de choisir un niveau de difficulté (de 1 à 4) et de revenir au menu principal.
 * <p>
 * Fonctionnalités principales :
 * - Affiche des boutons pour chaque niveau (Niveau 1 à Niveau 4).
 * - Fournit un bouton "Retour" pour revenir au menu principal (MainActivity).
 * - Lance l'activité CurveTrackingGame avec le niveau sélectionné.
 * <p>
 * Cycle de vie :
 * - onCreate : Initialise l'interface utilisateur, configure les écouteurs de clic des boutons et se prépare à la sélection du niveau.
 * <p>
 * Navigation :
 * - Bouton Retour : Navigue vers MainActivity.
 * - Boutons de niveau : Navigue vers CurveTrackingGame avec le niveau correspondant.
 */
public class CurveTrackingActivity extends AppCompatActivity
{
    // +--------------------------+
    // | MÉTHODES DU CYCLE DE VIE |
    // +--------------------------+

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curve_tracking);

        ImageButton btnBack = findViewById(R.id.btnBack  );
        Button btnLevel1    = findViewById(R.id.btnLevel1);
        Button btnLevel2    = findViewById(R.id.btnLevel2);
        Button btnLevel3    = findViewById(R.id.btnLevel3);
        Button btnLevel4    = findViewById(R.id.btnLevel4);

        btnBack.setOnClickListener(view ->
        {
            Intent intent = new Intent(CurveTrackingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnLevel1.setOnClickListener(view -> startGame(1));
        btnLevel2.setOnClickListener(view -> startGame(2));
        btnLevel3.setOnClickListener(view -> startGame(3));
        btnLevel4.setOnClickListener(view -> startGame(4));
    }




    // +------------------+
    // | MÉTHODES PRIVÉES |
    // +------------------+

    /**
     * Démarre l'activité CurveTrackingGame avec le niveau spécifié.
     *
     * Cette méthode crée un intent pour lancer l'activité CurveTrackingGame,
     * place le niveau sélectionné en tant qu'extra dans l'intent, démarre l'activité,
     * et termine ensuite l'activité courante (CurveTrackingActivity).
     *
     * @param level Le niveau à jouer dans CurveTrackingGame. Il doit s'agir d'un entier positif
     *              représentant la difficulté ou l'étape du jeu.
     */
    private void startGame(int level)
    {
        Intent intent = new Intent(CurveTrackingActivity.this, CurveTrackingGame.class);
        intent.putExtra("LEVEL", level);
        startActivity(intent);
        finish();
    }
}
