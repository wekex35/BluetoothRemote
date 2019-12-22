package com.wekex.apps.bluetoothremote;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.wekex.apps.bluetoothremote.bluetooth.BluetoothChatService;
import com.wekex.apps.bluetoothremote.bluetooth.datahandler;
import com.wekex.apps.bluetoothremote.constants.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import static com.wekex.apps.bluetoothremote.helper.jsonhelper.StringJsonreader;
import static com.wekex.apps.bluetoothremote.helper.jsonhelper.intJsonreader;

public class MainActivity extends AppCompatActivity {
    private Set<BluetoothDevice> mPairedDevices;
    private BluetoothAdapter mBluetoothAdapter = null;
    private ArrayAdapter<String> mBTArrayAdapter;
    private static final String TAG = "DeviceListActivity";
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    public static BluetoothChatService mChatService = null;
    private String mConnectedDeviceName = null;
    Dialog dialog;
//https://github.com/nisrulz/sensey
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mChatService = new BluetoothChatService(getBaseContext());
        mChatService.start();

        // If the adapter is null, then Bluetooth is not supported

        intiBuletooh();
    }

    public void intiBuletooh() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
    }

    public void bluetoothTerminal(View view) {
        mHandler.removeMessages(0);
        Intent intent = new Intent(this, BluetoothTerminal.class);
        intent.putExtra("address", address);
        startActivity(intent);
    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
              /*   case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
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
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);

                        Toast.makeText(MainActivity.this, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();

                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.d(TAG, msg.obj+" handleMessage: "+msg.arg1 +"arg "+readMessage);
                    break;
              case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;*/
            }
        }
    };

    private void setStatus(CharSequence subTitle) {
        setTitle(subTitle);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {
                // Launch the BluetoothDeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(this, BluetoothDeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
            case R.id.insecure_connect_scan: {
                // Launch the BluetoothDeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(this, BluetoothDeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            }
            case R.id.discoverable: {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    // setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "onActivityResult: ");
                    Toast.makeText(this, "bt_not_enabled_leaving",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        address = data.getExtras().getString(BluetoothDeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    private static String address;


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
                        Nofication(getString(R.string.title_connected_to, mConnectedDeviceName));

                        // mConversationArrayAdapter.clear();
                        break;
                    case BluetoothChatService.STATE_CONNECTING:

                        setStatus(getString(R.string.title_connecting));
                        Nofication(getString(R.string.title_connecting));

                        break;
                    case BluetoothChatService.STATE_LISTEN:
                    case BluetoothChatService.STATE_NONE:
                        Nofication("Disconnected");
                        setStatus(getString(R.string.title_not_connected));
                        break;
                }
                break;
            case Constants.MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = StringJsonreader(jsonObject, "msg");

                Toast.makeText(MainActivity.this, "Connected to "
                        + mConnectedDeviceName, Toast.LENGTH_SHORT).show();

                break;

            case Constants.MESSAGE_READ:

                String readMessage = StringJsonreader(jsonObject, "msg");
                String bytes = StringJsonreader(jsonObject, "bytes");

                //datahandler.addlayout(scrollView,messageHolder,BluetoothTerminal.this,readMessage,"RX",StringJsonreader(jsonObject, "bytes"));
                Log.d(TAG, " handleMessage2: "+readMessage);
                break;
            case Constants.MESSAGE_WRITE:
                String writeMessage = StringJsonreader(jsonObject, "msg");
                //datahandler.addlayout(scrollView,messageHolder,BluetoothTerminal.this,writeMessage,"TX",StringJsonreader(jsonObject, "bytes"));
                Log.d(TAG, " handleMessage2: "+writeMessage);
                break;
            case Constants.MESSAGE_TOAST:
                Toast.makeText(MainActivity.this, StringJsonreader(jsonObject, "msg"),
                        Toast.LENGTH_SHORT).show();

                break;

        }


    }

    private void Nofication(String message) {
        String title = "Printer";
       // String message = "Disconnected ";
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        String channelId = "fd634dgdft5";
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                // .setLargeIcon(emailObject.getSenderAvatar())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */,notificationBuilder.build());

    }


}
