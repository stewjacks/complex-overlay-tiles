package com.iogistics.complexoverlaytiles;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class GoogleMapFragment extends SupportMapFragment {

private static final String SUPPORT_MAP_BUNDLE_KEY = "MapOptions";
private static final LatLng LATLNG = new LatLng(45.506251123531,-73.73939635232091);
private static final float ZOOM = 10f;


public GoogleMapFragment() {
    super();
}

public static interface OnGoogleMapFragmentListener {
    void onMapReady();
}

public static GoogleMapFragment newInstance() {
    return new GoogleMapFragment();
}

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);

    setLocationAndZoom();

    //if we're dealing with a parent fragment that's controlling the view
    Fragment fragment = getParentFragment();
    if (fragment != null && fragment instanceof OnGoogleMapFragmentListener) {
        ((OnGoogleMapFragmentListener) fragment).onMapReady();
    }
    else if (getActivity()!=null && getActivity() instanceof OnGoogleMapFragmentListener){
    	((OnGoogleMapFragmentListener) getActivity()).onMapReady();
    }

    return view;
}

private void setLocationAndZoom(){
    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(LATLNG, ZOOM);
    this.getMap().moveCamera(cameraUpdate);
	}

}