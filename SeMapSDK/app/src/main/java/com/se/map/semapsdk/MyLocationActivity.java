package com.se.map.semapsdk;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;
import com.mapbox.services.android.telemetry.location.LocationEngineProvider;

/**
 * Author: Administrator;
 * Since 2018/4/14;
 * Description:
 */

public class MyLocationActivity extends AppCompatActivity implements LocationEngineListener {

    private MapView mapView;
    private LocationEngineListener locationEngineListener;
    private LocationEngine locationEngine;

    private MapboxMap mMapboxMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_location_activity);

        mapView = (MapView) findViewById(R.id.map_view);
        mapView.setStyleUrl("mapbox://styles/mapbox/streets-v9");
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.5427, 116.2317), 5));
                locationEngine = new LocationEngineProvider(MyLocationActivity.this).obtainBestLocationEngineAvailable();
                locationEngine.addLocationEngineListener(MyLocationActivity.this);
                locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
                locationEngine.activate();
//                mMapboxMap.setMyLocationEnabled(true);
            }
        });
    }


    @Override
    public void onConnected() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("garyhu","定位成功");
    }
}
