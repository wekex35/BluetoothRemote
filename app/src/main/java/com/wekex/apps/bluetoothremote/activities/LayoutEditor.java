package com.wekex.apps.bluetoothremote.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.wekex.apps.bluetoothremote.R;
import com.wekex.apps.bluetoothremote.constants.StaticVariables;
import com.wekex.apps.bluetoothremote.helper.sharedprefs;
import com.wekex.apps.bluetoothremote.views.ViewInflater;

import org.json.JSONArray;

import static com.wekex.apps.bluetoothremote.constants.Constants.KEY_JSONVIEW;
import static com.wekex.apps.bluetoothremote.constants.Constants.KEY_LAYOUT_NAME;
import static com.wekex.apps.bluetoothremote.constants.StaticVariables.BUTTONARRAYS;
import static com.wekex.apps.bluetoothremote.constants.StaticVariables.CURRENT_LAYOUT_NAME;
import static com.wekex.apps.bluetoothremote.constants.StaticVariables.ID;
import static com.wekex.apps.bluetoothremote.helper.dbhelper.getDB;
import static com.wekex.apps.bluetoothremote.helper.jsonhelper.jsonArraytoView;
import static com.wekex.apps.bluetoothremote.helper.jsonhelper.stringJsonToArray;
import static com.wekex.apps.bluetoothremote.views.ViewInflater.populateEditor;

public class LayoutEditor extends AppCompatActivity {

    private FrameLayout parent;
    private String TAG = "LayoutEditor";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        StaticVariables.BUTTONARRAYS = new JSONArray();

        ID = Integer.parseInt(getIntent().getStringExtra("layou_id"));

        parent = findViewById(R.id.parent);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.addView(ViewInflater.addViewLayEditor(LayoutEditor.this));
            }
        });
        getView();

    }

    private void getView() {
        parent.removeAllViews();
        if (ID!=0){
            Cursor cursor = getDB(LayoutEditor.this).getLayoutBYId(ID);
            cursor.moveToFirst();
            Log.d(TAG, "getView: "+cursor.getCount());
            String json = cursor.getString(cursor.getColumnIndex(KEY_JSONVIEW));

            if (json.length()>10) {
                Log.d(TAG, "getView: "+json);
                BUTTONARRAYS = stringJsonToArray(json);

                populateEditor(LayoutEditor.this,parent);
                //jsonArraytoView(this, jsons, parent);
            }else {
                Log.d(TAG, "getView: empty");
            }
            //   Log.d(TAG,  cursor.getString(cursor.getColumnIndex(KEY_LAYOUT_NAME))+" isExist: "+CURRENT_LAYOUT_NAME+" "+ cursor.getString(cursor.getColumnIndex(KEY_JSONVIEW)));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        BUTTONARRAYS = null;
    }
}
