package com.xy.www.xyvideo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.xy.www.xylib.XYUtil;
import com.xy.www.xylib.util.Constants;
import com.xy.www.xyvideo.activity.CameraPerActivity;
import com.xy.www.xyvideo.base.BaseActivity;
import com.xy.www.xyvideo.util.PermissionsUtils;
import com.xy.www.xyvideo.util.ToastUtils;

import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    private Button btAddMark;
    private XYUtil xyUtil;
    private Context mContext;
    private Button btAddMusic;
    private Button btBreakpoint;
    private Button btVideoConvert;
    private Button btVideoCut;
    private int requestcode_from_activity;

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


        btAddMusic = findViewById(R.id.bt_addMusic);
        btBreakpoint = findViewById(R.id.bt_breakpoint);
        btBreakpoint.setOnClickListener(this);
        btAddMusic.setOnClickListener(this);
        btVideoConvert = findViewById(R.id.bt_video_convert);
        btVideoCut = findViewById(R.id.bt_video_cut);
        btVideoConvert.setOnClickListener(this);
        btVideoCut.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.bt_addMark://添加水印录制
                intent = new Intent(this, CameraPerActivity.class);
                intent.putExtra("key", 1);
                break;
            case R.id.bt_addMusic://添加背景音乐录制
                intent = new Intent(this, CameraPerActivity.class);
                intent.putExtra("key", 2);
                break;
            case R.id.bt_breakpoint://断点录制
                intent = new Intent(this, CameraPerActivity.class);
                intent.putExtra("key", 3);
                break;
            case R.id.bt_video_convert:
                break;
            case R.id.bt_video_cut:
                fileChooser();
                break;
        }
        if (intent != null) {

            startActivity(intent);
        }
    }

    public void fileChooser() {
        requestcode_from_activity = 1000;
        new LFilePicker()
                .withActivity(MainActivity.this)
                .withRequestCode(requestcode_from_activity)
                .withStartPath(Constants.rootDir)//指定初始显示路径
                .withIsGreater(true)//过滤文件大小 小于指定大小的文件
                .withFileSize(500)//指定文件大小为500K
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == requestcode_from_activity) {
                List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
                Toast.makeText(getApplicationContext(), "选中了" + list.size() + "个文件", Toast.LENGTH_SHORT).show();
                ToastUtils.showToast(this,list.get(0));
            }
        }


    }
//    }

    //创建监听权限的接口对象
    PermissionsUtils.IPermissionsResult permissionsResult = new PermissionsUtils.IPermissionsResult() {
        @Override
        public void passPermissons() {
            Toast.makeText(MainActivity.this, "权限通过", Toast.LENGTH_SHORT).show();
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
