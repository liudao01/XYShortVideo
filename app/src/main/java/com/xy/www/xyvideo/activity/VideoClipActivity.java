package com.xy.www.xyvideo.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.xy.www.xylib.XYUtil;
import com.xy.www.xylib.listener.OnHandleListener;
import com.xy.www.xylib.util.Constants;
import com.xy.www.xylib.util.FileUtils;
import com.xy.www.xylib.util.LogUtil;
import com.xy.www.xyvideo.R;
import com.xy.www.xyvideo.base.BaseActivity;
import com.xy.www.xyvideo.util.FileChooserUtil;

public class VideoClipActivity extends BaseActivity implements View.OnClickListener {

    private VideoView videoViewClip;
    private RecyclerView recylerview;
    private Button btFileChooser;
    private TextView tvFileDir;
    private String url;
    private int alltime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_clip);
        initView();
    }

    private void initView() {
        videoViewClip = findViewById(R.id.videoView_clip);
        recylerview = findViewById(R.id.recylerview);

        btFileChooser = findViewById(R.id.bt_fileChooser);
        btFileChooser.setOnClickListener(this);
        tvFileDir = findViewById(R.id.tv_file_dir);


        videoViewClip.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) { //监听视频准备好
                //这里获取到的时间是ms为单位的，所以要转换成秒的话就要除以1000
                alltime = videoViewClip.getDuration() / 1000;
                boolean orExistsDir = FileUtils.createOrExistsDir(Constants.shortVideo);
                LogUtil.d("orExistsDir = " + orExistsDir);
                XYUtil.getInstance().videoToImage(url, 0, alltime, 1, Constants.shortVideo, new OnHandleListener() {
                    @Override
                    public void onBegin() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showLoadingDialog();
                            }
                        });

                    }

                    @Override
                    public void onEnd(int result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissLoadingDialog();

                            }
                        });

                    }
                });

            }
        });

    }

    public void fileChooser() {
        FileChooserUtil.getInstanse(this).fileChooser();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        url = FileChooserUtil.getInstanse(this).onActivityResult(requestCode, resultCode, data);
        tvFileDir.setText(url);
        videoViewClip.setVideoURI(Uri.parse(url));
    }

    private void videoClip() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_fileChooser:
                fileChooser();
                break;
        }
    }
}
