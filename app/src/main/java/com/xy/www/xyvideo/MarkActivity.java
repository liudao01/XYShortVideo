package com.xy.www.xyvideo;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xy.www.xylib.camera.XYCamaryView;
import com.xy.www.xylib.encodec.XYBaseMediaEncoder;
import com.xy.www.xylib.encodec.XYMediaEncodec;
import com.xy.www.xylib.util.AudioRecordUtil;
import com.xy.www.xylib.util.LogUtil;

public class MarkActivity extends AppCompatActivity {

    private boolean isStart = false;
    private Button btRecoder;
    private XYCamaryView xycamaryview;
    private AudioRecordUtil audioRecordUtil;
    private XYMediaEncodec xyMediaEncodec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);
        initView();
    }

    private void initView() {
        btRecoder = findViewById(R.id.bt_recoder);
        xycamaryview = findViewById(R.id.xycamaryview);

        audioRecordUtil = new AudioRecordUtil();

        btRecoder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStart) {
                    isStart = false;
                    stopRecoder();
                } else {
                    isStart = true;
                    startRecoder();
                }
            }
        });
    }

    private void stopRecoder() {
        if (xyMediaEncodec != null) {
            audioRecordUtil.stopRecord();
            xyMediaEncodec.stopRecord();
        }
    }

    private void startRecoder() {
        xyMediaEncodec = new XYMediaEncodec(MarkActivity.this, xycamaryview.getTextureId());
        xyMediaEncodec.initEncodec(xycamaryview.getEglContext(),
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/test_live_recoder.mp4", 1080, 1920, 44100, 2);
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
        xyMediaEncodec.startRecord();
    }


}
