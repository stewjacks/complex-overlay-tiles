package com.iogistics.complexoverlaytiles;

import java.util.ArrayList;
import java.util.Map;

import android.app.Application;
import android.database.Cursor;

import com.google.android.gms.maps.model.LatLng;
import com.iogistics.complexoverlaytiles.db.DbMapAdapter;
import com.iogistics.complexoverlaytiles.db.MapDbHelper;

public class ComplexTileOverlay extends Application {

	DbMapAdapter dbMapAdapter;
	MapDbHelper mapDbHelper;
	public static ArrayList<ArrayList<LatLng>> routes;
	public boolean loaded = false;

	private static final String ROUTE_DB_ID = "id";
	private static final String ROUTE_DB_LAT = "lat";
	private static final String ROUTE_DB_LON = "lon";

	public Map<String, Object> lanes, paths, others;

	public void getRoutes(){

		mapDbHelper = new MapDbHelper(this);
		mapDbHelper.createDatabase();
		mapDbHelper.close();

		//link to map DB
		dbMapAdapter = new DbMapAdapter(this);

		ArrayList<LatLng> points = new ArrayList<LatLng>();
		routes = new ArrayList<ArrayList<LatLng>>();
		dbMapAdapter.openReadOnly();
		int curId = 0, prevId = 0, currentCount = 0;
		Cursor c = dbMapAdapter.fetchAllCoordsForRoutes();

		int INDEX_LAT = c.getColumnIndex(ROUTE_DB_LAT);
		int INDEX_LON = c.getColumnIndex(ROUTE_DB_LON);
		int INDEX_ID = c.getColumnIndex(ROUTE_DB_ID);

		for (c.moveToPosition(0);!c.isAfterLast();c.moveToNext())
		{
			curId = c.getInt(INDEX_ID); //get the route number

			if (curId!=prevId)
			{ //if it doesn't equal the previous, add this route to the list and start a new one.
				routes.add(points);
				points = new ArrayList<LatLng>();
			}

			points.add(new LatLng(
					c.getDouble(INDEX_LAT),
					c.getDouble(INDEX_LON)
					));
			prevId = curId;
			currentCount++;
		}
		routes.add(points);

		dbMapAdapter.close();
		loaded = true;


	}


}