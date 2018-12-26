package com.xy.www.xylib;

/**
 * @author liuml
 * @explain
 * @time 2018/12/26 10:27
 */
public class XYUtil {

    private static class staticXYUtil{
        private static XYUtil xyUtil = new XYUtil();
    }

    private  XYUtil(){

    }

    public static XYUtil getInstance(){
        return staticXYUtil.xyUtil;
    }

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }



    public String helloword(){
        return n_helloWord();
    }
    private native void n_addMark();

    private native String n_helloWord();

}
