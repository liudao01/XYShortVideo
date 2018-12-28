package com.xy.www.xyvideo.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


interface IActivity {
//    void initView();
//
//    void initListener();
//
//    int getLayoutId();
}

/**
 * @author liuml
 * @explain
 * @time 2018/12/28 09:51
 */
public abstract class BaseActivity extends AppCompatActivity implements IActivity {


    /**
     * 是否禁止旋转屏幕
     */
    protected boolean isAllowScreenRoate = false;
    /**
     * 是否允许全屏
     */
    protected boolean mAllowFullScreen = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 一些系统配置
        //方法拆分
//        setContentView(getLayoutId());
//        initView();
//        initListener();
        //各种初始化：方法初始化、通用配置、
        // EventBus初始化
        //
    }

    @Override
    protected void onDestroy() {
        // 一些销毁  比如  EventBus销毁等等
        super.onDestroy();
    }
//....一些便于使用的共用方法

    /**
     * startActivity
     *
     * @param clazz
     */
    protected void readyGo(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

}
