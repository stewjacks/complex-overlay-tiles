package com.iogistics.complexoverlaytiles;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
//import android.os.Handler;
//import android.widget.ImageView;
//import android.widget.ImageView.ScaleType;

public class Splashscreen extends Activity {
	boolean isRecording = false;

//	private Handler h;
//	private Runnable r;
	private AsyncTask<Void, Void, Void> a;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    if (ComplexTileOverlay.routes != null){
			startActivity(new Intent(Splashscreen.this, MainActivity.class));
	    	Splashscreen.this.finish();
	    	overridePendingTransition(0, 0);

	    }else
	    	setContentView(R.layout.splash);
	}

	@Override
	public void onResume(){
		super.onResume();
		//version 3
		a = new loadApplicationClass().execute();
	}

	@Override
	public void onBackPressed(){
		Splashscreen.this.finish();
		a.cancel(true);
		super.onBackPressed();
	}

	private class loadApplicationClass extends AsyncTask<Void, Void, Void> {
		ProgressBar splashProgressBar = (ProgressBar) findViewById(R.id.splashProgressBar);
		@Override
		protected Void doInBackground(Void... params) {
			((ComplexTileOverlay)getApplication()).getRoutes();
			return null;
		}
		@Override
		protected void onPostExecute(Void v){
				startActivity(new Intent(Splashscreen.this, MainActivity.class));
        	splashProgressBar.setVisibility(View.INVISIBLE);
        	finish();
		}
	}
}
