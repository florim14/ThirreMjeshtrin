package com.example.florim.thirremjeshtrin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.JsonReader;

/**
 * Created by Gresa on 28-Nov-16.
 */

/**
 * A class that provides the needed infrastructure to the app to communicate with the local database, in order to get information from it,
 * or feed it information
 */
public class ThirreMjeshtrinDb extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION =1;
    private static final String DATABASE_NAME = "ThirreMjeshtrin.db";
    private SQLiteDatabase db = this.getWritableDatabase();
    public ThirreMjeshtrinDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * A static class that has it's own _ID variable to help keep track of records in a database
     * table and also other final string variables for easier SQL query writing for the table
     * Inbox- caches data on the user's conversations with other users
     */
    static class Inbox implements BaseColumns {
        public static final String TABLE_NAME = "Inbox";
        public static final String COLUMN_USERNAME = "Username";
        public static final String COLUMN_LASTMESSAGE = "LastMessage";
        public static final String COLUMN_TIMESTAMP = "Timestamp";
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                        + _ID +
                        " INTEGER PRIMARY KEY , " + COLUMN_USERNAME + " VARCHAR(30) ," +
                        COLUMN_LASTMESSAGE + " TEXT," + COLUMN_TIMESTAMP + " TIMESTAMP)";


        private static final String SQL_TRUNCATE_ENTRIES =
                "TRUNCATE TABLE IF EXISTS " + TABLE_NAME;

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

    }
    /**
     * A static class that has it's own _ID variable to help keep track of records in a database
     * table and also other final string variables for easier SQL query writing for the table
     * Profile- caches the profile data for an ordinary user
     */
    static class Profile implements BaseColumns {
        public static final String TABLE_NAME = "Profile";
        public static final String COLUMN_USERNAME = "Username";
        public static final String COLUMN_EMAIL = "Email";
        public static final String COLUMN_USERID = "UserID";
        public static final String COLUMN_TOKEN = "Token";
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                        + _ID +
                        " INTEGER PRIMARY KEY , " + COLUMN_USERNAME + " VARCHAR(30) ," +
                        COLUMN_EMAIL + " VARCHAR(50)," + COLUMN_USERID + " INT(11), "+
                        COLUMN_TOKEN + "  TEXT ) ";


        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        private static final String SQL_TRUNCATE_ENTRIES =
                "TRUNCATE TABLE IF EXISTS " + TABLE_NAME;

    }
    /**
     * A static class that has it's own _ID variable to help keep track of records in a database
     * table and also other final string variables for easier SQL query writing for the table
     * ReapairmanProfile- caches profile data for a repairman user
     */
    static class RepairmanProfile implements BaseColumns {
        public static final String TABLE_NAME = "RepairmanProfile";
        public static final String COLUMN_USERNAME = "Username";
        public static final String COLUMN_EMAIL = "Email";
        public static final String COLUMN_NAME="Name";
        public static final String COLUMN_SURNAME="Surname";
        public static final String COLUMN_LOCATION="Location";
        public static final String COLUMN_RADIUS = "Radius";
        public static final String COLUMN_CATEGORY = "Category";
        public static final String COLUMN_PHONE = "Phone";
        public static final String COLUMN_RATING = "Rating";
        public static final String COLUMN_USERID = "UserID";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                        + _ID +
                        " INTEGER PRIMARY KEY , " + COLUMN_USERNAME + " VARCHAR(30) ," +
                        COLUMN_EMAIL + " VARCHAR(50)," + COLUMN_NAME + " VARCHAR(70) ,"+
                        COLUMN_SURNAME + " VARCHAR(70) ,"+ COLUMN_LOCATION + " TEXT ,"+
                        COLUMN_RADIUS + " INTEGER ,"+ COLUMN_CATEGORY + " VARCHAR(30),"+ COLUMN_PHONE + " VARCHAR(30) ,"+
                        COLUMN_RATING + " VARCHAR(30) ,"+ COLUMN_USERID + " INT(11)) ";


        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        private static final String SQL_TRUNCATE_ENTRIES =
                "TRUNCATE TABLE IF EXISTS " + TABLE_NAME;

    }

    /**
     * Truncates the existing Inbox table in the database and feeds it new data from the ContentValues parameter
     * @param values contains the data that will be inserted in the Inbox table
     */
    public void updateInbox(ContentValues values){
        db.execSQL(Inbox.SQL_TRUNCATE_ENTRIES);
        db.insert(Inbox.TABLE_NAME,null,values);
    }

    /**
     * Reads all the data in the Inbox table, ordered from newest to oldest
     * @return Cursor containing the data returned from query execution on the table
     */
    public Cursor getInbox(){

        String query= "SELECT * FROM "+Inbox.TABLE_NAME+" ORDER BY "+Inbox._ID+ " DESC";
        Cursor c=db.rawQuery(query,null,null);
        return c;
    }

    /**
     * Truncates the existing RepairmanProfile or Profile table in the database and feeds it new data from the ContentValues parameter
     * @param values contains the data that will be inserted in the RepairmanProfile or Profile table
     * @param isRepairman works as a switch deciding which one of RepairmanProfile or Profile table will be updated
     */
    public void updateProfile(ContentValues values,boolean isRepairman){
        if(isRepairman){
            db.execSQL(RepairmanProfile.SQL_TRUNCATE_ENTRIES);
            db.insert(RepairmanProfile.TABLE_NAME,null,values);
        }
        else{
            db.execSQL(Profile.SQL_TRUNCATE_ENTRIES);
            db.insert(Profile.TABLE_NAME,null,values);
        }
    }

    /**
     * Reads all the data in the RepairmanProfile or Profile table
     * @param isRepairman works as a switch deciding which one of RepairmanProfile or Profile table will be read
     * @return profileData, a ContentValues object, with the data read from the chosen table
     */
    public ContentValues getProfile(boolean isRepairman){
        ContentValues profileData=new ContentValues();
        if(isRepairman){
            String query= "SELECT * FROM "+RepairmanProfile.TABLE_NAME;
            Cursor c=db.rawQuery(query,null,null);
            c.moveToFirst();
            profileData.put(RepairmanProfile.COLUMN_USERNAME,c.getString(c.getColumnIndex(RepairmanProfile.COLUMN_USERNAME)));
            profileData.put(RepairmanProfile.COLUMN_EMAIL,c.getString(c.getColumnIndex(RepairmanProfile.COLUMN_EMAIL)));
            profileData.put(RepairmanProfile.COLUMN_NAME,c.getString(c.getColumnIndex(RepairmanProfile.COLUMN_NAME)));
            profileData.put(RepairmanProfile.COLUMN_SURNAME,c.getString(c.getColumnIndex(RepairmanProfile.COLUMN_SURNAME)));
            profileData.put(RepairmanProfile.COLUMN_LOCATION,c.getString(c.getColumnIndex(RepairmanProfile.COLUMN_LOCATION)));
            profileData.put(RepairmanProfile.COLUMN_RADIUS,c.getString(c.getColumnIndex(RepairmanProfile.COLUMN_RADIUS)));
            profileData.put(RepairmanProfile.COLUMN_CATEGORY,c.getString(c.getColumnIndex(RepairmanProfile.COLUMN_CATEGORY)));
            profileData.put(RepairmanProfile.COLUMN_PHONE,c.getString(c.getColumnIndex(RepairmanProfile.COLUMN_PHONE)));
            profileData.put(RepairmanProfile.COLUMN_RATING,c.getString(c.getColumnIndex(RepairmanProfile.COLUMN_RATING)));
            profileData.put(RepairmanProfile.COLUMN_USERID,c.getString(c.getColumnIndex(RepairmanProfile.COLUMN_USERID)));
            return profileData;
        }

        String query= "SELECT * FROM "+Profile.TABLE_NAME;
        Cursor c=db.rawQuery(query,null,null);
        c.moveToFirst();
        profileData.put(Profile.COLUMN_USERNAME,c.getString(c.getColumnIndex(Profile.COLUMN_USERNAME)));
        profileData.put(Profile.COLUMN_EMAIL,c.getString(c.getColumnIndex(Profile.COLUMN_EMAIL)));
        profileData.put(Profile.COLUMN_USERID,c.getString(c.getColumnIndex(Profile.COLUMN_USERID)));
        return profileData;

    }


    /**
     * Method called on the creation of a ThirreMjeshtrinDb object
     * @param db
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Inbox.SQL_CREATE_TABLE);
        db.execSQL(Profile.SQL_CREATE_TABLE);
        db.execSQL(RepairmanProfile.SQL_CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(Inbox.SQL_DELETE_ENTRIES);
        db.execSQL(Profile.SQL_DELETE_ENTRIES);
        db.execSQL(RepairmanProfile.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);

    }

}
