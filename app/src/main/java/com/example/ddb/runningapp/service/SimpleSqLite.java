package com.example.ddb.runningapp.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SimpleSqLite extends SQLiteOpenHelper {
static final String NAME = "DATABASE";
    static final int VERSION = 1;
    public SimpleSqLite(Context context,  int version) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE TABLENAME ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " "+
                "Name TEXT);";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS TABLENAME;");
        this.onCreate(sqLiteDatabase);
    }

    public void addName(String name){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("NAME", name);

        db.insert("TABLENAME", null, values);
        db.close();
    }

    public String getName(int id) {

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query("TABLENAME", // a. table
                        new String[]{"id", "name"}, // b. column names
                        " id = ?", // c. selections
                        new String[] {String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if there are more results, give me the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 5. return object
        return cursor.getString(cursor.getColumnIndex("NAME"));
    }
}
