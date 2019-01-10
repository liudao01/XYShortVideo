package com.xy.www.xyvideo.activity;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.xy.www.xylib.XYUtil;
import com.xy.www.xylib.listener.OnHandleListener;
import com.xy.www.xylib.util.Constant;
import com.xy.www.xylib.util.FFmpegUtil;
import com.xy.www.xylib.util.LogUtil;
import com.xy.www.xyvideo.CustomBottomSheetDialogFragment;
import com.xy.www.xyvideo.R;
import com.xy.www.xyvideo.base.BaseActivity;
import com.xy.www.xyvideo.util.ProgressDlgUtil;

import java.io.File;

/**
 * 回放的页面.
 */
public class PreViewActivity extends BaseActivity {

    private VideoView videoView;
    private SeekBar seekBar;
    private TextView tvInstruction;
    private int stopPosition;
    private Button btAddWaterMark;
    private Context mContext;
    private ProgressBar progressVideo;
    private boolean isPlay = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        mContext = this;
        initView();
    }

    private Runnable onEverySecond = new Runnable() {

        @Override
        public void run() {

            if (seekBar != null) {
                seekBar.setProgress(videoView.getCurrentPosition());
            }

            if (videoView.isPlaying()) {
                seekBar.postDelayed(onEverySecond, 1000);
            }

        }
    };


    @Override
    protected void onPause() {
        super.onPause();
        stopPosition = videoView.getCurrentPosition(); //stopPosition is an int
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.seekTo(stopPosition);
        videoView.start();
    }

    private void initView() {
        final CustomBottomSheetDialogFragment customBottomSheetDialogFragment = new CustomBottomSheetDialogFragment();


        videoView = findViewById(R.id.videoView);

        seekBar = findViewById(R.id.seekBar);
        tvInstruction = findViewById(R.id.tvInstruction);

        videoInit(Constant.fileDir);

        btAddWaterMark = findViewById(R.id.bt_addWaterMark);
        progressVideo = findViewById(R.id.progress_video);

        //ffmpeg 添加水印
        btAddWaterMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String photo = Constant.RootDir + File.separator + "img_1.jpeg";
                final String photoMarkDir = Constant.RootDir + File.separator + "test_live_ffmpeg_pic.mp4";
                String[] strings = FFmpegUtil.addWaterMark(Constant.fileDir, photo, photoMarkDir);
                XYUtil.getInstance(PreViewActivity.this).execute(strings, new OnHandleListener() {
                    @Override
                    public void onBegin() {
                        LogUtil.d("开始");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ProgressDlgUtil.showProgressDlg("添加水印中", mContext);

                            }
                        });
                    }

                    @Override
                    public void onEnd(int result) {
                        LogUtil.d("结束");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                ProgressDlgUtil.stopProgressDlg();
                                videoInit(photoMarkDir);
                            }
                        });
                    }
                });

            }
        });

    }

    private void videoInit(String filePath) {


        if (videoView != null) {
            videoView.stopPlayback();
        }
        final String videoInfo = "";
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(filePath);
            retriever.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvInstruction.setText("Video stored at path " + filePath + "\n" + videoInfo);
        videoView.setVideoURI(Uri.parse(filePath));
        videoView.start();


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                seekBar.setMax(videoView.getDuration());
                if (isPlay) {
                    seekBar.postDelayed(onEverySecond, 1000);
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                if (fromUser) {
                    // this is when actually seekbar has been seeked to a new position
                    videoView.seekTo(progress);
                }
            }
        });

    }
}
