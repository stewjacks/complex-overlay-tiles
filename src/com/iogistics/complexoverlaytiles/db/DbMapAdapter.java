package com.iogistics.complexoverlaytiles.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.maps.GeoPoint;


public class DbMapAdapter {
    private static final int DATABASE_VERSION = 1;

    public static final String ROWID = "_id";
    public static final String ROUTE  = "id";
    public static final String LAT   = "lat";
    public static final String LON   = "lon";

    private static final String TAG = "DbMapAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_NAME = "routes.db";
    private static final String TABLE_ROUTES = "routedata";

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
              //      + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTES);
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx
     *            the Context within which to work
     */
    public DbMapAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the database. If it cannot be opened, try to create a new instance
     * of the database. If it cannot be created, throw an exception to signal
     * the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException
     *             if the database could be neither opened or created
     */
    public DbMapAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public DbMapAdapter openReadOnly() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getReadableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    // #### Coordinate table methods ####


    public Cursor fetchAllCoordsForRoutes() {
    	//add bounding box based on zoom level and x,y visible? this would speed up maps exponentially if there is a lag problem
    	try {

            Cursor mCursor = mDb.query(
            		true, //Boolean distinct (false here because there COULD be repeated coords...?)
            		TABLE_ROUTES, //Table to query
            		new String[] { //String array of columns to fetch
            				ROWID, ROUTE, LAT, LON},
//            				ROUTE, LAT, LON},
                    null, // selection: Filter of which rows to return
                    null, // selectionArgs: ?s in selection are replaced by selectionArgs
                    null, // groupBy: filter for grouping rows (SQL GROUP BY clause)
                    null, // having: filter row groups
                    ROWID, //orderBy which column
                    null); //limit the number of rows to return
            if (mCursor != null) {
                mCursor.moveToFirst();
            }
            return mCursor;
    	} catch (Exception e) {
    		//Log.v("GOT!",e.toString());
    		return null;
    	}
    }

    public Cursor fetchAllViewableCoords(GeoPoint topRight, GeoPoint bottomLeft){

    	double xMin = (bottomLeft.getLatitudeE6()/1E6)+0.01; //add a buffer so nothing is missing in frame
    	double xMax = (topRight.getLatitudeE6()/1E6)-0.01;
    	double yMin = (bottomLeft.getLongitudeE6()/1E6)-0.01;
    	double yMax = (topRight.getLongitudeE6()/1E6)+0.01;

    	final String whereClause = "LAT <= "+Double.toString(xMin)+" AND LAT >= "+Double.toString(xMax)+" AND LON <= "+Double.toString(yMax)+" AND LON >= "+Double.toString(yMin);

//    	final String whereClause = " LAT >= 0";
//    	final String[] whereArgs = new String[]{""+0, ""+0};

    	try {

            Cursor mCursor = mDb.query(
            		true, //Boolean distinct (false here because there COULD be repeated coords...?)
            		TABLE_ROUTES, //Table to query
            		new String[] { //String array of columns to fetch
            				ROUTE, LAT, LON},
                    whereClause, // selection: Filter of which rows to return
                    null, // selectionArgs: ?s in selection are replaced by selectionArgs
                    null, // groupBy: filter for grouping rows (SQL GROUP BY clause)
                    null, // having: filter row groups
                    ROWID, //orderBy which column
                    null); //limit the number of rows to return
            if (mCursor != null) {
                mCursor.moveToFirst();
            }
            return mCursor;
    	} catch (Exception e) {
    		//Log.v("GOT!",e.toString());
    		return null;
    	}
    }
//    public boolean multiplyandstore(int id, double lat, double lon){
//
//	    ContentValues initialValues = new ContentValues();
//	    initialValues.put(LAT, lat);
//	    initialValues.put(LON, lon);
//
//	    return mDb.update(TABLE_ROUTES, initialValues, ROWID + "="
//	            + id, null) > 0;
//    }
}

