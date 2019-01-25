package com.xy.www.xylib.beautify;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * @author liuml
 * @explain
 * @time 2019/1/25 11:01
 */
public class BeauifyView extends GLSurfaceView {
    public BeauifyView(Context context) {
        this(context,null);
    }

    public BeauifyView(Context context, AttributeSet attrs) {
        super(context, attrs);

//        setRenderer();
    }
}
