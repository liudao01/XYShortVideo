package com.xy.www.xyvideo.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @author liuml
 * @explain
 * @time 2019/1/13 12:23
 */
public class ToastUtils {

    public static void showToast(Context context, String content){
        Toast.makeText(context.getApplicationContext(),content,Toast.LENGTH_SHORT).show();
    }
}
