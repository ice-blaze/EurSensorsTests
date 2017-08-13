package com.etiennefrank.testasup;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.etiennefrank.flashlightlib.FlashlightManager;
import com.etiennefrank.lightsensorlib.LightSensorManager;
import com.etiennefrank.movingmanager.MovingManager;
import com.etiennefrank.screenshotlib.ScreenshotManager;
import com.etiennefrank.stepcounterlib.StepcounterManager;

/*
 * License: Apache 2.0
 * Author: Etienne "ice-blaze" Frank
 * GitHub: https://github.com/ice-blaze/
 * Personal website: http://etiennefrank.com/
 * Stack Overflow: https://stackoverflow.com/users/3012928/ice-blaze/
 * Email: mail@etiennefrank.com
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final StepcounterManager stepcounterManager = new StepcounterManager(this);
        final MovingManager movingManager = new MovingManager(this, 1000, 3000, 0.8f, 1.8f, true);
        final LightSensorManager lightsensorManager = new LightSensorManager(this);
        final FlashlightManager flashlightManager = new FlashlightManager(this);

        final Button button = (Button) findViewById(R.id.myButton);
        button.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            public void onClick(View v) {
//                TextView test = (TextView) findViewById(R.id.tutu);
//                test.setText(Integer.toString(stepCount));
                TextView test = (TextView) findViewById(R.id.tutu);
//                test.setText("gyro \n" + axisX.mean() + "\n" + axisY.mean() + "\n" + axisZ.mean() );
            }
        });

        final MainActivity mainActivity = this;

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView test = (TextView) findViewById(R.id.tutu);
                                test.setText(
                                    "sensors " +
                                    "\n movingManager: " + Long.toString(movingManager.getListTrueTime()) +
                                    "\n stepcounter: " + Integer.toString(stepcounterManager.getSteps()) +
                                    "\n screenshortManager: " + Integer.toString(ScreenshotManager.screenshotCount(mainActivity)) +
                                    "\n lightsensorManager: " + Float.toString(lightsensorManager.getLux()) +
                                    "\n flashlightManager: " + flashlightManager.flashlightStatus() +
//                                    "\nfast: " + Long.toString(movingManager2.getListTrueTime()) +
//                                    "\n" + Integer.toString(movingManager.getStepCount()) +
//                                    "\n" + getListTrueTime(listActive) +
//                                    "\n" + isActive +
//                                    "\n" + (axisZ.mean() + axisX.mean()) +
                                    ""
                                );
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
