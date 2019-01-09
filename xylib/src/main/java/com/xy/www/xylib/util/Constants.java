package com.xy.www.xylib.util;

import android.os.Environment;

import java.io.File;

/**
 * @author liuml
 * @explain
 * @time 2019/1/9 14:42
 */
public class Constants {
    private static final String PATH = Environment.getExternalStorageDirectory().getPath();
    private static final String appendVideo = PATH + File.separator + "test.mp4";
}
