package com.xy.www.xyvideo.activity;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xy.www.xylib.camera.XYCamera;
import com.xy.www.xyvideo.R;

import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * 相机滤镜
 */
public class CameraFilterActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llBottom;
    private Button btSelectFilter;
    private Button btRecord;
    private GPUImageView gpuImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        initView();
    }

    private void initView() {
        llBottom = findViewById(R.id.ll_bottom);
        btSelectFilter = findViewById(R.id.bt_select_filter);
        btRecord = findViewById(R.id.bt_record);
        btSelectFilter.setOnClickListener(this);
        btRecord.setOnClickListener(this);
        gpuImageView = findViewById(R.id.gpuImageView);
        XYCamera xyCamera = new XYCamera(getApplicationContext());
        xyCamera.initCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
        gpuImageView.setUpCamera(xyCamera.getCamera());
        gpuImageView.setRenderMode(GPUImageView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_record://录制

                break;
            case R.id.bt_select_filter://切换滤镜

                break;
        }
    }
}
