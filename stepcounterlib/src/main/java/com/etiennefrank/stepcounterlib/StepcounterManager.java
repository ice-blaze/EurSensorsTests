package com.etiennefrank.stepcounterlib;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/*
 * License: Apache 2.0
 * Author: Etienne "ice-blaze" Frank
 * GitHub: https://github.com/ice-blaze/
 * Personal website: http://etiennefrank.com/
 * Stack Overflow: https://stackoverflow.com/users/3012928/ice-blaze/
 * Email: mail@etiennefrank.com
 */

public class StepcounterManager implements SensorEventListener {
    private final SensorManager mSensorManager;
    private final Sensor stepSensor;
    private int steps = 0;

    public StepcounterManager (Activity activity) {
        mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);

        stepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mSensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public int getSteps() {
        return this.steps;
    }

    public void resetSteps() {
        steps = 0;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            steps++;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
