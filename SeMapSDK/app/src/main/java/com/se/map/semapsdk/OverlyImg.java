package com.se.map.semapsdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 叠加图片
 */

public class OverlyImg extends View {

    private int width;
    private int height;
    private List<Bitmap> bitmaps;

    private Context context;

    private int centerX,centerY;
    private boolean mFlat;

    public OverlyImg(Context context) {
        this(context,null);
    }

    public OverlyImg(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public OverlyImg(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setImages(Bitmap[] resIds){
        if(bitmaps == null){
            bitmaps = new ArrayList<>();
        }
        bitmaps.clear();
        for(int i = 0; i < resIds.length;i++){
            Bitmap bm = resIds[i];
            bitmaps.add(getBitmap(bm,40,40));
        }

        invalidate();
    }

    public void setFlat(boolean enable){
        this.mFlat = enable;
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if(this.mFlat){
            layoutParams.width = (int) dp2px(50);
            layoutParams.height = (int) dp2px(35);
        }else {
            layoutParams.width = (int) dp2px(55);
            layoutParams.height = (int) dp2px(55);
        }
        setLayoutParams(layoutParams);
        invalidate();
    }

    public void init(){
        context = getContext();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            // 分别获取到MapImgView的宽度和高度
            width = getWidth();
            height = getHeight();
//            centerX = (int)((width - dp2px(60))/2-dp2px(5));
//            centerY = (int)((height - dp2px(60))/2+dp2px(5));
            centerX = centerY = (int)dp2px(40)/2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(bitmaps!=null&&bitmaps.size()>0){
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            for (int i = 0; i < bitmaps.size(); i++) {
                Matrix matrix = new Matrix();
                //设置camera作用矩阵
                if(mFlat){
                    Camera camera = new Camera();
                    camera.rotateX(60f);
                    camera.getMatrix(matrix);
                    camera.restore();
                    //设置翻转中心点
                    matrix.preTranslate(-this.centerX, -this.centerY);
                    matrix.postTranslate(this.centerX+dp2px(2.5f), this.centerY-dp2px(5)*i);
                }else {
                    matrix.postTranslate((width - dp2px(40))/2-dp2px(7.5f)+dp2px(5)*i, (height - dp2px(40))/2+dp2px(2.5f)-dp2px(5)*i);
                }

                canvas.drawBitmap(bitmaps.get(i), matrix, paint);

            }
        }
    }

    private float dp2px(float dpValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (dpValue*scale+0.5f);
    }

    public Bitmap getBitmap(Bitmap bm,int w,int h){
        int width = bm.getWidth();
        int height = bm.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) dp2px(w) / width);
        float scaleHeight = ((float) dp2px(h) / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, true);
        Bitmap bitmap = bg2WhiteBitmap(newbmp);
        return toRoundCorner(bitmap,10);
    }

    /**
     * 对图片进行压缩的处理
     * @param resId 图片文件的路径
     * @param setWidth 指定图片的宽度
     * @param setHeight 指定图片的高度
     * @return 压缩处理的图片
     * 碰到最多的OOM(Out of Memory) 20M,系统报错
     */
    public Bitmap getBitmap(int resId , int setWidth, int setHeight){
        BitmapFactory.Options opts = new BitmapFactory.Options();
        //注意:必须要设置的属性inJustDecodeBounds,设置参数true或者false
        opts.inJustDecodeBounds = true; //不加载实际内容,只有图片的边框信息
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(),resId,opts);
//        Bitmap bm = BitmapFactory.decodeFile(pathName,opts);
        Log.i("123", "Bitmap使用opts是true的情况获取的信息-->>"+ bm);
        //获取图片的边框信息
        int orginWidth = opts.outWidth; //原始的宽度
        int orginHeight = opts.outHeight; //原始的高度
        int scaleWidth = orginWidth / setWidth; // >1才有效果    5
        int scaleHeight = orginHeight/ setHeight; // >1才有效果  4
        // 一般情况获取scale小的值作为压缩比
        int scale = scaleWidth < scaleHeight ? scaleWidth : scaleHeight;
        //注意:压缩图片的参数 (压缩比例)
        //该值>1才有压缩的效果,如果比<=1,就是原图的情况
        //实际情况是计算出来的,而不是直接写死的
        opts.inSampleSize = scale;
        //设置Bitmap的Config的设置 (也会影响到图片压缩的效果)

        //可以设置Bitmap.Config
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        //真实加载压缩后图片的内容了
        opts.inJustDecodeBounds = false; //要获取图片内容
        //BitmapFactory重新传入设置好的opts参数就可以了,这时候就可以返回按照要求压缩的图片信息了
        return BitmapFactory.decodeResource(context.getResources(),resId,opts);
    }

    /**
     *  图片转圆角
     * @param bitmap 需要转的bitmap
     * @param pixels 转圆角的弧度
     * @return 转圆角的bitmap
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels)    {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        if (bitmap != null && !bitmap.isRecycled())
        {
            bitmap.recycle();
        }
        return output;
    }

    public static Bitmap toRoundBitmap(Bitmap bitmap){
        if (bitmap == null){
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height){
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else{
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        if (bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
        return output;
    }

    /**
     *
     * 设置bitmap四周白边
     *
     * @param bitmap 原图
     * @return
     */
    public static Bitmap bg2WhiteBitmap(Bitmap bitmap)
    {
        int size = bitmap.getWidth() < bitmap.getHeight() ? bitmap.getWidth() : bitmap.getHeight();
        int num = 14;
        int sizebig = size + num;
        // 背图
        Bitmap newBitmap = Bitmap.createBitmap(sizebig, sizebig, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        // 生成白色的
        paint.setColor(Color.WHITE);
        canvas.drawBitmap(bitmap, num / 2, num / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        // 画正方形的
        canvas.drawRect(0, 0, sizebig, sizebig, paint);
        return newBitmap;
    }

}
