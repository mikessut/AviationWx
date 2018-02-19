package com.clearboxsoln.aviationwx;

import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by mikes on 2/18/2018.
 */



public class MapListener implements GoogleMap.OnCameraIdleListener,
    GoogleMap.OnMapLongClickListener,
        View.OnClickListener {

    private GoogleMap mMap;

    public MapListener(GoogleMap gmap) {
        mMap = gmap;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.radioMETAR) {
            setMETAR();
        } else if (view.getId() == R.id.radioTAF) {
            setTAF();
        }
    }

    public enum QueryType {METAR, TAF};
    private QueryType mQueryType;

    public void setMETAR() { mQueryType = QueryType.METAR; }
    public void setTAF() { mQueryType = QueryType.TAF; }

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
