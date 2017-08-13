package com.etiennefrank.screenshotlib;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import java.io.File;

/*
 * License: Apache 2.0
 * Author: Etienne "ice-blaze" Frank
 * GitHub: https://github.com/ice-blaze/
 * Personal website: http://etiennefrank.com/
 * Stack Overflow: https://stackoverflow.com/users/3012928/ice-blaze/
 * Email: mail@etiennefrank.com
 */

public class ScreenshotManager {
    @TargetApi(Build.VERSION_CODES.M)
    public static void askPermission(Activity activity) {
        if (activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static int screenshotCount(Activity activity) {
        ScreenshotManager.askPermission(activity);

        File pix = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File screenshots = new File(pix, "Screenshots");
        return screenshots.list().length;
    }
}
