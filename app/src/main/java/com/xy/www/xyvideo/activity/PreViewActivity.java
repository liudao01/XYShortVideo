package com.xy.www.xyvideo.activity;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.xy.www.xyvideo.Constant;
import com.xy.www.xyvideo.CustomBottomSheetDialogFragment;
import com.xy.www.xyvideo.R;
import com.xy.www.xyvideo.base.BaseActivity;
import com.xy.www.xyvideo.util.ProgressDlgUtil;

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

        String filePath = Constant.fileDir;

        String videoInfo = "";
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(filePath);
            int bitrate = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
            retriever.release();
//            MediaExtractor extractor = new MediaExtractor();
//            extractor.setDataSource(filePath);
//            MediaFormat format = extractor.getTrackFormat(VideoUtil.selectTrack(extractor, false));
//            int frameRate = format.containsKey(MediaFormat.KEY_FRAME_RATE) ? format.getInteger(MediaFormat.KEY_FRAME_RATE) : -1;
//            int width = format.getInteger(MediaFormat.KEY_WIDTH);
//            int height = format.getInteger(MediaFormat.KEY_HEIGHT);
//            int rotation = format.containsKey(MediaFormat.KEY_ROTATION) ? format.getInteger(MediaFormat.KEY_ROTATION) : -1;
//            long duration = format.containsKey(MediaFormat.KEY_DURATION) ? format.getLong(MediaFormat.KEY_DURATION) : -1;
//            videoInfo = String.format("size:%dX%d,framerate:%d,rotation:%d,bitrate:%d,duration:%.1fs", width, height, frameRate, rotation, bitrate,
//                    duration / 1000f / 1000f);
//            extractor.release();
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
                seekBar.postDelayed(onEverySecond, 1000);
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

        btAddWaterMark = findViewById(R.id.bt_addWaterMark);
        btAddWaterMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DialogUtils.getInstance().showLoadingProgress(mContext);
                ProgressDlgUtil.showProgressDlg("加载中",mContext);
            }
        });
    }
}
