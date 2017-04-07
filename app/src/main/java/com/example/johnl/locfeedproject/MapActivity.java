package com.example.johnl.locfeedproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private UiSettings mUiSettings;

    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_map);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Temple, Philadelphia, PA,
        // and move the map's camera to the same location.
        /*LatLng temple = new LatLng(39.98065114940919, -75.15505135059357);
        googleMap.addMarker(new MarkerOptions().position(temple).title("Tuttleman Building"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(temple, 15));

        LatLng serc = new LatLng(39.98167960047677, -75.1530547141374);
        googleMap.addMarker(new MarkerOptions().position(serc).title("SERC"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(serc, 15));*/

        new GetLocations().execute(googleMap);

        googleMap.getUiSettings();
        mUiSettings = googleMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setCompassEnabled(true);

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent bringFeed = new Intent(MapActivity.this, TestActivity.class);
                startActivity(bringFeed);

                return false;
            }
        });
    }

    private class GetLocations extends AsyncTask<GoogleMap, GoogleMap, GoogleMap>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected GoogleMap doInBackground(GoogleMap... arg0){
            try{
                String link = "https://locfeed.000webhostapp.com/android_connect/get_locations.php";
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);

                int responseCode = conn.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();

                    String line;
                    try{
                        while((line = reader.readLine()) != null){
                            sb.append(line).append('\n');
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    } finally {
                        try{
                            in.close();
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }

                    String jsonString = sb.toString();

                    if(jsonString != null){
                        try{
                            JSONObject jsonObject = new JSONObject(jsonString);

                            JSONArray locations = jsonObject.getJSONArray("locations");

                            for(int i = 0; i < locations.length(); i++){
                                JSONObject location = locations.getJSONObject(i);
                                String location_name = location.getString("location_name");
                                double latitude = Double.parseDouble(location.getString("latitude"));
                                double longitude = Double.parseDouble(location.getString("longitude"));

                                LatLng newLocation = new LatLng(latitude, longitude);
                                arg0[0].addMarker(new MarkerOptions().position(newLocation).title(location_name));
                                arg0[0].moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 15));
                            }
                        } catch(final JSONException e){
                            Log.e("JSON Error", "JSON Parsing Error: " + e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Json parsing error: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }

            } catch (Exception e){
                System.out.println("Error: " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(GoogleMap result){
            super.onPostExecute(result);
        }
    }
}