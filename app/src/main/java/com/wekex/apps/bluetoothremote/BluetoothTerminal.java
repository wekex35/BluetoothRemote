package com.wekex.apps.bluetoothremote;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.wekex.apps.bluetoothremote.bluetooth.BluetoothChatService;
import com.wekex.apps.bluetoothremote.bluetooth.datahandler;
import com.wekex.apps.bluetoothremote.constants.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import static com.wekex.apps.bluetoothremote.MainActivity.mChatService;
import static com.wekex.apps.bluetoothremote.helper.jsonhelper.StringJsonreader;
import static com.wekex.apps.bluetoothremote.helper.jsonhelper.intJsonreader;

public class BluetoothTerminal extends AppCompatActivity {
    private StringBuffer mOutStringBuffer;
    private LinearLayout messageHolder;
    private EditText mOutEditText;
    private ImageView mSendButton;
    private ScrollView scrollView;

    private String mConnectedDeviceName = null;
    Dialog dialog;
    private static final String TAG = "Terminal";
    private BluetoothAdapter mBluetoothAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_terminal);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        messageHolder =  findViewById(R.id.messageHolder);
        mOutEditText = (EditText) findViewById(R.id.edit_text_out);
        scrollView = findViewById(R.id.scrollView);
        mSendButton = findViewById(R.id.button_send);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget

                   // TextView textView = (TextView) view.findViewById(R.id.edit_text_out);
                    String message = mOutEditText.getText().toString();
                    sendMessage(message);

            }
        });

        mOutStringBuffer = new StringBuffer("");
        String addr = getIntent().getStringExtra("address");
        //connectDevice(addr,true);
    }
    private void connectDevice(String address, boolean secure) {
        // Get the device MAC address

        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
    }



    private void setStatus(CharSequence subTitle) {
        setTitle(subTitle);
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        registerReceiver(broadcastReceiver, new IntentFilter(BluetoothChatService.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private void updateUI(Intent intent) {
//        findViewById(R.id.progressBar).setVisibility(View.GONE);
        String Rdata = intent.getStringExtra("datafromService");
        Log.d(TAG, "updateUI: "+Rdata);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(Rdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int what = intJsonreader(jsonObject, "what");
        Log.d(TAG, "updateUI:what "+what);
        switch (what) {
            case Constants.MESSAGE_STATE_CHANGE:
                int what2 = intJsonreader(jsonObject, "bytes");
                Log.d(TAG, "updateUI:what2 "+what);
                switch (what2) {
                    case BluetoothChatService.STATE_CONNECTED:
                        setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                        // mConversationArrayAdapter.clear();
                        break;
                    case BluetoothChatService.STATE_CONNECTING:
                        setStatus(getString(R.string.title_connecting));
                        break;
                    case BluetoothChatService.STATE_LISTEN:
                    case BluetoothChatService.STATE_NONE:
                        setStatus(getString(R.string.title_not_connected));
                        break;
                }
                break;
            case Constants.MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = StringJsonreader(jsonObject, "msg");

                Toast.makeText(BluetoothTerminal.this, "Connected to "
                        + mConnectedDeviceName, Toast.LENGTH_SHORT).show();

                break;

            case Constants.MESSAGE_READ:

                String readMessage = StringJsonreader(jsonObject, "msg");
                String bytes = StringJsonreader(jsonObject, "bytes");

                datahandler.addlayout(scrollView,messageHolder,BluetoothTerminal.this,readMessage,"RX",StringJsonreader(jsonObject, "bytes"));
                Log.d(TAG, " handleMessage2: "+readMessage);
                break;
            case Constants.MESSAGE_WRITE:
                String writeMessage = StringJsonreader(jsonObject, "msg");
                datahandler.addlayout(scrollView,messageHolder,BluetoothTerminal.this,writeMessage,"TX",StringJsonreader(jsonObject, "bytes"));
                Log.d(TAG, " handleMessage2: "+writeMessage);
                break;
            case Constants.MESSAGE_TOAST:
                    Toast.makeText(BluetoothTerminal.this, StringJsonreader(jsonObject, "msg"),
                            Toast.LENGTH_SHORT).show();

                break;

        }


    }
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
       if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
           // return;
        }
        Toast.makeText(this,String.valueOf(mChatService.getState()), Toast.LENGTH_SHORT).show();

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);
            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            mOutEditText.setText(mOutStringBuffer);
        }
    }
}
