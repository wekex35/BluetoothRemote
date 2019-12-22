package com.wekex.apps.bluetoothremote.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.wekex.apps.bluetoothremote.constants.Constants.KEY_ID;
import static com.wekex.apps.bluetoothremote.constants.Constants.KEY_JSONVIEW;
import static com.wekex.apps.bluetoothremote.constants.Constants.KEY_LAYOUT_NAME;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db";
    private static final String TABLE_JSONVIEW = "table_jsonView";
    String TAG = "DataBaseHAndler";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE "+TABLE_JSONVIEW+"("
                +KEY_ID + " INTEGER PRIMARY KEY,"
                +KEY_LAYOUT_NAME+" TEXT, "
                +KEY_JSONVIEW+" TEXT"+")";
           db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JSONVIEW);
        // Create tables again
        onCreate(db);
    }

    public void addLayout(String name,String path){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LAYOUT_NAME,name);
        values.put(KEY_JSONVIEW,path);
        db.insert(TABLE_JSONVIEW,null,values);
        db.close();
        Cursor cursor = getLayoutBYName(name);
        cursor.moveToFirst();
        Log.d(TAG, "isExist: "+cursor.getString(cursor.getColumnIndex(KEY_LAYOUT_NAME)) );
    }

    public Cursor getRandom(){
        //SELECT * FROM table ORDER BY RANDOM() LIMIT 1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_JSONVIEW+" ORDER BY RANDOM() LIMIT 1",null);
        return cursor;
    }

    public Cursor getLayoutBYName(String name){
        //SELECT * FROM table ORDER BY RANDOM() LIMIT 1;
        SQLiteDatabase db = this.getReadableDatabase();

        //SELECT * FROM dbname.sqlite_master WHERE type='table';
        String Query = "Select * from " + TABLE_JSONVIEW + " where " + KEY_LAYOUT_NAME + " = \'" + name+"\'";
        Cursor cursor = db.rawQuery(Query, null);
        return cursor;
    }
    public Cursor getLayoutBYId(int id){
        //SELECT * FROM table ORDER BY RANDOM() LIMIT 1;
        SQLiteDatabase db = this.getReadableDatabase();

        //SELECT * FROM dbname.sqlite_master WHERE type='table';
        String Query = "Select * from " + TABLE_JSONVIEW + " where " + KEY_ID + " = \'" + id+"\'";
        Cursor cursor = db.rawQuery(Query, null);
        return cursor;
    }

    public void deleteLayout(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        String delete = "delete from "+ TABLE_JSONVIEW + " where " + KEY_ID + " = \'" + id+"\'";
        db.execSQL(delete);
       /// db.delete(TABLE_JSONVIEW,KEY_LAYOUT_NAME+" = ",new String[]{String.valueOf(name)});
        db.close();
    }

    public Boolean isExist(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_JSONVIEW + " where " + KEY_LAYOUT_NAME + " = \'" + name+"\'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();

        return true;

    }

    public Cursor getLayouts(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_JSONVIEW;
        Cursor cursor = db.rawQuery(Query, null);
        return cursor;


    }

    public void updateLayout(int ID, String path) {
        SQLiteDatabase db = this.getReadableDatabase();
          /* ContentValues values = new ContentValues();
        values.put(KEY_JSONVIEW,path);



        db.update(TABLE_JSONVIEW,values,KEY_LAYOUT_NAME+" = "+name,null);
        db.close();
        Cursor cursor = getLayoutBYName(name);
        cursor.moveToFirst();
     */
        Log.d(TAG, "updateLayout: "+new String(path)+ID);
        String Query = "UPDATE " + TABLE_JSONVIEW + " SET "+KEY_JSONVIEW +" = \'"+ new String(path) +"\' where " + KEY_ID + " = \'" + ID+"\'";
        Cursor cursor = db.rawQuery(Query, null);
        cursor.moveToFirst();
        db.close();

        //        cursor.moveToFirst();
//        Log.d(TAG, "isExist: "+cursor.getString(cursor.getColumnIndex(KEY_LAYOUT_NAME)) );

    }
}
