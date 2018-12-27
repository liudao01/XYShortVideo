package com.xy.www.xylib.shortvideo.recoder;

import java.io.File;

/**
 * @author liuml
 * @explain
 * @time 2018/12/27 10:52
 */
public class XYRecordSetting {
    public static final String TAG = "PLRecordSetting";
    private static final String MAX_RECORD_DURATION = "maxRecordDuration";
    private static final String VIDEO_CACHE_DIR = "videoCacheDir";
    private static final String RECORD_FILE_PATH = "recordFilePath";
    private static final String DISPLAY_MODE = "displayMode";
    private long mMaxRecordDuration = 10000L;
    private File mVideoCacheDir;
    private String mRecordFilepath;
    private XYDisplayMode mDisplayMode;
    private boolean mIsRecordSpeedVariable;




}
