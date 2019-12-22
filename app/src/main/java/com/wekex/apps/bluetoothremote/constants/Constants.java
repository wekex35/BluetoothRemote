/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wekex.apps.bluetoothremote.constants;

import com.wekex.apps.bluetoothremote.bluetooth.BluetoothChatService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Defines several constants used between {@link BluetoothChatService} and the UI.
 */
public interface Constants {
    String TAG = "bluetoothremote";

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;




    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";



    public static final String EMPTY = "empty";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_LAYOUT_NAME = "layout_name";
    public static final String KEY_JSONVIEW = "jsonview";

    //View Info
    String TOP_MARGIN = "topmargin";
    String LEFT_MARGIN = "leftmargin";
    String VIEW_HEIGHT = "height";
    String VIEW_WIDTH = "width";
    String VIEW_ROTATION = "rotation";


    //Date and Time
    DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    DateFormat FOR_TAG_FORMAT = new SimpleDateFormat("ddMMyyyyHHmmss");

    String TIME = TIME_FORMAT.format(Calendar.getInstance().getTime());
    String FOR_SET_TAG = TIME_FORMAT.format(Calendar.getInstance().getTime());



}
