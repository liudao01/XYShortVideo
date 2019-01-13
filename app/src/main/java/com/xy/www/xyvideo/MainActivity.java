package com.xy.www.xyvideo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.xy.www.xylib.XYUtil;
import com.xy.www.xyvideo.activity.CameraPerActivity;
import com.xy.www.xyvideo.base.BaseActivity;
import com.xy.www.xyvideo.util.PermissionsUtils;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    private Button btAddMark;
    private XYUtil xyUtil;
    private Context mContext;
    private ProgressBar progressVideo;
    private Button btAddMusic;
    private Button btBreakpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        //两个日历权限和一个数据读写权限
        String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
//        PermissionsUtils.showSystemSetting = false;//是否支持显示系统设置权限设置窗口跳转
        //这里的this不是上下文，是Activity对象！
        PermissionsUtils.getInstance().chekPermissions(this, permissions, permissionsResult);

        initView();
    }


    private void initView() {

        btAddMark = findViewById(R.id.bt_addMark);
        btAddMark.setOnClickListener(this);


        progressVideo = findViewById(R.id.progress_video);
        btAddMusic = findViewById(R.id.bt_addMusic);
        btBreakpoint = findViewById(R.id.bt_breakpoint);
        btBreakpoint.setOnClickListener(this);
        btAddMusic.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        final Intent intent = new Intent(this, CameraPerActivity.class);
        switch (v.getId()) {
            case R.id.bt_addMark:
                intent.putExtra("key", 1);
                break;
            case R.id.bt_addMusic:
                intent.putExtra("key", 2);
                break;
            case R.id.bt_breakpoint:
                intent.putExtra("key", 3);
                break;
        }
        startActivity(intent);
    }


    //创建监听权限的接口对象
    PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
        @Override
        public void passPermissons() {
            Toast.makeText(MainActivity.this, "权限通过，可以做其他事情!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void forbitPermissons() {
//            finish();
            Toast.makeText(MainActivity.this, "权限不通过!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //就多一个参数this
        PermissionsUtils.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }


}
