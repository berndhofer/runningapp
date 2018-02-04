package com.example.ddb.runningapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ddb.runningapp.pojo.Route;
import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DatabaseClass extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "LocationDB";

    public DatabaseClass(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // SQL statement to create storage table
        String CREATE_TABLE_ROUTES = "CREATE TABLE ROUTES ( " +
                "routeNo INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " "+
                "timestamp DATETIME, distance FLOAT, duration DATETIME)";

        String CREATE_TABLE_LOCATIONS ="CREATE TABLE LOCATIONS ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "routeNo INTEGER, "+
                "latitude FLOAT, longitude FLOAT, " +
                "FOREIGN KEY (routeNo) references ROUTES(routeNo) " +
                "on update set null)";


        db.execSQL(CREATE_TABLE_ROUTES);
        db.execSQL(CREATE_TABLE_LOCATIONS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older storage table if existed
        db.execSQL("DROP TABLE IF EXISTS ROUTES");
        db.execSQL("DROP TABLE IF EXISTS LOCATIONS");

        // create new storage table
        this.onCreate(db);
    }
    // Storage table name
    private static final String TABLE_LOCATIONS = "LOCATIONS";
    private static final String TABLE_ROUTES = "ROUTES";

    // Storage Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_ROUTENO = "routeNo";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_DISTANCE = "distance";
    private static final String[] LOCATION_COLUMNS = {KEY_ID, KEY_ROUTENO, KEY_LATITUDE, KEY_LONGITUDE};
    private static final String[] ROUTES_COLUMNS = {KEY_ROUTENO, KEY_TIMESTAMP, KEY_DISTANCE, KEY_DURATION};

    public void addRoute(int no) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ROUTENO, no);
        values.put(KEY_TIMESTAMP, new SimpleDateFormat("dd.MM.yyyy  HH:mm:ss").format(new java.util.Date().getTime()));
        values.put(KEY_DISTANCE, 0);
        values.put(KEY_DURATION, new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new java.util.Date()));

        // 3. insert

        db.insert(TABLE_ROUTES,
                null,
                values);

        // 4. close
        db.close();
    }

    public int getNewTrackNo(){
        int trackNo = 1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_ROUTES, // a. table
                        ROUTES_COLUMNS, // b. column names
                        null, // c. selections
                        null, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if (cursor.moveToNext()) {
            cursor.moveToLast();
           trackNo= cursor.getInt(cursor.getColumnIndex(KEY_ROUTENO))+1;
        }
        db.close();
        return trackNo;
    }

    public void addLocation(int routeNo, LatLng location, float dist){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ROUTENO, routeNo);
        values.put(KEY_LATITUDE, location.latitude);
        values.put(KEY_LONGITUDE, location.longitude);

        db.insert(TABLE_LOCATIONS, null, values);

        db.close();
        updateDistance(routeNo,dist);
    }

    public List<LatLng> getLocationOfRoute(int routeNo) {
        List<LatLng> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_LOCATIONS, // a. table
                        LOCATION_COLUMNS, // b. column names
                        "routeNo=?", // c. selections
                        new String[] {String.valueOf(routeNo)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit


        while (cursor.moveToNext()) {
            list.add(new LatLng(cursor.getFloat(cursor.getColumnIndex(KEY_LATITUDE)), cursor.getFloat(cursor.getColumnIndex(KEY_LONGITUDE))));

        }
        db.close();
        return list;
    }

    public List<Route> getRoutes() {

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        List<Route> list = new ArrayList<>();
        // 2. build query
        Cursor cursor =
                db.query(TABLE_ROUTES, // a. table
                        ROUTES_COLUMNS, // b. column names
                        null, // c. selections
                        null, // d. selections args
                        null, // e. group by
                        null, // f. having
                        "timestamp asc", // g. order by
                        null); // h. limit


        while (cursor.moveToNext()) {
            java.util.Date date = null;
            java.util.Date dur = null;

            try {
                date = new SimpleDateFormat("dd.MM.yy hh:mm:ss").parse(cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP)));
                dur = new SimpleDateFormat("dd.MM.yy hh:mm:ss").parse(cursor.getString(cursor.getColumnIndex(KEY_DURATION)));
                System.out.println(dur);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            list.add(new Route(date, cursor.getInt(cursor.getColumnIndex(KEY_ROUTENO)), cursor.getFloat(cursor.getColumnIndex(KEY_DISTANCE))
                    , dur));

        }
        db.close();
        // 5. return object
        return list;
    }

    public boolean removeRoute(int no){
       SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(TABLE_ROUTES, KEY_ROUTENO + "=" + no, null) > 0 && db.delete(TABLE_LOCATIONS, KEY_ROUTENO + "=" + no, null)>0;
        }

        private void updateDistance(int routeNo, float distance){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues args =new ContentValues();
            String filter = KEY_ROUTENO +"=" + routeNo;
            args.put(KEY_DISTANCE, distance);
            db.update(TABLE_ROUTES, args,filter,null );
        }

        public void updateTime(int routeNo){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues args =new ContentValues();
            args.put(KEY_DURATION, new SimpleDateFormat("dd.MM.yyyy  HH:mm:ss").format(new java.util.Date().getTime()));
            String filter = KEY_ROUTENO +"=" + routeNo;
            db.update(TABLE_ROUTES,args,filter,null);
        }





}
