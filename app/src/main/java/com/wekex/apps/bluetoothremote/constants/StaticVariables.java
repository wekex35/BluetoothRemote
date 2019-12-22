package com.wekex.apps.bluetoothremote.constants;

import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;

import org.json.JSONArray;

public class StaticVariables {
    public static int ID_NO = 0;
    public static String CURRENT_LAYOUT_NAME;
    public static int ID;
    public static JSONArray BUTTONARRAYS;
    public static boolean isEditing = false;

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int valueTOpercentage(int margin,int size) {
        return margin*100/size;
    }
    public static int percentageTOvalue(int dist,int size) {
        return size*dist/100;
    }
}
