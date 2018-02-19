package com.clearboxsoln.aviationwx;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

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

    private static int GREEN = 0x7f39c134;
    private static int BLUE = 0x7f5c42f4;
    private static int RED = 0x7ff44242;
    private static int PURPLE = 0x7fe242f4;
    private static int GRAY = 0x7f9ea39e;

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

    /**
     *
     *
     int d = 50; // diameter
     Bitmap bm = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
     Canvas c = new Canvas(bm);
     Paint p = new Paint();
     p.setColor(0x7fff0003);
     c.drawCircle(d/2, d/2, d/2, p);

     // generate BitmapDescriptor from circle Bitmap
     BitmapDescriptor bmD = BitmapDescriptorFactory.fromBitmap(bm);
     mMap.addMarker(new MarkerOptions().
     position(ssi).
     icon(bmD).
     title("Marker at KSSI"));

     * @param result
     */
    private BitmapDescriptor getMarker(int color) {
        int d = 50; // diameter
        Bitmap bm = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint();
        p.setColor(color);
        c.drawCircle(d/2, d/2, d/2, p);

        // generate BitmapDescriptor from circle Bitmap
        return BitmapDescriptorFactory.fromBitmap(bm);
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

                BitmapDescriptor marker = getMarker(GRAY);
                String rawOb = "UNKN";
                try {
                    int ceil = airport.getJSONObject("properties").getInt("ceil");
                    double visib = airport.getJSONObject("properties").getDouble("visib");
                    rawOb = airport.getJSONObject("properties").getString("rawOb");

                    if ( (ceil <= 5) || (visib <= 1)) {
                        marker = getMarker(PURPLE);
                    } else if ( ((ceil > 5) && (ceil <= 10)) || ((visib > 1) && (visib <= 3)) ) {
                        marker = getMarker(RED);
                    } else if ( ((ceil > 10) && (ceil <= 30)) || ((visib > 3) && (visib <= 5)) ) {
                        marker = getMarker(BLUE);
                    } else if ( ((ceil > 5) && (ceil <= 10)) || ((visib > 1) && (visib <= 3)) ) {
                        marker = getMarker(GREEN);
                    }
                } catch (JSONException ex) {
                    Log.e("AviationWx",ex.toString());
                }

                mMap.addMarker(new MarkerOptions().
                        position(center).
                        icon(marker).
                        title(rawOb));
            }
        } catch (JSONException ex) {
            Log.e("AviationWx",ex.toString());
        }
    }
}
