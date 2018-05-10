package com.se.map.semapsdk;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerView;
import com.mapbox.mapboxsdk.annotations.MarkerViewManager;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.geometry.VisibleRegion;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.se.map.semapsdk.net.PoiRequest;
import com.se.map.semapsdk.utils.DistanceUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class MarkerActivity extends AppCompatActivity {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private ArrayList<String> urls;

    private List<PoiEntity.DataBean> data;
    private MarkerViewManager markerViewManager ;


    private final static int LOAD_IMG = 0x001;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LOAD_IMG:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);

        urls = new ArrayList();
        urls.add("http://img4.imgtn.bdimg.com/it/u=307051832,2958828818&fm=27&gp=0.jpg");
        urls.add("http://pic1.nipic.com/2008-08-14/200881415416310_2.jpg");
        urls.add("http://img1.tplm123.com/2010/04/03/37478/14834771252048.png");

        mapView = (MapView) findViewById(R.id.map_view);
        mapView.setStyleUrl("mapbox://styles/mapbox/streets-zh-v1");
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                MarkerActivity.this.mapboxMap = mapboxMap;
                markerViewManager = mapboxMap.getMarkerViewManager();
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.5427,116.2317),5));
                addMaker(mapboxMap);
                addImgMaker(mapboxMap);
                loadMarker();

                mapboxMap.addOnCameraMoveCancelListener(new MapboxMap.OnCameraMoveCanceledListener() {
                    @Override
                    public void onCameraMoveCanceled() {
                        Log.e("garyhu","over");
//                        show();
                    }
                });

                mapboxMap.addOnCameraMoveListener(new MapboxMap.OnCameraMoveListener() {
                    @Override
                    public void onCameraMove() {
//                        mapboxMap.removeAnnotations();
//                        show();
                    }
                });

            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mapView.addOnMapChangedListener(new MapView.OnMapChangedListener() {
            @Override
            public void onMapChanged(int change) {
//                Log.e("garyhu","mapboxMap ------>"+mapboxMap);
                if(mapboxMap!=null){
                    VisibleRegion visibleRegion = mapboxMap.getProjection().getVisibleRegion();
                    final LatLngBounds latLngBounds = visibleRegion.latLngBounds;
//                    Log.e("garyhu","left ------>"+latLngBounds.getLatNorth());
//                    Log.e("garyhu","top ------>"+latLngBounds.getLatSouth());
//                    Log.e("garyhu","right ------>"+latLngBounds.getLonEast());
//                    Log.e("garyhu","bottom ------>"+latLngBounds.getLonWest());
                }
            }
        });

        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void loadMarker(){

        PoiRequest poiRequest = new PoiRequest();

        poiRequest.request();

        poiRequest.addPoiListener(new PoiRequest.PoiCallback() {
            @Override
            public void showPoiContent(PoiEntity poiEntity) {
                data = poiEntity.getData();
                show();
            }
        });
    }

    public void show(){
        double standarad = DistanceUtil.dp2px(this,50);
        List<List<PoiEntity.DataBean>> container = new ArrayList<>();
        DistanceUtil.filterPoiData(mapboxMap,data,container,standarad);
//        for (int i = 0; i < container.size(); i++) {
//            List<PoiEntity.DataBean> dataBeans = container.get(i);
//            PoiEntity.DataBean dataBean = dataBeans.get(0);
//            ArrayList<String> urls = new ArrayList<>();
//            for (int j = 0; j < dataBeans.size(); j++) {
//                if(j<3){
//                    PoiEntity.DataBean dataBean1 = dataBeans.get(j);
//                    urls.add(dataBean1.getImg());
//                }
//            }
//            CountryMarkerViewOptions options = new CountryMarkerViewOptions();
//            options.resIds(urls);
//            options.position(new LatLng(dataBean.getY(), dataBean.getX()));
//            options.skew(false);
//            options.flat(false);
//            mapboxMap.addMarker(options);
//        }
//        markerViewManager.addMarkerViewAdapter(new CountryAdapter(MarkerActivity.this, mapboxMap));
        IconFactory instance = IconFactory.getInstance(this);
        for (int i = 0; i < container.size(); i++) {
            List<PoiEntity.DataBean> dataBeans = container.get(i);
            PoiEntity.DataBean dataBean = dataBeans.get(0);
            View view = LayoutInflater.from(this).inflate(R.layout.view_custom_marker,null);
            SimpleDraweeView img1 = (SimpleDraweeView) view.findViewById(R.id.img1);
            SimpleDraweeView img2 = (SimpleDraweeView) view.findViewById(R.id.img2);
            SimpleDraweeView img3 = (SimpleDraweeView) view.findViewById(R.id.img3);
            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.r_layout);
            ImageView locationImg = (ImageView)view.findViewById(R.id.location_img);
            for (int j = 0; j < dataBeans.size(); i++) {
                if(j==0){
                    img1.setImageURI(dataBeans.get(0).getImg());
                }else if(j==1){
                    img2.setVisibility(View.VISIBLE);
                    img2.setImageURI(dataBeans.get(1).getImg());
                }else if(j==2){
                    img3.setVisibility(View.VISIBLE);
                    img3.setImageURI(dataBeans.get(2).getImg());
                }
            }
            Bitmap bitmap = loadBitmapFromView(view);
            Icon icon = instance.fromBitmap(bitmap);
            MarkerOptions option = new MarkerOptions()
                    .position(new LatLng(dataBean.getY(), dataBean.getX()))
                    .icon(icon);
            mapboxMap.addMarker(option);
        }
    }

    private Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }

    // 为图片target添加水印
    private Bitmap createWatermarkBitmap(Bitmap target, String str) {
        int w = target.getWidth();
        int h = target.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);

        Paint p = new Paint();

        // 水印的颜色
        p.setColor(Color.RED);

        // 水印的字体大小
        p.setTextSize(16);

        p.setAntiAlias(true);// 去锯齿

        canvas.drawBitmap(target, 0, 0, p);

        // 在中间位置开始添加水印
        canvas.drawText(str, w / 2, h / 2, p);

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return bmp;
    }

    /**
     * 添加maker
     */
    public void addMaker(MapboxMap mapboxMap){
        // 设置maker样式和位置
        IconFactory iconFactory = IconFactory.getInstance(this);
        Icon icon = iconFactory.fromResource(R.mipmap.semap_mylocation_icon_bearing);
        Icon icon1 = iconFactory.fromResource(R.mipmap.ic_launcher_round);
        MarkerOptions markerOptions1 = new MarkerOptions()
                .position(new LatLng(31.264502,120.739194))
                .title("MarkerTitle")
                .snippet("MarkerInfo")
                .icon(icon);
        MarkerOptions markerOptions2= new MarkerOptions()
                .position(new LatLng(31.419861, 120.514911))
                .title("MarkerTitle")
                .snippet("MarkerInfo")
                .icon(icon);
        MarkerOptions markerOptions3= new MarkerOptions()
                .position(new LatLng(39.619861, 116.514911))
                .title("MarkerTitle")
                .snippet("MarkerInfo")
                .icon(icon);
        MarkerOptions markerOptions4= new MarkerOptions()
                .position(new LatLng(39.62861, 116.515911))
                .title("MarkerTitle")
                .snippet("MarkerInfo")
                .icon(icon1);
        // 添加maker
        mapboxMap.addMarker(markerOptions1);
        mapboxMap.addMarker(markerOptions2);
        mapboxMap.addMarker(markerOptions3);
        mapboxMap.addMarker(markerOptions4);
//        如果想添加多个maker，需要重复以上操作，
//        删除时需要遍历删除 如下
//        for (Marker marker : mapboxMap.getMarkers()) {
//            mapboxMap.removeMarker(marker);
//        }
    }

    // 添加图片标记
    public void addImgMaker(MapboxMap mapboxMap){
        Icon picIcon = IconFactory.getInstance(this).fromResource(R.mipmap.ic_launcher);
        Icon markerPic = IconFactory.getInstance(this).fromResource(R.mipmap.maker);

        mapboxMap.addMarker(new MarkerViewOptions()
                .title("超擎")
                .position(new LatLng(38.629861, 116.524911))
                .icon(picIcon)
        );
        MarkerViewOptions markerOptions0 = new MarkerViewOptions()
                .position(new LatLng(31.619861, 120.515911))

                .icon(picIcon);
        mapboxMap.addMarker(markerOptions0);

        MarkerViewManager markerViewManager = mapboxMap.getMarkerViewManager();

        mapboxMap.addMarker(new TextMarkerViewOptions()
                .text("超擎")
                .position(new LatLng(39.629861, 117.594911))
        );

        markerViewManager.addMarkerViewAdapter(new TextAdapter(MarkerActivity.this, mapboxMap));

        MarkerOptions m = new MarkerOptions()
                .position(new LatLng(39.919361, 116.514511))
                .title("MarkerTitle")
                .snippet("MarkerInfo")
                .icon(markerPic);
        mapboxMap.addMarker(m);

    }

    /**
    *    get image from network
    *    @param  url
    *    @return [BitMap]image
    */
    public Bitmap returnBitMap(String url){
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
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

    /**
     * 用来展示图片的适配器.
     */
    private static class CountryAdapter extends MapboxMap.MarkerViewAdapter<CountryMarkerView> {

        private LayoutInflater inflater;
        private MapboxMap mapboxMap;

        CountryAdapter(@NonNull Context context, @NonNull MapboxMap mapboxMap) {
            super(context);
            this.inflater = LayoutInflater.from(context);
            this.mapboxMap = mapboxMap;
        }

        @Nullable
        @Override
        public View getView(@NonNull CountryMarkerView marker, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.view_custom_marker, parent, false);
                viewHolder.img1 = (SimpleDraweeView) convertView.findViewById(R.id.img1);
                viewHolder.img2 = (SimpleDraweeView) convertView.findViewById(R.id.img2);
                viewHolder.img3 = (SimpleDraweeView) convertView.findViewById(R.id.img3);
                viewHolder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.r_layout);
                viewHolder.locationImg = (ImageView)convertView.findViewById(R.id.location_img);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            boolean skew = marker.isSkew();
            if(skew){
                viewHolder.locationImg.setVisibility(View.VISIBLE);
                int locationMarker = marker.getLocationMarker();
                if(locationMarker>0){
                    viewHolder.locationImg.setImageResource(locationMarker);
                }
                viewHolder.relativeLayout.setRotationX(60f);
            }else {
                viewHolder.locationImg.setVisibility(View.GONE);
            }
            List<String> flagRes = marker.getFlagRes();
            for (int i = 0; i < flagRes.size(); i++) {
                if(i==0){
                    viewHolder.img1.setImageURI(flagRes.get(0));
                }else if(i==1){
                    viewHolder.img2.setVisibility(View.VISIBLE);
                    viewHolder.img2.setImageURI(flagRes.get(1));
                }else if(i==2){
                    viewHolder.img3.setVisibility(View.VISIBLE);
                    viewHolder.img3.setImageURI(flagRes.get(2));
                }
            }
//            viewHolder.img1.setImageURI("http://img4.imgtn.bdimg.com/it/u=307051832,2958828818&fm=27&gp=0.jpg");
//            viewHolder.img2.setImageURI("http://pic1.nipic.com/2008-08-14/200881415416310_2.jpg");
//            viewHolder.img3.setImageURI("http://img1.tplm123.com/2010/04/03/37478/14834771252048.png");
//            viewHolder.img.setImages(marker.getFlagRes());
//            viewHolder.img.setFlat(skew);
            return convertView;
        }

        @Override
        public boolean onSelect(
                @NonNull final CountryMarkerView marker, @NonNull final View convertView, boolean reselectionForViewReuse) {
            convertView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(convertView, View.ROTATION, 0, 360);
            rotateAnimator.setDuration(reselectionForViewReuse ? 0 : 350);
            rotateAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    convertView.setLayerType(View.LAYER_TYPE_NONE, null);
                    mapboxMap.selectMarker(marker);
                }
            });
//            rotateAnimator.start();

            // false indicates that we are calling selectMarker after our animation ourselves
            // true will let the system call it for you, which will result in showing an InfoWindow instantly
            return true;
        }

        @Override
        public void onDeselect(@NonNull CountryMarkerView marker, @NonNull final View convertView) {
            convertView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(convertView, View.ROTATION, 360, 0);
            rotateAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    convertView.setLayerType(View.LAYER_TYPE_NONE, null);
                }
            });
//            rotateAnimator.start();
        }

        private static class ViewHolder {
            SimpleDraweeView img1,img2,img3;
            RelativeLayout relativeLayout;
            ImageView locationImg;
        }
    }

    /**
     * Adapts a MarkerView to display text  in a TextView.
     */
    public static class TextAdapter extends MapboxMap.MarkerViewAdapter<TextMarkerView> {

        private LayoutInflater inflater;
        private MapboxMap mapboxMap;

        public TextAdapter(@NonNull Context context, @NonNull MapboxMap mapboxMap) {
            super(context);
            this.inflater = LayoutInflater.from(context);
            this.mapboxMap = mapboxMap;
        }

        @Nullable
        @Override
        public View getView(@NonNull TextMarkerView marker, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.view_text_marker, parent, false);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.text_view);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textView.setText(marker.getText());
            return convertView;
        }

        @Override
        public boolean onSelect(
                @NonNull final TextMarkerView marker, @NonNull final View convertView, boolean reselectionForViewReuse) {
            animateGrow(marker, convertView, 0);

            // false indicates that we are calling selectMarker after our animation ourselves
            // true will let the system call it for you, which will result in showing an InfoWindow instantly
            return false;
        }

        @Override
        public void onDeselect(@NonNull TextMarkerView marker, @NonNull final View convertView) {
            animateShrink(convertView, 350);
        }

        @Override
        public boolean prepareViewForReuse(@NonNull MarkerView marker, @NonNull View convertView) {
            // this method is called before a view will be reused, we need to restore view state
            // as we have scaled the view in onSelect. If not correctly applied other MarkerView will
            // become large since these have been recycled

            // cancel ongoing animation
            convertView.animate().cancel();

            if (marker.isSelected()) {
                // shrink view to be able to be reused
                animateShrink(convertView, 0);
            }

            // true if you want reuse to occur automatically, false if you want to manage this yourself
            return true;
        }

        private void animateGrow(@NonNull final MarkerView marker, @NonNull final View convertView, int duration) {
            convertView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            Animator animator = AnimatorInflater.loadAnimator(convertView.getContext(), R.animator.scale_up);
            animator.setDuration(duration);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    convertView.setLayerType(View.LAYER_TYPE_NONE, null);
                    mapboxMap.selectMarker(marker);
                }
            });
            animator.setTarget(convertView);
            animator.start();
        }

        private void animateShrink(@NonNull final View convertView, int duration) {
            convertView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            Animator animator = AnimatorInflater.loadAnimator(convertView.getContext(), R.animator.scale_down);
            animator.setDuration(duration);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    convertView.setLayerType(View.LAYER_TYPE_NONE, null);
                }
            });
            animator.setTarget(convertView);
            animator.start();
        }

        private static class ViewHolder {
            TextView textView;
        }
    }
}
