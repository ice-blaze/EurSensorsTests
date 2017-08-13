package com.etiennefrank.movingmanager;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/*
 * License: Apache 2.0
 * Author: Etienne "ice-blaze" Frank
 * GitHub: https://github.com/ice-blaze/
 * Personal website: http://etiennefrank.com/
 * Stack Overflow: https://stackoverflow.com/users/3012928/ice-blaze/
 * Email: mail@etiennefrank.com
 */

public class MovingManager implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor accSensor;
    private Sensor stepSensor;
    private List<Pair<Long, Boolean>> listActive;
    private int stepCount = 0;

    private FixedSizedQueue axisX;
    private FixedSizedQueue axisY;
    private FixedSizedQueue axisZ;
    private Activity activity;
    private float minLinear;
    private float maxLinear;

    public MovingManager(
            Activity activity,
            final int queueSize,
            final long timeRefresh,
            final float minLinear,
            final float maxLinear,
            final boolean withAbs
    ) {
        listActive = new ArrayList<>();

        mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);

        accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_FASTEST);

        stepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mSensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);

        axisX = new FixedSizedQueue(queueSize, withAbs);
        axisY = new FixedSizedQueue(queueSize, withAbs);
        axisZ = new FixedSizedQueue(queueSize, withAbs);
        this.activity = activity;
        this.minLinear = minLinear;
        this.maxLinear = maxLinear;

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(timeRefresh);
                        boolean isActive =
                                isLinearActive(axisX.mean()) ||
                                isLinearActive(axisY.mean()) ||
                                isLinearActive(axisZ.mean());
                        listActive.add(new Pair<>(System.currentTimeMillis() / 1000, isActive));
                    }
                } catch (InterruptedException ignored) { }
            }
        };
        t.start();
    }

    class FixedSizedQueue {
        Queue<Float> q = new ConcurrentLinkedQueue<>();

        final int limit;
        final boolean withAbs;

        public FixedSizedQueue() {
            this.limit = 100;
            this.withAbs = true;
        }

        public FixedSizedQueue(int limit, boolean withAbs) {
            this.limit = limit;
            this.withAbs = withAbs;
        }

        synchronized void Enqueue(float obj) {
            q.add(obj);
            if (q.size() > limit) {
                q.remove();
            }
        }

        synchronized float mean() {
            float sum = 0;

            for (float val : q) {
                if(this.withAbs) {
                    sum += Math.abs(val);
                } else {
                    sum += val;
                }
            }

            sum = Math.abs(sum);
            return Float.parseFloat(String.format("%.1f", sum / q.size()));
        }
    }

    private boolean isLinearActive(float val) {
        return val > this.minLinear && val < this.maxLinear;
    }

    private List<Pair<Long, Boolean>> deepCopy(List<Pair<Long, Boolean>> list) {
        List<Pair<Long, Boolean>>  copy = new ArrayList<>();

        for(Pair<Long, Boolean> pair : list) {
            copy.add(pair);
        }

        return copy;
    }

    public int getStepCount() {
        return this.stepCount;
    }

    public long getListTrueTime() {
        // at least 3 input (so 3 seconds)
        if(this.listActive.size() < 3) {
            return 0;
        }

        long trueTime = 0;
        List<Pair<Long, Boolean>> doubleTrueList = deepCopy(this.listActive);

        for(int i=doubleTrueList.size()-2; i > 1; --i) {
            if(
                doubleTrueList.get(i - 1).second &&
                doubleTrueList.get(i).second &&
                doubleTrueList.get(i + 1).second
            ) {
                doubleTrueList.remove(i);
                --i;
            }
        }

        for(int i = 0; i < doubleTrueList.size() - 1; i++) {
            Pair<Long, Boolean> pair1 = doubleTrueList.get(i);
            Pair<Long, Boolean> pair2 = doubleTrueList.get(i + 1);

            if(pair1.second && pair2.second) {
                trueTime += pair2.first - pair1.first;
            }
        }

        return trueTime;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        switch(sensor.getType()) {
            case Sensor.TYPE_LINEAR_ACCELERATION:
                axisX.Enqueue(event.values[0]);
                axisY.Enqueue(event.values[1]);
                axisZ.Enqueue(event.values[2]);
                break;
            case Sensor.TYPE_STEP_COUNTER:
                stepCount++;
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
