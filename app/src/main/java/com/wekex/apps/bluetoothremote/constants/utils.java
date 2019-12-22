package com.wekex.apps.bluetoothremote.constants;

import android.app.Activity;

public class utils {

    public static int dpToPx(int dp, Activity activity) {
        float density = activity.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    public static int pxtoDp(int dp, Activity activity) {
        float density = activity.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp / density);
    }

    public static int dpToPx(Double dp, Activity activity) {
        float density = activity.getResources()
                .getDisplayMetrics()
                .density;
        return (int) Math.round(dp * density);
    }

    public static int pxtoDp(Double dp, Activity activity) {
        float density = activity.getResources()
                .getDisplayMetrics()
                .density;
        return (int) Math.round(dp / density);
    }
}
