package com.antonin.multijeux.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * SensorHelper est une classe utilitaire qui gère l'utilisation de l'accéléromètre.
 *
 * - Écoute les mouvements du capteur et transmet les valeurs de déplacement.
 * - Peut être utilisée dans différents jeux nécessitant un contrôle par mouvement.
 *
 * Cette classe est actuellement uniquement utilisée dans CurveTrackingGame.
 */

public class SensorHelper implements SensorEventListener
{
    // +------------------+
    // | ATTRIBUTS PRIVÉS |
    // +------------------+

    private SensorManager    sensorManager;

    private Sensor           accelerometer;

    private MovementListener listener     ;




    // +-------------------+
    // | INTERFACE INTERNE |
    // +-------------------+

    /**
     * Interface pour écouter les mouvements détectés par l'accéléromètre.
     * Doit être implémentée par la classe utilisant SensorHelper.
     */
    public interface MovementListener
    {
        void onMovement(float dx, float dy);
    }




    // +--------------+
    // | CONSTRUCTEUR |
    // +--------------+

    public SensorHelper(Context context, MovementListener listener)
    {
        this.listener = listener;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }




    // +--------------------+
    // | MÉTHODES PUBLIQUES |
    // +--------------------+

    /**
     * Enregistre l'écouteur du capteur d'accéléromètre.
     */
    public void register()
    {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }



    /**
     * Désenregistre l'écouteur du capteur pour économiser la batterie.
     */
    public void unregister()
    {
        sensorManager.unregisterListener(this);
    }




    // +----------------------+
    // | CALLBACKS DU CAPTEUR |
    // +----------------------+

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        float dx = -event.values[0]; // gauche-droite
        float dy =  event.values[1]; // haut-bas
        listener.onMovement(dx, dy);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }
}
