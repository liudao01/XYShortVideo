package com.xy.www.xyvideo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.xy.www.xylib.XYUtil;
import com.xy.www.xyvideo.activity.CameraPerActivity;
import com.xy.www.xyvideo.base.BaseActivity;

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
}
