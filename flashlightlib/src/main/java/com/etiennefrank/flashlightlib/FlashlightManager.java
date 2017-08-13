package com.etiennefrank.flashlightlib;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.widget.Toast;

/*
 * License: Apache 2.0
 * Author: Etienne "ice-blaze" Frank
 * GitHub: https://github.com/ice-blaze/
 * Personal website: http://etiennefrank.com/
 * Stack Overflow: https://stackoverflow.com/users/3012928/ice-blaze/
 * Email: mail@etiennefrank.com
 */

public class FlashlightManager {
    private boolean isTorchOn = false;
    private final Activity activity;
    private final CameraManager camManager;
    private final CameraManager.TorchCallback torchCallback;

    @TargetApi(Build.VERSION_CODES.M)
    public FlashlightManager(Activity activity) {
        this.activity = activity;
        this.camManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        this.askPermission();
        torchCallback = new CameraManager.TorchCallback() {
            @Override
            public void onTorchModeUnavailable(String cameraId) {
                super.onTorchModeUnavailable(cameraId);
                Toast.makeText(
                        FlashlightManager.this.activity,
                        "Torch not available, can't finish the game",
                        Toast.LENGTH_LONG
                ).show();
            }

            @Override
            public void onTorchModeChanged(String cameraId, boolean enabled) {
                super.onTorchModeChanged(cameraId, enabled);
                isTorchOn = enabled;
            }
        };
        this.camManager.registerTorchCallback(torchCallback, null);
    }

    public void askPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[] {Manifest.permission.CAMERA}, 1);
            }
            if (activity.checkSelfPermission(Manifest.permission.FLASHLIGHT) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[] {Manifest.permission.FLASHLIGHT}, 1);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void setFlashlight(boolean val) {
        try{
            String cameraId = camManager.getCameraIdList()[0]; // Usually front camera is at 0 position.
            camManager.setTorchMode(cameraId, val);
        } catch (Exception e){
            Toast.makeText(
                    FlashlightManager.this.activity,
                    "Error when tried to set flashlight",
                    Toast.LENGTH_LONG
            ).show();

        }
//        legacy
//        Camera camera = Camera.open();
//        camera.getParameters().setFlashMode("off");
//        camera.getParameters().setFlashMode("torch");
    }

    public String flashlightStatus() {
        return Boolean.toString(this.isTorchOn);
//        legacy
//        Camera camera = Camera.open();
//        return camera.getParameters().getFlashMode();
    }
}
