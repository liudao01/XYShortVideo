package com.xy.www.xylib.shortvideo;

import android.graphics.Bitmap;

/**
 * @author liuml
 * @explain
 * @time 2018/12/27 10:34
 */
public class XYWatermarkSetting {
    private int mResourceId;
    private Bitmap mBitmap;
    private float mX;
    private float mY;
    private float mWidth;
    private float mHeight;
    private int mAlpha = 255;

    public XYWatermarkSetting() {
    }

    public int getResourceId() {
        return this.mResourceId;
    }

    public void setResourceId(int resourceId) {
        this.mResourceId = resourceId;
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    public float getX() {
        return this.mX;
    }

    public float getY() {
        return this.mY;
    }

    public void setPosition(float x, float y) {
        this.mX = x;
        this.mY = y;
    }

    public float getWidth() {
        return this.mWidth;
    }

    public float getHeight() {
        return this.mHeight;
    }

    public void setSize(float width, float height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    public int getAlpha() {
        return this.mAlpha;
    }

    public void setAlpha(int alpha) {
        if (alpha >= 0 && alpha <= 255) {
            this.mAlpha = alpha;
        } else {
            throw new IllegalArgumentException("alpha value should be [0...255]:" + alpha);
        }
    }
}
