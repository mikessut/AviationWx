package com.clearboxsoln.aviationwx;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by mikes on 2/18/2018.
 */

public class MapListener implements GoogleMap.OnCameraIdleListener,
    GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;

    public MapListener(GoogleMap gmap) {
        mMap = gmap;
    }

    @Override
    public void onCameraIdle() {
        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        float z = mMap.getCameraPosition().zoom;
        Log.v("AviationWx",String.format("onCameraIdle: %s, %f", bounds, z));
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Log.v("AviationWx",String.format("onMapLongClick:"));

        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;

        WxFetcher wxf = new WxFetcher(mMap);
        wxf.execute(bounds);

    }
}
