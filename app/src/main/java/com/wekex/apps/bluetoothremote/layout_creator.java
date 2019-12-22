package com.wekex.apps.bluetoothremote;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wekex.apps.bluetoothremote.constants.Constants;
import com.wekex.apps.bluetoothremote.constants.StaticVariables;
import com.wekex.apps.bluetoothremote.helper.sharedprefs;
import com.wekex.apps.bluetoothremote.views.ViewInflater;
import static com.wekex.apps.bluetoothremote.constants.Constants.TAG;

import static com.wekex.apps.bluetoothremote.constants.Constants.KEY_JSONVIEW;
import static com.wekex.apps.bluetoothremote.constants.Constants.KEY_LAYOUT_NAME;
import static com.wekex.apps.bluetoothremote.constants.StaticVariables.CURRENT_LAYOUT_NAME;
import static com.wekex.apps.bluetoothremote.constants.StaticVariables.ID;
import static com.wekex.apps.bluetoothremote.constants.StaticVariables.isEditing;
import static com.wekex.apps.bluetoothremote.constants.utils.dpToPx;
import static com.wekex.apps.bluetoothremote.helper.dbhelper.getDB;
import static com.wekex.apps.bluetoothremote.helper.jsonhelper.jsonArraytoView;
import static com.wekex.apps.bluetoothremote.helper.jsonhelper.view_json_info;
import static com.wekex.apps.bluetoothremote.helper.jsonhelper.view_json_row_info;

public class layout_creator extends AppCompatActivity {

    ImageView im_move_zoom_rotate;
    Toolbar toolbar;

    private ImageView im_move_zoom_rotate2;
    RelativeLayout parent;
    Button editing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StaticVariables.ID_NO = 0;
        setContentView(R.layout.activity_layout_creator);
        parent = findViewById(R.id.parent);
        editing = findViewById(R.id.editing);
        Log.d(TAG, parent.getHeight()+" onCreate: "+parent.getWidth());
        ID = Integer.parseInt(getIntent().getStringExtra("layou_id"));

        Log.d(TAG, "onCreate: "+ID);



        //getDB(this).
      //  init();
        getView();

    }

    private void getView() {
        parent.removeAllViews();
        if (ID!=0){
            Cursor cursor = getDB(this).getLayoutBYId(ID);
            cursor.moveToFirst();
            String json = cursor.getString(cursor.getColumnIndex(KEY_JSONVIEW));
            String name = cursor.getString(cursor.getColumnIndex(KEY_LAYOUT_NAME));
            CURRENT_LAYOUT_NAME = name+ID;
            sharedprefs.save(this,CURRENT_LAYOUT_NAME,"");
            if (!json.equals("")) {
                String jsons = cursor.getString(cursor.getColumnIndex(KEY_JSONVIEW));
                sharedprefs.save(this,CURRENT_LAYOUT_NAME,jsons);
                jsonArraytoView(this, jsons, parent);
            }
         //   Log.d(TAG,  cursor.getString(cursor.getColumnIndex(KEY_LAYOUT_NAME))+" isExist: "+CURRENT_LAYOUT_NAME+" "+ cursor.getString(cursor.getColumnIndex(KEY_JSONVIEW)));
        }
    }

   /* private void init() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(250, 250);
        im_move_zoom_rotate = (ImageView) findViewById(R.id.imageView);
        im_move_zoom_rotate2 = (ImageView) findViewById(R.id.imageView2);
        String value = sharedprefs.read(layout_creator.this,"params2");
        if (!Constants.EMPTY.equals(value)){
            Log.d(TAG, "init: "+value);
            layoutParams.leftMargin = jsonhelper.intJsonreaderFromString(value,"rightMargin");
            layoutParams.topMargin = jsonhelper.intJsonreaderFromString(value,"bottomMargin");
        }else {
            layoutParams.leftMargin = 50;
            layoutParams.topMargin = 50;
            layoutParams.bottomMargin = -250;
            layoutParams.rightMargin = -250;
        }
        im_move_zoom_rotate.setLayoutParams(layoutParams);
        im_move_zoom_rotate.setOnTouchListener(new move_zoom_rotate(this));
        im_move_zoom_rotate2.setOnTouchListener(new move_zoom_rotate(this));
    }*/


    public void addView(View view) {
        parent.addView(ViewInflater.addImageViewButton(this));
    }

    public void AddtoDB(View view) {
      /*  if(!getDB(this).isExist(CURRENT_LAYOUT_NAME)){
            getDB(this).addLayout(CURRENT_LAYOUT_NAME, sharedprefs.read(this, CURRENT_LAYOUT_NAME));
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
             Log.d(TAG, "AddtoDB: "+ sharedprefs.read(this, CURRENT_LAYOUT_NAME));
            getDB(this).updateLayout(ID, sharedprefs.read(this, CURRENT_LAYOUT_NAME).replaceAll("null,",""));
        }else {*/
        if (isEditing){
            editing.setText("EDIT");
            isEditing = false;
            getView();
        }else {
            editing.setText("DONE");
            isEditing = true;
            getView();
        }
            Toast.makeText(this, "Update", Toast.LENGTH_SHORT).show();

        //}
    }

}
