package com.wekex.apps.bluetoothremote.helper;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.wekex.apps.bluetoothremote.constants.Constants.LEFT_MARGIN;
import static com.wekex.apps.bluetoothremote.constants.Constants.TOP_MARGIN;
import static com.wekex.apps.bluetoothremote.constants.Constants.VIEW_HEIGHT;
import static com.wekex.apps.bluetoothremote.constants.Constants.VIEW_ROTATION;
import static com.wekex.apps.bluetoothremote.constants.Constants.VIEW_WIDTH;
import static com.wekex.apps.bluetoothremote.constants.StaticVariables.BUTTONARRAYS;
import static com.wekex.apps.bluetoothremote.views.ViewInflater.ViewInflaterPopulate;

public class jsonhelper {
    static String TAG = "jsonhelper";



    public static String StringJsonreader(JSONObject jsonObject, String msg) {

        try {
            return jsonObject.getString(msg);
        } catch (JSONException e) {
            e.printStackTrace();
            return "error";
        }
    }
    public static JSONArray stringJsonToArray(String msg) {

        try {
            return new JSONArray(msg);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String StringJsonreaderFromString(String jsonString, String msg) {

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject.getString(msg);
        } catch (JSONException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public static int intJsonreaderFromString(String jsonString, String msg) {

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return (int) jsonObject.getInt(msg);
        } catch (JSONException e) {
            e.printStackTrace();
            return Integer.parseInt("0");
        }
    }

    public static int intJsonreader(JSONObject jsonObject, String what) {

        try {
            return (int) jsonObject.get(what);
        } catch (JSONException e) {
            e.printStackTrace();
            return Integer.parseInt("0");
        }
    }

    public static JSONObject view_json_info(float leftMargin, float topMargin, int height, int width, char c){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put(LEFT_MARGIN, leftMargin);
            jsonObject.put(TOP_MARGIN,topMargin);
            jsonObject.put(VIEW_HEIGHT,height);
            jsonObject.put(VIEW_WIDTH,width);
            //jsonObject.put()
            return jsonObject;
        }catch (Exception e){
            Log.d(TAG, "view_json_info: "+e.getMessage());
            return jsonObject;

        }
    }
    public static JSONObject view_json_info_editor(int lm, int tm, int vh, int vw, float r){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put(LEFT_MARGIN,lm);
            jsonObject.put(TOP_MARGIN,tm);
            jsonObject.put(VIEW_HEIGHT,vh);
            jsonObject.put(VIEW_WIDTH,vw);
            jsonObject.put(VIEW_ROTATION,r);
            //jsonObject.put()
            Log.d(TAG, "view_json_info: "+jsonObject.toString());
            return jsonObject;
        }catch (Exception e){
            Log.d(TAG, "view_json_info: "+e.getMessage());
            return jsonObject;

        }
    }

    public static String addToButtonArrays(int pos,JSONObject jsonObject){

        try {

            BUTTONARRAYS.put(pos,jsonObject);
            Log.d(TAG, "addToButtonArrays: "+BUTTONARRAYS.toString());
            return BUTTONARRAYS.toString();
        }catch (Exception e){
            Log.d(TAG, "view_json_info: "+e.getMessage());
            return "addToButtonArrays Error";

        }
    }

    public static JSONArray view_json_row_info(JSONObject jsnObject,String rowJson,int id){
        JSONArray jsonArray = null;
        Log.d(TAG, rowJson+"view_json_row_info: "+jsnObject.toString());
        try {
            if (rowJson.equals("1")){
                jsonArray = new JSONArray();
                jsonArray.put(jsnObject);
            }else if(rowJson.equals("2")){
                jsonArray = new JSONArray(rowJson);
                jsonArray.put(jsnObject);
            }else {
                Log.d(TAG, "view_json_row_info: "+id);
                jsonArray = new JSONArray(rowJson);
                jsonArray.put(id,jsnObject);
            }

            //jsonObject.put()
            return jsonArray;
        }catch (Exception e){
            Log.d(TAG, e.toString()+" view_json_info: "+e.getMessage());
            return jsonArray;

        }
    }

    public static void jsonArraytoView(Activity context, String fromDBjsonView, RelativeLayout parent){
        try {
            JSONArray array = new JSONArray(fromDBjsonView);
            for (int i=0;i<array.length();i++){
                Log.d(TAG, "jsonArraytoView: " + array.getString(i));
                if (array.getString(i) != null) {
                    String jsonString = array.getString(i);
                    View view = ViewInflaterPopulate(context, jsonString);
                    parent.addView(view);
                 }
              }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static JSONObject getJSONobject(String jsonString){
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
