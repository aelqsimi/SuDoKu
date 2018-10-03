package com.ximisoft.sudoku;

/**
 * Created by abdel on 11/12/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;


public class DBAdapter {
    Context c;
    DBHelper helper;
    public DBAdapter(Context context)
    {
        c = context;
        helper = new DBHelper(context);
    }

    public void insertData(String fName, String lName,String bGrid, String cGrid, String minutes, String seconds)
    {
        SQLiteDatabase dbb = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.ID, fName+lName);
        contentValues.put(DBHelper.FIRST_NAME, fName);
        contentValues.put(DBHelper.LAST_NAME, lName);
        contentValues.put(DBHelper.BASE_GRID, bGrid);
        contentValues.put(DBHelper.CURRENT_GRID, cGrid);
        contentValues.put(DBHelper.MINUTES, minutes);
        contentValues.put(DBHelper.MINUTES, seconds);
        dbb.insert(DBHelper.TABLE_NAME, null , contentValues);
    }

    public HashMap<String,HashMap<String,String>> getData()
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {DBHelper.ID, DBHelper.FIRST_NAME, DBHelper.LAST_NAME,DBHelper.BASE_GRID,DBHelper.CURRENT_GRID,DBHelper.MINUTES,DBHelper.SECONDS};
        Cursor cursor =db.query(DBHelper.TABLE_NAME,columns,null,null,null,null,null);
        HashMap<String,HashMap<String,String>> map = new HashMap<>();
        HashMap<String,String> map2 = new HashMap<>();
        while (cursor.moveToNext())
        {
            map2.put(DBHelper.FIRST_NAME,cursor.getString(cursor.getColumnIndex(DBHelper.FIRST_NAME)));
            map2.put(DBHelper.LAST_NAME,cursor.getString(cursor.getColumnIndex(DBHelper.LAST_NAME)));
            map2.put(DBHelper.BASE_GRID,cursor.getString(cursor.getColumnIndex(DBHelper.BASE_GRID)));
            map2.put(DBHelper.CURRENT_GRID,cursor.getString(cursor.getColumnIndex(DBHelper.CURRENT_GRID)));
            map2.put(DBHelper.MINUTES,cursor.getString(cursor.getColumnIndex(DBHelper.MINUTES)));
            map2.put(DBHelper.SECONDS,cursor.getString(cursor.getColumnIndex(DBHelper.SECONDS)));
            map.put(cursor.getString(cursor.getColumnIndex(DBHelper.ID)),map2);
        }
        return map;
    }

    public  int delete(String nom, String prenom)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs ={prenom,nom};

        int count =db.delete(DBHelper.TABLE_NAME , DBHelper.FIRST_NAME +" = ? and "+DBHelper.LAST_NAME +" = ?",whereArgs);
        return  count;
    }

    public int update(String fName, String lName,String bGrid, String cGrid, String minutes, String seconds)
    {
        if(getData().containsKey(fName+lName)) {
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.CURRENT_GRID, cGrid);
            contentValues.put(DBHelper.MINUTES, minutes);
            contentValues.put(DBHelper.SECONDS, seconds);
            String[] whereArgs = {fName + lName};
            int count = db.update(DBHelper.TABLE_NAME, contentValues,DBHelper.ID + " = ?", whereArgs);
            return count;
        }
        else{
            insertData(fName, lName,bGrid, cGrid,minutes,seconds);
            return -1;
        }
    }



    static class DBHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "sudoku";
        private static final String TABLE_NAME = "Game";   // Table Name
        private static final int DATABASE_Version = 1;
        private static final String ID = "PrenomNom";
        private static final String FIRST_NAME = "Prenom";
        private static final String LAST_NAME = "Nom";
        private static final String CURRENT_GRID = "grilleCourante";
        private static final String BASE_GRID = "grilleDeBase";
        private static final String MINUTES = "Minutes";
        private static final String SECONDS = "Secondes";
        private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+
                "("+ ID +" VARCHAR(255) PRIMARY KEY, "+ FIRST_NAME +" VARCHAR(255), "+ LAST_NAME+" VARCHAR(225), "+ CURRENT_GRID+" VARCHAR(225), "+BASE_GRID+" VARCHAR(225), "+ MINUTES+" VARCHAR(10), "+ SECONDS+" VARCHAR(10));";
        private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
            Log.d("Table===============>",CREATE_TABLE);
        }

        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                Toast.makeText(context,"Erreur "+e,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch (Exception e) {
                Toast.makeText(context,"Erreur "+e,Toast.LENGTH_LONG).show();
            }
        }
    }
}
