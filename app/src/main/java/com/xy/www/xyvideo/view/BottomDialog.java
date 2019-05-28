package com.xy.www.xyvideo.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.xy.www.xyvideo.R;


/**
 * @author liuml
 * @explain
 * @time 2019/5/28 14:07
 */
public abstract class BottomDialog extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置style
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomDialog);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //去除标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);


        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; //底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        return createView(inflater, container);
    }


    //重写此方法，设置布局文件
    protected abstract View createView(LayoutInflater inflater, ViewGroup container);


}



