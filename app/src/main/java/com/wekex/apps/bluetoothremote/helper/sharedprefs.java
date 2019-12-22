package com.wekex.apps.bluetoothremote.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.wekex.apps.bluetoothremote.R;
import com.wekex.apps.bluetoothremote.constants.Constants;

public class sharedprefs {


    public static SharedPreferences createprefs(Context context) {
        return context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE);
    }

    public static void save(Context context, String key, String valve) {
        createprefs(context).edit().putString(key,valve).apply();
    }
    public static String read(Context context, String key) {
       return createprefs(context).getString(key, Constants.EMPTY);
    }
}
