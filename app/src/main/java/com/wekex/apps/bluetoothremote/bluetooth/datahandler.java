package com.wekex.apps.bluetoothremote.bluetooth;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wekex.apps.bluetoothremote.R;
import static android.content.ContentValues.TAG;
import static com.wekex.apps.bluetoothremote.constants.Constants.TIME;

public class datahandler {

    public static void addlayout(ScrollView scrollView, LinearLayout msgHolder, Context context, String readMessage, String tx,String length) {

        View view = LayoutInflater.from(context).inflate(R.layout.terminal_item,null,false);
        TextView msg = view.findViewById(R.id.msg);
        TextView time = view.findViewById(R.id.time);
        TextView msglength = view.findViewById(R.id.msglength);
        TextView rxtx = view.findViewById(R.id.rxtx);
        msg.setText(readMessage);

        msglength.setText(length);
        rxtx.setText(tx);
        time.setText(TIME);
        Log.d(TAG, "addlayout: " + readMessage);
        msgHolder.addView(view);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
