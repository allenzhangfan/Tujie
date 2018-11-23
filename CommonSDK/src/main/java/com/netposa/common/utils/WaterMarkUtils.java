package com.netposa.common.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.View;

/**
 * 作者：安兴亚
 * 创建日期：2017/12/14
 * 邮箱：anxingya@lingdanet.com
 * 描述：TODO
 */

public class WaterMarkUtils {

    private static final int DEFAULT_DEGREES = -45;
    private static final int DEFAULT_COLUMN = 3;
    private static final int DEFAULT_ROW = 5;

    //然后View和其内部的子View都具有了实际大小，也就是完成了布局，相当与添加到了界面上。接着就可以创建位图并在上面绘制了：
    public static void layoutView(View v, int width, int height) {
        // 整个View的大小 参数是左上角 和右下角的坐标
        v.layout(0, 0, width, height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        /** 当然，measure完后，并不会实际改变View的尺寸，需要调用View.layout方法去进行布局。
         * 按示例调用layout函数后，View的大小将会变成你想要设置成的大小。
         */
        v.measure(measuredWidth, measuredHeight);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
    }

    public static Bitmap loadBitmapFromView(View v) {
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

    /**
     * 获取竖屏的重复Bitmap
     *
     * @param hCount
     * @param src
     * @return
     */
    public static Bitmap createRepeater(int hCount, Bitmap src) {
        float width = src.getWidth();
        float height = src.getHeight();
        float vCount = Math.round(SizeUtils.getScreenHeight() / height);

        Bitmap bitmap = Bitmap.createBitmap(SizeUtils.getScreenWidth(), SizeUtils.getScreenHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        for (int idx = 0; idx < hCount; ++idx) {
            for (int idy = 0; idy < vCount; ++idy) {
                canvas.drawBitmap(src, idx * width, idy * height, new Paint());
            }

        }

        return bitmap;
    }

    /**
     * 获取竖屏的重复Bitmap
     *
     * @param src
     * @return
     */
    public static Bitmap createRepeater(Bitmap src) {
        Bitmap bitmap = Bitmap.createBitmap(SizeUtils.getScreenWidth(), SizeUtils.getScreenHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setShader(new BitmapShader(src, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));

        canvas.drawRect(0, 0, SizeUtils.getScreenWidth(), SizeUtils.getScreenHeight(), paint);

        return bitmap;
    }

    /**
     * 获取横屏的重复Bitmap
     *
     * @param hCount
     * @param src
     * @return
     */
    public static Bitmap createLandscapeRepeater(int hCount, Bitmap src) {
        float width = src.getWidth();
        float height = src.getHeight();
        int vCount = Math.round(SizeUtils.getScreenHeight() / height);

        Bitmap bitmap = Bitmap.createBitmap(SizeUtils.getScreenWidth(), SizeUtils.getScreenHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        for (int idx = 0; idx < vCount; ++idx) {
            for (int idy = 0; idy < hCount; ++idy) {
                canvas.drawBitmap(src, idy * width, idx * height, new Paint());
            }

        }

        return bitmap;
    }

    /**
     * 获取横屏的重复Bitmap
     *
     * @param hCount
     * @param src
     * @param bitmapWidth
     * @param bitmapHeight
     * @return
     */
    public static Bitmap createLandscapeRepeater(int hCount, Bitmap src, int bitmapWidth, int bitmapHeight) {
        float width = src.getWidth();
        float height = src.getHeight();
        int vCount = Math.round(bitmapHeight / height);

        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        for (int idx = 0; idx < vCount; ++idx) {
            for (int idy = 0; idy < hCount; ++idy) {
                canvas.drawBitmap(src, idy * width, idx * height, new Paint());
            }

        }

        return bitmap;
    }

    /**
     * 获取横屏的重复Bitmap
     *
     * @param src
     * @param bitmapWidth
     * @param bitmapHeight
     * @return
     */
    public static Bitmap createLandscapeRepeater(Bitmap src, int bitmapWidth, int bitmapHeight) {
        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setShader(new BitmapShader(src, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));

        canvas.drawRect(0, 0, bitmapWidth, bitmapHeight, paint);

        return bitmap;
    }

    /**
     * 获取竖屏全屏水印的Bitmap
     *
     * @param waterText 水印文字
     * @return 全屏水印的Bitmap
     */
    public static Bitmap getMarkerBitmap(String waterText) {
        int width = SizeUtils.getScreenWidth() / DEFAULT_COLUMN;
        Bitmap bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        TextPaint paint = new TextPaint();
        paint.setFakeBoldText(true);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        //让画出的图形是实心的
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#22FFFFFF"));  // 设置字体中间实体部分的颜色
        //设置字体大小
        paint.setTextSize(SizeUtils.sp2px(18));

        canvas.rotate(DEFAULT_DEGREES, width / 2, width / 2);
        float textLength = getTextLength(paint, waterText);
        float startPos;
        if (textLength < width) {
            startPos = (width - textLength) / 2;
        } else {
            startPos = 0;
        }
        canvas.drawText(waterText, startPos, width / 2, paint);
        //让画出的图形是空心的
        paint.setStyle(Paint.Style.STROKE);
        //设置画出的线的 粗细程度
        paint.setStrokeWidth(SizeUtils.sp2px(1.1f));
        paint.setColor(Color.parseColor("#1A000000")); // 设置字体镂空部分的颜色
        canvas.drawText(waterText, startPos, width / 2, paint);
        // return WaterMarkUtils.createRepeater(DEFAULT_COLUMN, bitmap);
        return WaterMarkUtils.createRepeater(bitmap);
    }

    /**
     * 获取横屏全屏水印的Bitmap
     *
     * @param waterText 水印文字
     * @return 全屏水印的Bitmap
     */
    public static Bitmap getLandscapeMarkerBitmap(String waterText) {
        int width = SizeUtils.getScreenWidth() / DEFAULT_ROW;
        Bitmap bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        TextPaint paint = new TextPaint();
        paint.setFakeBoldText(true);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        //让画出的图形是实心的
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#22FFFFFF"));  // 设置字体中间实体部分的颜色
        //设置字体大小
        paint.setTextSize(SizeUtils.sp2px(18));

        canvas.rotate(DEFAULT_DEGREES, width / 2, width / 2);
        float textLength = getTextLength(paint, waterText);
        float startPos;
        if (textLength < width) {
            startPos = (width - textLength) / 2;
        } else {
            startPos = 0;
        }
        canvas.drawText(waterText, startPos, width / 2, paint);
        //让画出的图形是空心的
        paint.setStyle(Paint.Style.STROKE);
        //设置画出的线的 粗细程度
        paint.setStrokeWidth(SizeUtils.sp2px(1.1f));
        paint.setColor(Color.parseColor("#1A000000")); // 设置字体镂空部分的颜色
        canvas.drawText(waterText, startPos, width / 2, paint);
        // return WaterMarkUtils.createLandscapeRepeater(DEFAULT_ROW, bitmap);
        return WaterMarkUtils.createRepeater(bitmap);
    }

    /**
     * 获取横屏全屏水印的Bitmap
     *
     * @param waterText 水印文字
     * @return 全屏水印的Bitmap
     */
    public static Bitmap getLandscapeMarkerBitmap(String waterText, int bitmapWidth, int bitmapHeight) {
        int width = bitmapWidth / DEFAULT_ROW;
        Bitmap bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        TextPaint paint = new TextPaint();
        paint.setFakeBoldText(true);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        //让画出的图形是实心的
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#22FFFFFF"));  // 设置字体中间实体部分的颜色
        //设置字体大小
        paint.setTextSize(SizeUtils.sp2px(18));

        canvas.rotate(DEFAULT_DEGREES, width / 2, width / 2);
        float textLength = getTextLength(paint, waterText);
        float startPos;
        if (textLength < width) {
            startPos = (width - textLength) / 2;
        } else {
            startPos = 0;
        }
        canvas.drawText(waterText, startPos, width / 2, paint);
        //让画出的图形是空心的
        paint.setStyle(Paint.Style.STROKE);
        //设置画出的线的 粗细程度
        paint.setStrokeWidth(SizeUtils.sp2px(1.1f));
        paint.setColor(Color.parseColor("#1A000000")); // 设置字体镂空部分的颜色
        canvas.drawText(waterText, startPos, width / 2, paint);
        // return WaterMarkUtils.createLandscapeRepeater(DEFAULT_ROW, bitmap, bitmapWidth, bitmapHeight);
        return WaterMarkUtils.createLandscapeRepeater(bitmap, bitmapWidth, bitmapHeight);


    }


    /**
     * 获取一串文字的显示长度
     *
     * @param paint 绘制text的画笔
     * @param text  需要测量长度的文字
     * @return
     */
    public static float getTextLength(Paint paint, String text) {
        return paint.measureText(text);
    }
}
