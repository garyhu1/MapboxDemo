package com.se.map.semapsdk;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/22.
 */

public class MarkerAddActivity extends AppCompatActivity {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private ArrayList<MarkerOptions> markerList = new ArrayList<>();

    private static final DecimalFormat LAT_LON_FORMATTER = new DecimalFormat("#.#####");

    private static String STATE_MARKER_LIST = "markerList";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_marker);

        mapView = (MapView) findViewById(R.id.map_view);
        mapView.setStyleUrl("mapbox://styles/mapbox/streets-v9");
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap map) {
                mapboxMap = map;
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.5427,116.2317),5));
                resetMap();

                mapboxMap.setOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(@NonNull LatLng point) {
                        addMarker(point);
                    }
                });

                mapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng point) {
                        addMarker(point);
                    }
                });

                if (savedInstanceState != null) {
                    markerList = savedInstanceState.getParcelableArrayList(STATE_MARKER_LIST);
                    mapboxMap.addMarkers(markerList);
                }
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetMap();
            }
        });
    }

    private void addMarker(LatLng point) {
        final PointF pixel = mapboxMap.getProjection().toScreenLocation(point);

        String title = LAT_LON_FORMATTER.format(point.getLatitude()) + ", "
                + LAT_LON_FORMATTER.format(point.getLongitude());
        String snippet = "X = " + (int) pixel.x + ", Y = " + (int) pixel.y;

        MarkerOptions marker = new MarkerOptions()
                .position(point)
                .title(title)
                .snippet(snippet);

        markerList.add(marker);
        mapboxMap.addMarker(marker);
    }

    private void resetMap() {
        if (mapboxMap == null) {
            return;
        }
        mapboxMap.removeAnnotations();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
