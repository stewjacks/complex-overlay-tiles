package com.iogistics.complexoverlaytiles;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.iogistics.complexoverlaytiles.GoogleMapFragment.OnGoogleMapFragmentListener;

import android.support.v7.app.ActionBarActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity implements OnGoogleMapFragmentListener {

	private GoogleMapFragment googleMapFragment;
	private GoogleMap map;
	private String TAG = "ComplexOverlayTiles";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		if (googleMapFragment==null){
			googleMapFragment = GoogleMapFragment.newInstance();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, googleMapFragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onMapReady() {
		/*
		 * Now that the map is initiated (adding the fragment programmatically), 
		 * the GoogleMap can be retrieved and tile overlay can be added
		 */
		if (googleMapFragment!=null){
			map = googleMapFragment.getMap();
			map.addTileOverlay(new TileOverlayOptions().tileProvider(
					new CustomTileProvider(ComplexTileOverlay.routes, Color.RED)));
		}
	}

}
