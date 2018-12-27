package com.xy.www.xylib.encodec;

import android.content.Context;

/**
 * @author liuml
 * @explain
 * @time 2018/12/11 16:41
 */
public class XYMediaEncodec extends XYBaseMediaEncoder {

    private XYEncodecRender xyEncodecRender;

    public XYMediaEncodec(Context context,int textureId) {
        super(context);
        xyEncodecRender = new XYEncodecRender(context, textureId);
        setRender(xyEncodecRender);
        setmRenderMode(XYBaseMediaEncoder.RENDERMODE_CONTINUOUSLY);

    }
}
