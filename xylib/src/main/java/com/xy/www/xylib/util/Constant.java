package com.xy.www.xylib.util;

import android.os.Environment;

/**
 * @author liuml
 * @explain
 * @time 2018/12/28 10:40
 */
public class Constant {

    public static final String RootDir = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String fileDir = RootDir + "/test_live_recoder.mp4";
    public static final String breakPointfile1 = RootDir + "/test_live_recoder1.mp4";
    public static final String breakPointfile2 = RootDir + "/test_live_recoder2.mp4";
    public static final int ScreenWidth = 720;
    public static final int ScreenHeight= 1080;
    public static final String musicfileDir = RootDir + "/xjw.mp3";//加入背景的音乐
    public static  String addMarkText= "Liuml 刘茂林的水印";
    public static final String testDir = RootDir + "/药神MP4.mp4";
}
