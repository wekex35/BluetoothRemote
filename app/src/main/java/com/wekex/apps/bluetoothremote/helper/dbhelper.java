package com.wekex.apps.bluetoothremote.helper;

import android.app.Activity;

public class dbhelper {

    public static DatabaseHandler getDB(Activity activity){
        return new DatabaseHandler(activity);
    }
}
