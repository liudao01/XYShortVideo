package com.xy.www.xyvideo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xy.www.xylib.XYUtil;
import com.xy.www.xyvideo.base.BaseActivity;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity {


    private Button btAddMark;
    private XYUtil xyUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }


    private void initView() {
        EasyPermissions.requestPermissions(
                MainActivity.this,
                "申请权限",
                0,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO);

        final Intent intent = new Intent(this, WaterMarkActivity.class);
        btAddMark = findViewById(R.id.bt_addMark);
        btAddMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
//                readyGo(AddWateMarkActivity.class);
            }
        });
    }


}
