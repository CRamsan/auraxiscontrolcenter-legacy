package com.cesarandres.ps2link.module;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by cesar on 6/16/13.
 */
public class Connector {

    public static String getDataFrom(URL url) throws IOException {
        InputStream is = null;
        String result = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            // Convert the InputStream into a string
            StringBuffer builder = new StringBuffer();
            String line;
            while((line = rd.readLine()) != null) {
                builder.append(line);
                builder.append('\r');
            }

            rd.close();
            result = builder.toString();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return  result;
    }

}
