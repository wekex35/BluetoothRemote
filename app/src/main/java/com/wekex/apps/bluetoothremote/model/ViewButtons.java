package com.wekex.apps.bluetoothremote.model;

import android.content.Context;

import com.wekex.apps.bluetoothremote.helper.view_move_zoom_rotate;

import org.json.JSONException;
import org.json.JSONObject;

import static com.wekex.apps.bluetoothremote.constants.Constants.LEFT_MARGIN;
import static com.wekex.apps.bluetoothremote.constants.Constants.TOP_MARGIN;
import static com.wekex.apps.bluetoothremote.constants.Constants.VIEW_HEIGHT;
import static com.wekex.apps.bluetoothremote.constants.Constants.VIEW_ROTATION;
import static com.wekex.apps.bluetoothremote.constants.Constants.VIEW_WIDTH;

public class ViewButtons {
    int leftMargin,topmargin;
    double roattion,Height,Width;

    public ViewButtons(int leftMargin, int topmargin, int height, int width) {
        this.leftMargin = leftMargin;
        topmargin = topmargin;
        Height = height;
        Width = width;
    }
    public ViewButtons(JSONObject jo) {

        try {
        this.leftMargin =jo.getInt(LEFT_MARGIN);
            this.topmargin = jo.getInt(TOP_MARGIN);
            this.Height = jo.getDouble(VIEW_HEIGHT);
            this.Width = jo.getDouble(VIEW_WIDTH);
            this.roattion= jo.getDouble(VIEW_ROTATION);
        }catch (JSONException e){

        }
    }

    public ViewButtons(Context context) {
    }

    public double getRoattion() {
        return roattion;
    }

    public void setRoattion(double roattion) {
        this.roattion = roattion;
    }

    public int getLeftMargin() {
        return leftMargin;
    }

    public void setLeftMargin(int leftMargin) {
        this.leftMargin = leftMargin;
    }

    public int gettopmargin() {
        return topmargin;
    }

    public void settopmargin(int topmargin) {
        topmargin = topmargin;
    }

    public double getHeight() {
        return Height;
    }

    public void setHeight(double height) {
        Height = height;
    }

    public double getWidth() {
        return Width;
    }

    public void setWidth(double width) {
        Width = width;
    }
}
