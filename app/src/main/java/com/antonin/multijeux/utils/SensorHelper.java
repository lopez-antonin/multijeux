package com.antonin.multijeux.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

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

    public void register()
    {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }



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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
