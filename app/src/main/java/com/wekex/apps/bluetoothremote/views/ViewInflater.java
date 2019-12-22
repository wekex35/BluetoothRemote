package com.wekex.apps.bluetoothremote.views;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wekex.apps.bluetoothremote.R;
import com.wekex.apps.bluetoothremote.constants.StaticVariables;
import com.wekex.apps.bluetoothremote.helper.move_zoom_rotate;
import com.wekex.apps.bluetoothremote.helper.sharedprefs;
import com.wekex.apps.bluetoothremote.helper.view_move_zoom_rotate;
import com.wekex.apps.bluetoothremote.model.ViewButtons;

import org.json.JSONException;
import org.json.JSONObject;

import static com.wekex.apps.bluetoothremote.constants.Constants.TAG;
import static com.wekex.apps.bluetoothremote.constants.StaticVariables.BUTTONARRAYS;
import static com.wekex.apps.bluetoothremote.constants.StaticVariables.CURRENT_LAYOUT_NAME;
import static com.wekex.apps.bluetoothremote.constants.StaticVariables.getScreenHeight;
import static com.wekex.apps.bluetoothremote.constants.StaticVariables.getScreenWidth;
import static com.wekex.apps.bluetoothremote.constants.StaticVariables.isEditing;
import static com.wekex.apps.bluetoothremote.constants.utils.dpToPx;
import static com.wekex.apps.bluetoothremote.helper.jsonhelper.addToButtonArrays;
import static com.wekex.apps.bluetoothremote.helper.jsonhelper.intJsonreaderFromString;
import static com.wekex.apps.bluetoothremote.helper.jsonhelper.view_json_info;
import static com.wekex.apps.bluetoothremote.helper.jsonhelper.view_json_row_info;

public class ViewInflater {

    public static View addViewLayEditor(Activity context){
        View view = LayoutInflater.from(context).inflate(R.layout.view_textview,null,false);
        TextView imageView = view.findViewById(R.id.image);

        imageView.setOnTouchListener(new view_move_zoom_rotate(context));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(dpToPx(50,context),dpToPx(50,context));
        imageView.setLayoutParams(layoutParams);
        imageView.setTag(BUTTONARRAYS.length());
        addToButtonArrays(BUTTONARRAYS.length(),view_json_info(100, 100, dpToPx(50,context), dpToPx(50,context), '0'));
        return view;
    }

    public static void populateEditor(Activity context, FrameLayout parent){
        ViewButtons vB;
        if (BUTTONARRAYS == null)
            return;
       for (int i = 0;i<BUTTONARRAYS.length();i++) {
           try {
               Log.d(TAG, "populateEditor: "+BUTTONARRAYS.getJSONObject(i).toString());
               vB = new ViewButtons(BUTTONARRAYS.getJSONObject(i));

           View view = LayoutInflater.from(context).inflate(R.layout.view_textview, null, false);
           TextView imageView = view.findViewById(R.id.image);
           imageView.setOnTouchListener(new view_move_zoom_rotate(context));
               RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(dpToPx(vB.getHeight(),context),dpToPx(vB.getWidth(),context));
               //intJsonreaderFromString(jsonString,VIEW_WIDTH), intJsonreaderFromString(jsonString,VIEW_HEIGHT)
               layoutParams.leftMargin = dpToPx(vB.getLeftMargin(),context);
               layoutParams.topMargin =  dpToPx(vB.gettopmargin(),context);

           imageView.setLayoutParams(layoutParams);
           imageView.setRotation((float) vB.getRoattion());
           imageView.setTag(i);
           parent.addView(view);
           } catch (JSONException e) {
               e.printStackTrace();
               Log.d(TAG, "populateEditor: Error"+e.getCause());
           }
       }
    }



    public static View addImageViewButton(Activity context){
        View view = LayoutInflater.from(context).inflate(R.layout.view_textview,null,false);
        TextView imageView = view.findViewById(R.id.image);
        imageView.setOnTouchListener(new move_zoom_rotate(context));
        Log.d(TAG, "addImageViewButton: "+StaticVariables.ID_NO);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(dpToPx(50,context),dpToPx(50,context));
        imageView.setLayoutParams(layoutParams);

        imageView.setTag(StaticVariables.ID_NO);
        if (StaticVariables.ID_NO == 0){
            Log.d(TAG, view_json_row_info(view_json_info(100, 100, dpToPx(50,context), dpToPx(50,context), '0'), "1", 0).toString());
            sharedprefs.save(context, CURRENT_LAYOUT_NAME, String.valueOf(view_json_row_info(view_json_info(100, 100, view.getHeight(), view.getWidth(), '0'),"1",Integer.parseInt(view.getTag().toString()))));
        }else {
            String jstring = sharedprefs.read(context,CURRENT_LAYOUT_NAME);
            String here =  String.valueOf(view_json_row_info(view_json_info(100, 100, dpToPx(50,context), dpToPx(50,context),'0'),jstring,Integer.parseInt(view.getTag().toString())));
            Log.d(TAG, here);
            sharedprefs.save(context,CURRENT_LAYOUT_NAME,here);
        }
        StaticVariables.ID_NO++;
        return view;
    }


    public static View ViewInflaterPopulate(final Activity context, String jsonString){
        Log.d(TAG, "ViewInflaterPopulate: "+jsonString);
        int width= getScreenWidth();
        int height= getScreenHeight();
        View view = LayoutInflater.from(context).inflate(R.layout.view_textview,null,false);
        TextView imageView =  view.findViewById(R.id.image);

        if(isEditing)
        imageView.setOnTouchListener(new move_zoom_rotate(context));
        else{
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Create", Toast.LENGTH_SHORT).show();
                }
            });
        }


        imageView.setTag(StaticVariables.ID_NO);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                dpToPx(intJsonreaderFromString(jsonString,"height"),context),
                dpToPx(intJsonreaderFromString(jsonString,"width"),context));
        //intJsonreaderFromString(jsonString,VIEW_WIDTH), intJsonreaderFromString(jsonString,VIEW_HEIGHT)
        layoutParams.leftMargin = dpToPx(intJsonreaderFromString(jsonString,"leftmargin"),context);
        layoutParams.topMargin =  dpToPx(intJsonreaderFromString(jsonString,"topmargin"),context);
        imageView.setLayoutParams(layoutParams);
        StaticVariables.ID_NO++;
        return view;
    }

}

