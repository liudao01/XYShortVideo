package com.xy.www.xyvideo.activity;

import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xy.www.xylib.camera.XYCamera;
import com.xy.www.xyvideo.R;

/**
 * 相机滤镜
 */
public class CameraFilterActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llBottom;
    private Button btSelectFilter;
    private Button btRecord;
    //摄像头
    private int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

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
        XYCamera xyCamera = new XYCamera(getApplicationContext());
        xyCamera.initFilterCamera(Camera.CameraInfo.CAMERA_FACING_BACK);

        int angle = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
//        gpuImageView.setRatio(Ra);
        int result = 0;
        if (cameraId != Camera.CameraInfo.CAMERA_FACING_BACK) {
            result = (90 + angle) % 360;
        } else { // back-facing
            result = (90 - angle) % 360;
        }

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
