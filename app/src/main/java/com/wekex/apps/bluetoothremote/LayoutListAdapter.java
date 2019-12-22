package com.wekex.apps.bluetoothremote;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wekex.apps.bluetoothremote.activities.LayoutEditor;
import com.wekex.apps.bluetoothremote.helper.dbhelper;
import com.wekex.apps.bluetoothremote.helper.jsonhelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.wekex.apps.bluetoothremote.constants.Constants.KEY_ID;
import static com.wekex.apps.bluetoothremote.constants.Constants.KEY_LAYOUT_NAME;

class LayoutListAdapter extends RecyclerView.Adapter<LayoutListAdapter.MyViewHolder> implements View.OnClickListener {
    Activity context;
    Cursor cursor;

    private static String TAG = "LayoutListAdapter";
    ArrayList<JSONObject> jsonObjects;

    public LayoutListAdapter(Activity activity, Cursor cursorAdapter) {
        context = activity;
        cursor = cursorAdapter;
        jsonObjects = new ArrayList<>();
        cursor.moveToPosition(0);
       /* for (int i=0;i<= cursor.getCount();i++){

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name",cursor.getString(cursor.getColumnIndex(KEY_LAYOUT_NAME)));
                jsonObject.put("id",cursor.getString(cursor.getColumnIndex(KEY_ID)));
                jsonObjects.add(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }*/
        while (cursor.moveToNext()){
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name",cursor.getString(cursor.getColumnIndex(KEY_LAYOUT_NAME)));
                jsonObject.put("id",cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                Log.d(TAG, "LayoutListAdapter: "+cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                jsonObjects.add(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_list_item,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {
        JSONObject jsonObject = jsonObjects.get(position);
        final int layou_id = jsonhelper.intJsonreader(jsonObject,"id");
        String name =jsonhelper.StringJsonreader(jsonObject,"name");
        myViewHolder.name.setText(name);
        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LayoutEditor.class);
                Log.d(TAG, "onClick: "+layou_id);
                intent.putExtra("layou_id",String.valueOf(layou_id));
                context.startActivity(intent);
            }
        });
        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbhelper.getDB(context).deleteLayout(layou_id);
                removeItem(position);
                Log.d(TAG, "onClick: Deleted");
            }
        });
    }

    public void removeItem(int position) {
        jsonObjects.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,getItemCount());
        /*
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);*/
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.parent:
              
                break;
            case R.id.delete:
              
                break;
        }
    }


    @Override
    public int getItemCount() {
        return jsonObjects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView name;
        ImageView delete,share;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.parent);
            name = itemView.findViewById(R.id.layout_name);
            delete = itemView.findViewById(R.id.delete);
            share = itemView.findViewById(R.id.share);
        }
    }
}
