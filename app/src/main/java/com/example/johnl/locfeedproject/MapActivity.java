package com.example.johnl.locfeedproject;

/**
 * Created by johnl on 3/27/2017.
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.HashMap;


//An activity that displays a Google map with a marker (pin) to indicate a particular location.
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private UiSettings mUiSettings;
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();

    private String id;

    ArrayList<String> location_names;
    ArrayList<Double> latitudes, longitudes;
    ArrayList<Integer> location_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        id = "0";
        if (extras != null) {
            id = extras.getString("id").toString();
        }

        System.out.println("++++++======= ID in Map: " + id);

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_map);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        location_names = new ArrayList<String>();
        latitudes = new ArrayList<Double>();
        longitudes = new ArrayList<Double>();
        location_ids = new ArrayList<Integer>();

        try {
            String result = new GetLocations().execute().get();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        for (int i = 0; i < location_names.size(); i++) {
            LatLng newLocation = new LatLng(latitudes.get(i), longitudes.get(i));
            Marker marker = googleMap.addMarker(new MarkerOptions().position(newLocation).title(location_names.get(i)));

            mHashMap.put(marker, location_ids.get(i));
        }

        LatLng middle = new LatLng(39.981469143334614, -75.15679478645325);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(middle, 15));
        mUiSettings = googleMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setCompassEnabled(true);

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent bringFeed = new Intent(MapActivity.this, ChooseActivity.class);
                bringFeed.putExtra("LocationID", mHashMap.get(marker).toString());
                bringFeed.putExtra("id", id);
                startActivity(bringFeed);
            }
        });
    }

    private class GetLocations extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try {
                String link = "https://locfeed.000webhostapp.com/android_connect/get_locations.php";
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();

                    String line;
                    try {
                        while ((line = reader.readLine()) != null) {
                            sb.append(line).append('\n');
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    String jsonString = sb.toString();

                    if (jsonString != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonString);

                            JSONArray locations = jsonObject.getJSONArray("locations");

                            for (int i = 0; i < locations.length(); i++) {
                                JSONObject location = locations.getJSONObject(i);
                                String location_name = location.getString("location_name");
                                double latitude = Double.parseDouble(location.getString("latitude"));
                                double longitude = Double.parseDouble(location.getString("longitude"));
                                int location_id = Integer.parseInt(location.getString("id"));

                                System.out.println("location_name: " + location_name);
                                System.out.println("latitude: " + latitude);
                                System.out.println("longitude: " + longitude);

                                location_names.add(location_name);
                                latitudes.add(latitude);
                                longitudes.add(longitude);
                                location_ids.add(location_id);
                            }
                        } catch (final JSONException e) {
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
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }
}