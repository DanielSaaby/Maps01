package com.easv.oe.maps01;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends ActionBarActivity {

    private static String LOGTAG = "MAP_LOG";

    private final LatLng EASV = new LatLng(55.488230, 8.446936);
    private final LatLng BAKER = new LatLng(55.485386, 8.451585);
    private final LatLng ROUND = new LatLng(55.473939, 8.435959);


    MarkerOptions easv_marker;
    MarkerOptions baker_marker;
    MarkerOptions round_marker;


    private GoogleMap m_map;

    Spinner m_zoomLevelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(LOGTAG, "getting the map async");
         ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
             @Override
             public void onMapReady(GoogleMap googleMap) {
                 Log.d(LOGTAG, "The map is ready - adding markers");
                 m_map = googleMap;
                 m_map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                     @Override
                     public boolean onMarkerClick(Marker marker) {
                         if (marker.getPosition().equals(easv_marker.getPosition()))
                             Toast.makeText(MainActivity.this, "EASV hit!", Toast.LENGTH_LONG).show();
                         else
                             Toast.makeText(MainActivity.this, "EASV NOT hit!", Toast.LENGTH_LONG).show();
                         return false;
                     }
                 });

                 easv_marker = new MarkerOptions().position(EASV).title("EASV is HERE!");
                 baker_marker = new MarkerOptions().position(BAKER).title("Højvangs bageren").icon(BitmapDescriptorFactory.fromResource(R.drawable.cake));
                 round_marker = new MarkerOptions().alpha((float)0.3).position(ROUND).title("Round about");

                 m_map.addMarker(easv_marker);
                 m_map.addMarker(baker_marker);
                 m_map.addMarker(round_marker);

                 m_zoomLevelView = (Spinner) findViewById(R.id.spinnerZoomLevel);

                 setupZoomLevel();
             }
         });



    }

    public void onClickEASV(View v) {
        int level = Integer.parseInt(m_zoomLevelView.getSelectedItem().toString());
        CameraUpdate viewPoint = CameraUpdateFactory.newLatLngZoom(EASV, level);
        // zoomlevel 0..21, where 0 is the world and 21 is single street
        Log.d(LOGTAG, "Will zoom to easv to level " + level);
        m_map.animateCamera(viewPoint);

    }

    void setupZoomLevel() {

        // Create an ArrayAdapter using the string array and a default m_zoomLevelView layout
        ArrayAdapter<CharSequence> adapter =
           ArrayAdapter.createFromResource(this,
                                           R.array.zoomlevels,
                                           android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        m_zoomLevelView.setAdapter(adapter);
    }
}
