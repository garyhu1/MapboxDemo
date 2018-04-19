package com.se.map.semapsdk;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.TrackingSettings;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.se.map.semapsdk.permission.EasyPermission;
import com.se.map.semapsdk.permission.PermissionCallBackM;
import com.se.map.semapsdk.utils.CustomToast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2018/3/22.
 */

public class LocationActivity extends AppCompatActivity implements EasyPermission.PermissionCallback {

    private MapView mapView;
    private FloatingActionButton floatingActionButton;
    private TextView loadText;
    private TextView addressText;
    private LocationEngineListener locationEngineListener;
    private LocationEngine locationEngine;
    private MapboxMap mMapboxMap;

    private int mRequestCode;
    private String[] mPermissions;
    private PermissionCallBackM mPermissionCallBack;

    private final static int MY_LOCATION_PERM = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.location_btn);
        loadText = (TextView) findViewById(R.id.load);
        addressText = (TextView) findViewById(R.id.address);
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.setStyleUrl("mapbox://styles/mapbox/streets-v9");
        mapView.onCreate(savedInstanceState);

        locationEngine = Mapbox.getLocationEngine();

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.5427,116.2317),5));
                if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Location lastlocation = locationEngine.getLastLocation();
                    if (lastlocation != null) {
                        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastlocation), 5), 1000);
                    }
                }
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mMapboxMap!=null){
                    loadText.setText("loadding");
                    toggleGps();
                }
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //开关定位
    private void toggleGps() {
        if (mMapboxMap.isMyLocationEnabled()) {//已开启
            enableLocation(false);//若已开启，就将它关闭
        } else {//未开启
            requestPermission(MY_LOCATION_PERM, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    getString(R.string.rationale_location), new PermissionCallBackM() {
                        @Override
                        public void onPermissionGrantedM(int requestCode, String... perms) {
                            enableLocation(true);//若未开启，就在检查完权限后，将它打开
                        }

                        @Override
                        public void onPermissionDeniedM(int requestCode, String... perms) {
                            CustomToast.showShortToast(LocationActivity.this, "拒绝该权限后会影响地图的定位操作，建议重新申请");
                        }
                    });
        }
    }

    public void enableLocation(boolean enable) {
        TrackingSettings trackingSettings = mMapboxMap.getTrackingSettings();
        if (enable) {
            //获取上次定位参数,如果存在先直接使用
            Location lastlocation;
            // 检查下权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                lastlocation = locationEngine.getLastLocation();
            }else {
                lastlocation = null;
            }
            if (lastlocation != null) {
                mMapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastlocation), 5), 1000);
            }
            // 定位监听
            locationEngineListener = new LocationEngineListener() {
                @Override
                public void onConnected() {
                    //连接到定位服务
                }

                @Override
                public void onLocationChanged(Location location) {
                    Location myLocation = mMapboxMap.getMyLocation();
                    Log.e("garyhu","myLocation ------->"+myLocation);
                    Log.e("garyhu","lat ------->"+myLocation.getLatitude());
                    Log.e("garyhu","lon -------->"+myLocation.getLongitude());
                    if (location != null) {
                        mMapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location), 5), 1000);
                        Geocoder geocoder = new Geocoder(LocationActivity.this, Locale.CHINESE);
                        try {
                            StringBuilder sb = new StringBuilder();
                            List<Address> fromLocation = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            for(Address address : fromLocation){
                                Log.e("garyhu","address == "+address.getCountryName());// 中国
                                Log.e("garyhu","getAdminArea == "+address.getAdminArea());//江苏省
                                Log.e("garyhu","getFeatureName == "+address.getFeatureName());
                                Log.e("garyhu","getSubLocality == "+address.getSubLocality());
                                Log.e("garyhu","getLocality == "+address.getLocality());//苏州市
                                sb.append(address.getCountryName());
                                sb.append(address.getAdminArea());
                                sb.append(address.getLocality());
                                sb.append(address.getSubLocality());
                                sb.append(address.getAddressLine(2));
                                for(int i=0;i < address.getMaxAddressLineIndex();i++){
                                    Log.e("garyhu","getAddressLine == "+address.getAddressLine(i));
                                }
//                                Log.e("garyhu","getAddressLine0 == "+address.getAddressLine(0));
//                                Log.e("garyhu","getAddressLine1 == "+address.getAddressLine(1));
//                                Log.e("garyhu","getAddressLine2 == "+address.getAddressLine(2));
//                                Log.e("garyhu","getAddressLin3 == "+address.getAddressLine(3));
                            }
                            loadText.setText("加载完成");
                            addressText.setText(sb.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        locationEngine.removeLocationEngineListener(this);
                    }
                }
            };
            //设置监听器
            locationEngine.addLocationEngineListener(locationEngineListener);
            locationEngine.activate();
            floatingActionButton.setImageResource(R.drawable.location_disable);

            // 让地图始终以定位点为中心，无法滑动
//            trackingSettings.setDismissAllTrackingOnGesture(false);
            // 启用位置和方位跟踪
//            trackingSettings.setMyLocationTrackingMode(MyLocationTracking.TRACKING_FOLLOW);
//            trackingSettings.setMyBearingTrackingMode(MyBearingTracking.COMPASS);
        } else {
            // 让地图始终以定位点为中心，无法滑动
//            trackingSettings.setDismissAllTrackingOnGesture(true);
            floatingActionButton.setImageResource(R.drawable.my_location);
        }
        //添加或移除定位图层
        mMapboxMap.setMyLocationEnabled(enable);

        mMapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng point) {

            }
        });

        mMapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                return false;
            }
        });
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
        if (locationEngine != null && locationEngineListener != null) {
            locationEngine.activate();
            if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationEngine.requestLocationUpdates();
            locationEngine.addLocationEngineListener(locationEngineListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        if (locationEngine != null && locationEngineListener != null) {
            locationEngine.removeLocationEngineListener(locationEngineListener);
            locationEngine.removeLocationUpdates();
            locationEngine.deactivate();
        }
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
        if (locationEngineListener != null) {
            locationEngine.removeLocationEngineListener(locationEngineListener);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    // 授权
    @Override
    public void onEasyPermissionGranted(int requestCode, String... perms) {
        if (mPermissionCallBack != null) {
            mPermissionCallBack.onPermissionGrantedM(requestCode, perms);
        }
    }

    // 拒绝
    @Override
    public void onEasyPermissionDenied(final int requestCode, final String... perms) {

        //rationale: Never Ask Again后的提示信息
        if (EasyPermission.checkDeniedPermissionsNeverAskAgain(this, "请去系统设置里授权", android.R.string.ok,
                android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (mPermissionCallBack != null) {
                            mPermissionCallBack.onPermissionDeniedM(
                                    requestCode, perms);
                        }
                    }
                }, perms)) {
            return;
        }

        if (mPermissionCallBack != null) {
            mPermissionCallBack.onPermissionDeniedM(requestCode, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    //rationale: 申请授权理由
    public void requestPermission(int requestCode, String[] permissions, String rationale,
                                  PermissionCallBackM permissionCallback) {
        this.mRequestCode = requestCode;
        this.mPermissionCallBack = permissionCallback;
        this.mPermissions = permissions;

        EasyPermission.with(this)
                .addRequestCode(requestCode)
                .permissions(permissions)
                //.nagativeButtonText(android.R.string.ok)
                //.positveButtonText(android.R.string.cancel)
                .rationale(rationale)
                .request();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*
            从Settings界面跳转回来，标准代码，就这么写
        */
        if (requestCode == EasyPermission.SETTINGS_REQ_CODE) {
            if (EasyPermission.hasPermissions(this, mPermissions)) {
                //已授权，处理业务逻辑
                onEasyPermissionGranted(mRequestCode, mPermissions);
            } else {
                onEasyPermissionDenied(mRequestCode, mPermissions);
            }
        }
    }
}
