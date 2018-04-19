package com.se.map.semapsdk;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.se.map.semapsdk.areacode.AreaCodeActivity;
import com.se.map.semapsdk.net.GenerateSignActivity;
import com.se.map.semapsdk.net.PoiContentActivity;
import com.se.map.semapsdk.permission.EasyPermission;
import com.se.map.semapsdk.permission.PermissionCallBackM;
import com.se.map.semapsdk.utils.CustomToast;
import com.se.map.semapsdk.utils.DistanceUtil;

public class MainActivity extends AppCompatActivity implements EasyPermission.PermissionCallback {

    private int mRequestCode;
    private String[] mPermissions;
    private PermissionCallBackM mPermissionCallBack;

    private final static int RC_LOCATION_PERM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPermission();

        findViewById(R.id.location_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LocationActivity.class));
            }
        });

        findViewById(R.id.marker_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MarkerActivity.class));
            }
        });

        findViewById(R.id.add_marker_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MarkerAddActivity.class));
            }
        });

        findViewById(R.id.poi_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,PoiContentActivity.class));
            }
        });

        findViewById(R.id.my_location_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MyLocationActivity.class));
            }
        });

        findViewById(R.id.sign_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,GenerateSignActivity.class));
            }
        });

        findViewById(R.id.area_code_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AreaCodeActivity.class));
            }
        });

        double distance = DistanceUtil.getDistance(120.002, 30.001, 120.003, 30.002);
        Log.d("garyhu","distance == "+distance);
    }


    public void getPermission(){
        requestPermission(RC_LOCATION_PERM, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                getString(R.string.rationale_location), new PermissionCallBackM() {
                    @Override
                    public void onPermissionGrantedM(int requestCode, String... perms) {
                    }

                    @Override
                    public void onPermissionDeniedM(int requestCode, String... perms) {
                        CustomToast.showShortToast(MainActivity.this, "拒绝该权限后会影响地图的定位操作，建议重新申请");
                    }
                });
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
