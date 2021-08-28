package com.example.quake;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {
    private  static final String  LOG_TAG= QueryUtils.class.getSimpleName();






    public static List<earthquake> fetchEarthquakeData(String USES_REQUEST_URL) {
        URL url = createUrl(USES_REQUEST_URL);
        // Create an empty ArrayList that we can start adding earthquakes to

        String jsonResponse=null;
        try{
            jsonResponse=makeHTTPRequest(url);
        }catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object

        // Return the {@link Event}
        return extractFeatureFromJson(jsonResponse);}
    // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
    // is formatted, a JSONException exception object will be thrown.
    // Catch the exception so the app doesn't crash, and print the error message to the logs.
    public static List<earthquake> extractFeatureFromJson(String SAMPLE_JSON_RESPONSE) {
        List<earthquake> earthquakesList = new ArrayList<>();
        try {
            if(SAMPLE_JSON_RESPONSE == null)return null;
            JSONObject root = new JSONObject(SAMPLE_JSON_RESPONSE);
            JSONArray features = root.optJSONArray("features");
            for (int i = 0, size = features != null ? (features).length() : 0; i<size; i++){
                JSONObject earth = features.optJSONObject(i);
                JSONObject properties = earth.optJSONObject("properties");
                assert properties != null;
                double magnitude = properties.getDouble("mag");
                long time = properties.getLong("time");
                String place = properties.getString("place");
                String webLink = properties.getString("url");
                earthquakesList.add(new earthquake(magnitude, place,time,webLink));
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakesList;
    }

    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse=null;
        InputStream inputStream = null;
        HttpURLConnection urlConnection=null;
        if(url ==null)return null;
        try{urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if(urlConnection.getResponseCode()==200){
                inputStream= urlConnection.getInputStream();
                jsonResponse=getInputStream(inputStream);
            }
            else {
                Log.e(LOG_TAG, "HTTP Response Code is "+urlConnection.getResponseCode());
            }

        }catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }
        finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String getInputStream(InputStream inputStream) throws IOException {
        StringBuilder output=new StringBuilder();
        InputStreamReader inputStreamReader= new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader =new BufferedReader(inputStreamReader);
        String line = reader.readLine();
        while (line != null) {
            output.append(line);
            line = reader.readLine();
        }

        return output.toString();
    }

    public  static URL createUrl(String link){
        URL url = null;
        try{
            if(link.length()==0)return null;
            url = new URL(link);
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG, "URL NOT in Format");
        }
        return url; }

}
