package com.clearboxsoln.aviationwx;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import com.clearboxsoln.aviationwx.WxFetcher;
import com.google.android.gms.maps.model.LatLng;

import static org.junit.Assert.*;

/**
 * Created by mikes on 2/18/2018.
 */

public class TestWxFetcher {

    @Test
    public void print_metars() throws Exception {
        WxFetcher wx = new WxFetcher();
        //JSONObject j = wx.getMetars(new LatLng(-83.227132449815,30.662734846156),
        //        new LatLng(-80.480550418565,32.127979772662));
        JSONObject j = new JSONObject("{\"foo\": 123}");
        System.out.println(j.getInt("foo"));
        JSONArray reports = (JSONArray) j.get("features");
        JSONObject airport = (JSONObject)((JSONObject)reports.get(0)).get("properties");
        System.out.println(airport.keys());
        assertEquals(4, 2 + 2);
    }
}
