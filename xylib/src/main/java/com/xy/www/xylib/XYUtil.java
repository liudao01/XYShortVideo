package com.xy.www.xylib;

import com.xy.www.xylib.listener.OnHandleListener;
import com.xy.www.xylib.util.LogUtil;

/**
 * @author liuml
 * @explain
 * @time 2018/12/26 10:27
 */
public class XYUtil {

    private int result = 0;

    private native static int handle(String[] commands);
    private native static int helloWorld();


    private static class staticXYUtil {
        private static XYUtil xyUtil = new XYUtil();
    }

    private XYUtil() {

    }

    public static XYUtil getInstance() {
        return staticXYUtil.xyUtil;
    }

    static {
        System.loadLibrary("native-lib");
    }


    public void execute(final String[] commands, final OnHandleListener onHandleListener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (onHandleListener != null) {
                    onHandleListener.onBegin();
                }
                //调用ffmpeg进行处理
                LogUtil.d("commands = "+commands);
//                    result = helloWorld();

                int result = handle(commands);
                if (onHandleListener != null) {
                    onHandleListener.onEnd(result);
                }
            }
        }).start();
    }


//    public int handle(String[] commands){
//        return n_handle(commands);
//    }




}
