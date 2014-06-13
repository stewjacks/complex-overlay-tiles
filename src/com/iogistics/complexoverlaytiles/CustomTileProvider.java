package com.iogistics.complexoverlaytiles;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Path;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;
import com.google.maps.android.geometry.Point;
import com.google.maps.android.projection.SphericalMercatorProjection;

public class CustomTileProvider implements TileProvider {
    private static final String TAG = "PointTileOverlay";
	private final int mTileSize = 256;
    private final SphericalMercatorProjection mProjection = new SphericalMercatorProjection(mTileSize);
    private final int mScale = 1;
    private final int mDimension = mScale * mTileSize;
	private final ArrayList<ArrayList<LatLng>> mLatLng;
	private Paint paint;

	private final int colour;

    public CustomTileProvider(ArrayList<ArrayList<LatLng>> mLatLng, int colour) {
		this.mLatLng = mLatLng;
		this.colour = colour;
	}

	@Override
    public Tile getTile(int x, int y, int zoom) {
		Log.d(TAG, "zoom int: "+zoom);
        Matrix matrix = new Matrix();
        
        /*The scale factor in the transformation matrix is 1/10 here because I scale up the tiles for drawing.
         * Why? Well, the spherical mercator projection doesn't seem to quite provide the resolution I need for
         * scaling up at high zoom levels. This bypasses it without needing a higher tile resolution.
         */
        float scale = ((float) Math.pow(2, zoom) * mScale/10);
        matrix.postScale(scale, scale);
        matrix.postTranslate(-x * mDimension, -y * mDimension);

        Bitmap bitmap = Bitmap.createBitmap(mDimension, mDimension, Bitmap.Config.ARGB_8888); //save memory on old phones
        Canvas c = new Canvas(bitmap);
    	c.setMatrix(matrix);
    	c=drawCanvasFromArray(c, zoom);
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

        return new Tile(mDimension, mDimension, baos.toByteArray());
    }

	/**
	 * Here the Canvas can be drawn on based on data provided from a Spherical Mercator Projection
	 * @param c
	 * @param zoom
	 * @return
	 */
    private Canvas drawCanvasFromArray(Canvas c, int zoom){

		paint = new Paint();

		//Line features
		paint.setStrokeWidth(getLineWidth(zoom));
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(colour);
		paint.setStrokeCap(Cap.ROUND);
		paint.setStrokeJoin(Join.ROUND);
		paint.setShadowLayer(0, 0, 0, 0);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setAlpha(getAlpha(zoom));
		Path path = new Path();

		if (mLatLng != null){
			for(int i = 0; i<mLatLng.size();i++){
				ArrayList<LatLng> route = mLatLng.get(i);

				if(route!=null&&route.size()>1){

					Point screenPt1 = mProjection.toPoint(route.get(0)); //first point
					MarkerOptions m = new MarkerOptions();
					m.position(route.get(0));
					path.moveTo((float)screenPt1.x*10, (float)screenPt1.y*10);
					for (int j = 1; j<route.size(); j++){
						Point screenPt2 = mProjection.toPoint(route.get(j));
						path.lineTo((float)screenPt2.x*10, (float)screenPt2.y*10);
						}
					}
				}
			}
		c.drawPath(path, paint);
		return c;
    }
    /**
     * This will let you adjust the line width based on zoom level
     * @param zoom
     * @return
     */
    private float getLineWidth(int zoom){
    	switch(zoom){
    	case 21:
    	case 20:
    		return 0.0001f;
    	case 19:
    		return 0.00025f;
    	case 18:
    		return 0.0005f;
    	case 17:
    		return 0.0005f;
    	case 16:
    		return 0.001f; 
    	case 15:
    		return 0.001f; 
    	case 14:
    		return 0.001f; 
    	case 13:
    		return 0.002f; 
    	case 12:
    		return 0.003f; 
    	default:
    		return 0f;
    	}
    }

    /**
     * This will let you adjust the alpha value based on zoom level
     * @param zoom
     * @return
     */
    private int getAlpha(int zoom){
    	
    	switch(zoom){
    	case 20:
    		return 140;
    	case 19:
    		return 140;
    	case 18:
    		return 140;
    	case 17:
    		return 140;
    	case 16:
    		return 180;
    	case 15:
    		return 180;
    	case 14:
    		return 180;
    	default:
    		return 255;
    	}
    }
}
