package com.wordpress.priyankvex.sockets0;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by priyank on 22/12/14.
 * Class to handle all the database operations and provides functions to perform them.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "skiffle_database";

    //Names of the tables in the database
    private static final String TABLE_COMMANDS = "commands";


    //Names of columns in TABLE_FAVOURITES
    private static final String KEY_ID = "_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CMD = "cmd";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //SQL query to create the table.
        String queryCreateTableCommands = "CREATE TABLE " + TABLE_COMMANDS + "( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_TITLE + " TEXT, "
                + KEY_CMD + " TEXT"
                + ")";

        //Executing the query to create the table for top songs
        db.execSQL(queryCreateTableCommands);

    }

    //We are just going to replace the database for a version change.
    //This is not an app to store top secret info after all.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMANDS);

        // Create tables again
        onCreate(db);
    }


    void addCommand(SQLiteDatabase db, Bundle b){

        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, b.getString("title"));
        values.put(KEY_CMD, b.getString("cmd"));

        //Inserting this content value of a single song into the table
        db.insert(TABLE_COMMANDS, null, values);

    }


    List readCommands(SQLiteDatabase db){

        //List of maps to hold all the commands and their titles.
        List<Map> cmds = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_COMMANDS;

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Map<String, String> cmd = new HashMap<>();
                cmd.put("title", cursor.getString(1));
                cmd.put("cmd", cursor.getString(2));
                cmds.add(cmd);

            } while (cursor.moveToNext());
        }

        // return list of maps. Where each map is cmd + its title
        return cmds;
    }



    void deleteCommand(String cmd){
        String sql = "DELETE FROM " +TABLE_COMMANDS + " WHERE " + KEY_CMD + " = " + "\"" + cmd + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);

    }


    //Function to check a table is empty or not
    public boolean isEmptyTableFavourites(SQLiteDatabase db){

        String countQuery = "SELECT count(*) FROM " + TABLE_COMMANDS;
        Cursor mcursor = null;
        int icount = 0;
        try {
            mcursor = db.rawQuery(countQuery, null);
            mcursor.moveToFirst();
            icount = mcursor.getInt(0);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if(icount > 0 ){
            return false;
        }
        return true;
    }


}