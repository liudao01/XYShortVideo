package com.xy.www.xyvideo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.xy.www.xylib.camera.XYCameraView;
import com.xy.www.xylib.encodec.XYBaseMediaEncoder;
import com.xy.www.xylib.encodec.XYMediaEncodec;
import com.xy.www.xylib.util.AudioRecordUtil;
import com.xy.www.xylib.util.LogUtil;
import com.xy.www.xyvideo.base.BaseActivity;

/**
 * 录制时添加水印
 */
public class WaterMarkActivity extends BaseActivity {

    private boolean isStart = false;
    private Button btRecoder;
    private XYCameraView xycamaryview;
    private AudioRecordUtil audioRecordUtil;
    private XYMediaEncodec xyMediaEncodec;
    private CheckBox cbAddMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);
        initView();
    }

    private void initView() {
        btRecoder = findViewById(R.id.bt_recoder);
        xycamaryview = findViewById(R.id.xycameraview);

        audioRecordUtil = new AudioRecordUtil();

        btRecoder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStart) {
                    isStart = false;
                    btRecoder.setText("录制视频");
                    stopRecoder();
                } else {
                    isStart = true;
                    startRecoder(720,1080);
                }
            }
        });
        cbAddMark = findViewById(R.id.cb_add_mark);

        cbAddMark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                xycamaryview.isAddMark = isChecked;
            }
        });
    }

    private void stopRecoder() {
        if (xyMediaEncodec != null) {
            audioRecordUtil.stopRecord();
            xyMediaEncodec.stopRecord();
        }
    }

    private void startRecoder(int width, int height) {
        boolean checked = cbAddMark.isChecked();

        xyMediaEncodec = new XYMediaEncodec(WaterMarkActivity.this, xycamaryview.getTextureId());
        xyMediaEncodec.initEncodec(xycamaryview.getEglContext(),
                Constant.fileDir, width, height, 44100, 2);
        xyMediaEncodec.setOnMediaInfoListener(new XYBaseMediaEncoder.OnMediaInfoListener() {
            @Override
            public void onMediaTime(int times) {
                LogUtil.d("time = " + times);
            }
        });

        audioRecordUtil.setOnRecordListener(new AudioRecordUtil.OnRecordListener() {
            @Override
            public void recordByte(byte[] audioData, int readSize) {

                if (xyMediaEncodec != null) {
                    xyMediaEncodec.putPCMData(audioData, readSize);
                }

            }
        });
        audioRecordUtil.startRecord();
        btRecoder.setText("正在录制中...");
        xyMediaEncodec.startRecord();
    }

    private void startRecoder() {
        startRecoder(720, 1080);
    }


}
