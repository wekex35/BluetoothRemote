package com.wekex.apps.bluetoothremote.dbhandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wekex.apps.bluetoothremote.constants.Constants;

import static com.wekex.apps.bluetoothremote.constants.Constants.KEY_ID;
import static com.wekex.apps.bluetoothremote.constants.Constants.KEY_JSONVIEW;
import static com.wekex.apps.bluetoothremote.constants.Constants.KEY_NAME;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "servicefeeder";
    private static final String TABLE_GIFS = "Giftable";
    String TAG = "DataBaseHAndler";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE "+TABLE_GIFS+"("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                +KEY_NAME+" TEXT, "
                +KEY_JSONVIEW+" TEXT"+")";
           db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GIFS);
        // Create tables again
        onCreate(db);
    }

    public void addGifs(String name,String path){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME,name);
        values.put(KEY_JSONVIEW,path);
        db.insert(TABLE_GIFS,null,values);
        db.close();

        Cursor cursor = getPathByName(name);
        cursor.moveToFirst();
        Log.d(TAG, "isExist: "+cursor.getString(cursor.getColumnIndex(KEY_NAME)) );
    }

    public Cursor getRandom(){
        //SELECT * FROM table ORDER BY RANDOM() LIMIT 1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_GIFS+" ORDER BY RANDOM() LIMIT 1",null);
        return cursor;
    }
    public Cursor getPathByName(String name){
        //SELECT * FROM table ORDER BY RANDOM() LIMIT 1;
        SQLiteDatabase db = this.getReadableDatabase();

        //SELECT * FROM dbname.sqlite_master WHERE type='table';
        String Query = "Select * from " + TABLE_GIFS + " where " + KEY_NAME + " = \'" + name+"\'";
        Cursor cursor = db.rawQuery(Query, null);
        return cursor;
    }

    public void removeGifs(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        String delete = "delete from "+ TABLE_GIFS +" where " + KEY_NAME + " = \'" + name+"\'";
        db.execSQL(delete);
       /// db.delete(TABLE_GIFS,KEY_NAME+" = ",new String[]{String.valueOf(name)});
        db.close();
    }

    public Boolean isExist(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_GIFS + " where " + KEY_NAME + " = \'" + name+"\'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();

        return true;

    }


}
