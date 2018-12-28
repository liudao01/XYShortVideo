package com.xy.www.xylib.shortvideo.play;

import android.content.Context;
import android.util.AttributeSet;

import com.xy.www.xylib.egl.XYEGLSurfaceView;
import com.xy.www.xylib.util.LogUtil;

/**
 * @author liuml
 * @explain
 * @time 2018/12/28 13:54
 */
public class XYPlayVideoView extends XYEGLSurfaceView {


    private XYPlayVideoRender xyPlayVideoRender;
    private int fbotextureid;

    public XYPlayVideoView(Context context) {
        this(context, null);
    }

    public XYPlayVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XYPlayVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        xyPlayVideoRender = new XYPlayVideoRender(context);
        setRender(xyPlayVideoRender);
        setRenderMode(XYEGLSurfaceView.RENDERMODE_WHEN_DIRTY);
        xyPlayVideoRender.setOnRenderCreateListener(new XYPlayVideoRender.OnRenderCreateListener() {
            @Override
            public void onCreate(int textureId) {

                fbotextureid = textureId;
            }

        });
    }

    public void setCurrentImg(int imgsrc) {
        if (xyPlayVideoRender != null) {
            xyPlayVideoRender.setCurrentImgSrc(imgsrc);
            requestRender();//手动刷新 调用一次
            LogUtil.d("手动刷新 "+imgsrc);
        }
    }


    public int getFbotextureid() {
        return fbotextureid;
    }
}