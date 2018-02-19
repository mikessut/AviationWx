package com.clearboxsoln.aviationwx;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by mikes on 2/18/2018.
 */

public class WxFetcher
extends AsyncTask<LatLngBounds, Void, JSONObject> {
    /**
     * Documentation
     * https://www.aviationweather.gov/help/webservice?page=metarjson
     *
     *
     * http://aviationweather.gov/gis/scripts/MetarJSON.php?zoom=9&filter=prior&density=0&taf=false
     *   &bbox=-83.227132449815,30.662734846156,-80.480550418565,32.127979772662
     */

    static String URL_BASE = "http://aviationweather.gov/gis/scripts/MetarJSON.php";
    static String testURL = "http://aviationweather.gov/gis/scripts/MetarJSON.php?zoom=9&filter=prior&density=0&taf=false&bbox=-83.227132449815,30.662734846156,-80.480550418565,32.127979772662";

    Exception mE;

    private GoogleMap mMap;

    public WxFetcher(GoogleMap map) {
        mMap = map;
    }

    @Override
    protected JSONObject doInBackground(LatLngBounds... latLngBounds) {
        LatLng ll = latLngBounds[0].southwest;
        LatLng ur = latLngBounds[0].northeast;
        String urlString = URL_BASE + String.format("?density=all&bbox=%f,%f,%f,%f", ll.longitude, ll.latitude,
                ur.longitude,ur.latitude);
        Log.v("AviationWx",urlString);
        String s = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;

            while ((line = rd.readLine()) != null) {
                //System.out.println(line);
                //result.append(line);
                s += line;
            }
            rd.close();
            return new JSONObject(s);
        } catch (IOException | JSONException ex) {
            mE = ex;
        }
        return new JSONObject();
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        LatLng ssi = new LatLng(31.1519722,-81.3910556);
        try {
            JSONArray airports = result.getJSONArray(("features"));
            for (int i = 0; i < airports.length(); i++) {
                JSONObject airport = (JSONObject)airports.get(i);
                JSONArray coords = airport.getJSONObject("geometry").getJSONArray("coordinates");

                LatLng center = new LatLng((double)coords.get(1), (double)coords.get(0));

                mMap.addCircle(new CircleOptions()
                        .center(center)
                        .radius(10000)
                        .strokeColor(Color.RED)
                        .fillColor(Color.BLUE));
            }
        } catch (JSONException ex) {
            Log.e("AviationWx",ex.toString());
        }
    }
}
