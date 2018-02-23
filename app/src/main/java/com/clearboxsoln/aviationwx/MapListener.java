package com.clearboxsoln.aviationwx;

import android.util.Log;
import android.view.View;
import android.widget.Button;

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


    public enum QueryType {METAR, TAF, PIREP};
    private QueryType mQueryType;


    public MapListener(GoogleMap gmap) {
        mMap = gmap;
        mQueryType = QueryType.METAR;
    }

    @Override
    public void onClick(View view) {
        View parent = (View)view.getParent();
        Button b = (Button) parent.findViewById(R.id.toggle);
        if (mQueryType == QueryType.METAR) {
            setTAF();
            b.setText("TAF");
        } else if (mQueryType == QueryType.TAF) {
            setPIREP();
            b.setText("PIREP");
        } else if (mQueryType == QueryType.PIREP) {
            setMETAR();
            b.setText("METAR");
        }
    }

    public void setMETAR() { mQueryType = QueryType.METAR; }
    public void setTAF() { mQueryType = QueryType.TAF; }
    public void setPIREP() { mQueryType = QueryType.PIREP; }

    @Override
    public void onCameraIdle() {
        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        float z = mMap.getCameraPosition().zoom;
        //Log.v("AviationWx",String.format("onCameraIdle: %s, %f", bounds, z));
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Log.v("AviationWx",String.format("onMapLongClick:"));

        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;

        WxFetcher wxf = new WxFetcher(mMap, mQueryType);
        wxf.execute(bounds);

    }
}
