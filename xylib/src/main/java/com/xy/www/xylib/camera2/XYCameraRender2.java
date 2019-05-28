package com.xy.www.xylib.camera2;

import android.content.Context;

import com.xy.www.xylib.base.BaseRender;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author liuml
 * @explain
 * @time 2018/12/7 15:54
 */
public class XYCameraRender2 extends BaseRender {

    public XYCameraRender2(Context context) {
        super(context);


    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
    }
}
