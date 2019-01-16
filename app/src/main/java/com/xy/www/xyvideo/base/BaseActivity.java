package com.xy.www.xyvideo.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.xy.www.xyvideo.util.ProgressDlgUtil;


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

    protected String TAG = getClass().getSimpleName();
    protected Context context;

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
        context=this;
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
    protected void readyGo(Class<?> clazz,String type) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    protected String getFrome() {
        return getIntent().getStringExtra("type");
    }

    private AlertDialog alertDialog;

    public void showLoadingDialog() {
//        alertDialog = new AlertDialog.Builder(this).create();
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
//        alertDialog.setCancelable(false);
//        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK)
//                    return true;
//                return false;
//            }
//        });
//        alertDialog.show();
//        alertDialog.setContentView(R.layout.dialog_view);
//        alertDialog.setCanceledOnTouchOutside(false);
        ProgressDlgUtil.showProgressDlg("正在处理中...", this);
    }

    public void dismissLoadingDialog() {
//        if (null != alertDialog && alertDialog.isShowing()) {
//            alertDialog.dismiss();
//        }
        ProgressDlgUtil.stopProgressDlg();
    }


}
