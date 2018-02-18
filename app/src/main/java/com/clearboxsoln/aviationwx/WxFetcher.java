package com.clearboxsoln.aviationwx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mikes on 2/18/2018.
 */

public class WxFetcher {
    /**
     * http://aviationweather.gov/gis/scripts/MetarJSON.php?zoom=9&filter=prior&density=0&taf=false&bbox=-83.227132449815,30.662734846156,-80.480550418565,32.127979772662
     */

    static String URLstr = "http://aviationweather.gov/gis/scripts/MetarJSON.php";
    static String testURL = "http://aviationweather.gov/gis/scripts/MetarJSON.php?zoom=9&filter=prior&density=0&taf=false&bbox=-83.227132449815,30.662734846156,-80.480550418565,32.127979772662";

    public void printMetars() throws IOException {
        URL url = new URL(testURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
            //result.append(line);
        }
        rd.close();
    }
}
