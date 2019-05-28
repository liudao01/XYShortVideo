package com.xy.www.xyvideo.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xy.www.xylib.XYUtil;
import com.xy.www.xylib.camera.XYCameraView;
import com.xy.www.xylib.util.Constants;
import com.xy.www.xyvideo.R;
import com.xy.www.xyvideo.base.BaseActivity;

/**
 * @author liuml
 * @explain
 * @time 2019/5/23 17:18
 */
public class RecordVideoActivity extends BaseActivity implements View.OnClickListener {
    private XYCameraView xycameraview;
    private LinearLayout matchingBack;
    private Button videoRecordFinishIv;
    private FrameLayout recordBtnLl;
    private LinearLayout indexDelete;
    private TextView indexAlbum;
    private ImageView btnRecordIv;
    private TextView countDownTv;
    private ImageView meetMask;
    private ImageView videoFilter;
    private ImageView countTimeDownIv;
    private ImageView recorderStart;
    private ImageView recorderStop;
    private XYUtil xyUtil;
    private TextView recordTypeTv;
    private Chronometer chronometer;
    private long baseTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_record_video);
        initView();
    }

    private void initView() {
        xycameraview = findViewById(R.id.xycameraview);
        matchingBack = findViewById(R.id.matching_back);
        videoRecordFinishIv = findViewById(R.id.video_record_finish_iv);
        indexDelete = findViewById(R.id.index_delete);
        indexAlbum = findViewById(R.id.index_album);
        countDownTv = findViewById(R.id.count_down_tv);
        meetMask = findViewById(R.id.meet_mask);
        videoFilter = findViewById(R.id.video_filter);
        countTimeDownIv = findViewById(R.id.count_time_down_iv);
        recorderStart = findViewById(R.id.recorder_start);
        recorderStop = findViewById(R.id.recorder_stop);
        recordTypeTv = findViewById(R.id.record_type_tv);
        chronometer = findViewById(R.id.chronometer);

        matchingBack.setOnClickListener(this);
        videoRecordFinishIv.setOnClickListener(this);
        indexDelete.setOnClickListener(this);
        recorderStart.setOnClickListener(this);
        recorderStop.setOnClickListener(this);
        recordTypeTv.setOnClickListener(this);
        xyUtil = XYUtil.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recorder_start:
                recorderStop.setVisibility(View.VISIBLE);
                recorderStart.setVisibility(View.GONE);
                onRecordStart();

                // 重置其他
                if (xyUtil.isRecording) {
//                    btRecoder.setText("录制视频");
                    xyUtil.stopRecoder();
                } else {
                    xyUtil.startRecoder(this, xycameraview,Constants.fileDir);
//                    btRecoder.setText("正在录制中...");
                }
                break;

            case R.id.recorder_stop:
                onRecordPause();
                chronometer.stop();
                recorderStop.setVisibility(View.GONE);
                recorderStart.setVisibility(View.VISIBLE);
                break;
            case R.id.record_type_tv:
                //分段
                showRecordVideoTypeDialog();

                break;
            case R.id.video_record_finish_iv:
                readyGo(PlayBackActivity.class);
                break;
            case R.id.matching_back:
                finish();
                break;
        }
    }

    /**
     * 录制方式
     */
    private void showRecordVideoTypeDialog() {

    }


    public void onRecordStart() {
        chronometer.setBase(SystemClock.elapsedRealtime() - baseTime);// 跳过已经记录了的时间，起到继续计时的作用
        chronometer.start();
    }

    public void onRecordPause() {
        chronometer.stop();
        baseTime = SystemClock.elapsedRealtime() - chronometer.getBase();// 保存这次记录了的时间
    }

    public void onRecordStop() {
        baseTime = 0;
        chronometer.setBase(SystemClock.elapsedRealtime());
    }
}
