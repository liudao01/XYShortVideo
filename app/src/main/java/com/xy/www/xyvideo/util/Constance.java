package com.xy.www.xyvideo.util;

import android.Manifest;

/**
 * @author liuml
 * @explain
 * @time 2019/1/13 12:23
 */
public final class Constance {
    /**
     * 写入权限的请求code,提示语，和权限码
     */
    public final static  int WRITE_PERMISSION_CODE=110;
    public final static  String WRITE_PERMISSION_TIP ="为了正常使用，请允许权限!";
    public final  static String[] CAMERA ={Manifest.permission.CAMERA};
    public final  static String[] STORAGE ={Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /**
     * 相机，图库的请求code
     */
    public final static int PICTURE_CODE=10;
    public final static int GALLERY_CODE=11;
}