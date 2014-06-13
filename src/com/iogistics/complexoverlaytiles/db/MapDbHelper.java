package com.iogistics.complexoverlaytiles.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MapDbHelper extends SQLiteOpenHelper {

    //we declare a bunch of useful constants
    //the should be pretty obvious what they are!
    private static final String DATABASE_PATH = "/data/data/com.iogistics.complexoverlaytiles/databases/";
    private static final String DATABASE_NAME="routes.db";
    private static final int SCHEMA_VERSION=1;

    public SQLiteDatabase dbSqlite;
    private final Context myContext;

    public MapDbHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void createDatabase() {
        createDB();
    }

    private void createDB() {

        boolean dbExist = DBExists();

        if (!dbExist) {

             //By calling this method we create an empty database into the default system location
             //We need this so we can overwrite that database with our database.
             this.getReadableDatabase();

             //now we copy the database we included!
             copyDBFromResource();

        }

    }

    private boolean DBExists() {

        SQLiteDatabase db = null;

        try {
            String databasePath = DATABASE_PATH + DATABASE_NAME;
            db = SQLiteDatabase.openDatabase(databasePath, null,
                    SQLiteDatabase.OPEN_READWRITE);
            db.setLocale(Locale.getDefault());
            db.setLockingEnabled(true);
            db.setVersion(1);

        } catch (SQLiteException e) {

            Log.e("SqlHelper", "database not found");

        }

        if (db != null) {

            db.close();

        }

        return db != null ? true : false;
    }

    private void copyDBFromResource() {

        InputStream inputStream = null;
        OutputStream outStream = null;
        String dbFilePath = DATABASE_PATH + DATABASE_NAME;

        try {

            inputStream = myContext.getAssets().open(DATABASE_NAME);

            outStream = new FileOutputStream(dbFilePath);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }

            outStream.flush();
            outStream.close();
            inputStream.close();

        } catch (IOException e) {
        	//Log.d("IOException", e.toString());

            throw new Error("Problem copying database from resource file.");

        }

    }


    @Override
    public synchronized void close() {

        if (dbSqlite != null)
        {
            dbSqlite.close();
        }
        super.close();

    }


}