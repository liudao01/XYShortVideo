package com.xy.www.xyvideo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.xy.www.xylib.XYUtil;
import com.xy.www.xyvideo.activity.CameraPerActivity;
import com.xy.www.xyvideo.base.BaseActivity;
import com.xy.www.xyvideo.util.Constance;
import com.xy.www.xyvideo.util.PermissionManager;
import com.xy.www.xyvideo.util.ToastUtils;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements View.OnClickListener ,EasyPermissions.PermissionCallbacks{


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

    private void setBtAddMark() {

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
    /**
     * 检查读写权限权限
     */
    private void checkWritePermission() {
        boolean result = PermissionManager.checkPermission(this, Constance.PERMS_WRITE);
        if (!result) {
            PermissionManager.requestPermission(this, Constance.WRITE_PERMISSION_TIP, Constance.WRITE_PERMISSION_CODE, Constance.PERMS_WRITE);
        }
    }
    /**
     * 重写onRequestPermissionsResult，用于接受请求结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //将请求结果传递EasyPermission库处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    /**
     * 请求权限成功
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        ToastUtils.showToast(getApplicationContext(), "用户授权成功");

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        ToastUtils.showToast(getApplicationContext(), "用户授权失败功");
    }

}
