package com.etiennefrank.lightsensorlib;

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

public class LightSensorManager implements SensorEventListener{
    private SensorManager mSensorManager;
    private Sensor mSensorRot;
    private float lux = -1000;

    public LightSensorManager(Activity activity) {
        mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);

        mSensorRot = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(this, mSensorRot, 500);
    }

    public float getLux () {
        return lux;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float sensorData[];

        if(event.sensor.getType()== Sensor.TYPE_LIGHT) {
            sensorData = event.values.clone();
            lux = sensorData[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
