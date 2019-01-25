package com.xy.www.xyvideo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoClipActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private VideoView videoViewClip;
    private RecyclerView recylerview;
    private Button btFileChooser;
    private TextView tvFileDir;
    private String url;
    private int alltime;
    private Button btCutVideo;
    private SeekBar seekBarCutVideo;
    private TextView tvSeekBarProgress;
    private int currentProgress = 0;
    private Button btVideoToImage;
    private Button btRotateVideo;

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
        btCutVideo = findViewById(R.id.bt_cut_video);

        seekBarCutVideo = findViewById(R.id.seekBar_cut_video);
        tvSeekBarProgress = findViewById(R.id.tv_seekBar_progress);
        btVideoToImage = findViewById(R.id.bt_video_to_image);
        btRotateVideo = findViewById(R.id.bt_rotate_video);
        btRotateVideo.setOnClickListener(this);
        videoViewClip.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) { //监听视频准备好
                //这里获取到的时间是ms为单位的，所以要转换成秒的话就要除以1000
                alltime = videoViewClip.getDuration() / 1000;
                boolean orExistsDir = FileUtils.createOrExistsDir(Constants.shortVideo);
                LogUtil.d("orExistsDir = " + orExistsDir);
                seekBarCutVideo.setMax(alltime);


            }
        });

        seekBarCutVideo.setOnSeekBarChangeListener(this);
        btCutVideo.setOnClickListener(this);

        btVideoToImage.setOnClickListener(this);
    }

    /**
     * 处理完成
     */
    private void handlePic() {
        //Constants.shortVideo
        //获取文件夹内的图片
        List<String> imagePathFromSD = getImagePathFromSD(Constants.shortVideo);
//        GlideUtils.loadImage(this, imagePathFromSD.get(0), image);
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
//        videoViewClip.start();
    }


    /**
     * 从sd卡获取图片资源
     *
     * @return
     */
    private List<String> getImagePathFromSD(String filePath) {
        // 图片列表
        List<String> imagePathList = new ArrayList<String>();
        // 得到sd卡内image文件夹的路径   File.separator(/)

        // 得到该路径文件夹下所有的文件
        File fileAll = new File(filePath);
        File[] files = fileAll.listFiles();
        // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (checkIsImageFile(file.getPath())) {
                imagePathList.add(file.getPath());
            }
        }
        // 返回得到的图片列表
        return imagePathList;
    }

    /**
     * 检查扩展名，得到图片格式的文件
     *
     * @param fName 文件名
     * @return
     */
    @SuppressLint("DefaultLocale")
    private boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")
                || FileEnd.equals("jpeg") || FileEnd.equals("bmp")) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_fileChooser:
                fileChooser();
                break;
            case R.id.bt_cut_video:
                cutVideo();
                break;
            case R.id.bt_video_to_image:
                videoToImage();
                break;
            case R.id.bt_rotate_video:
                rotateVideo();
                break;
        }
    }

    /**
     * 视频抽帧
     */
    private void videoToImage() {

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
            public void onEnd(final int result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoadingDialog();
                        if (Constants.handleSuccess == result) {

                            //视频转图片
                            handlePic();
                        }

                    }
                });

            }
        });
    }
    private void rotateVideo() {

        XYUtil.getInstance().rotateVideo(url,Constants.rotateFile, 90,new OnHandleListener() {
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
            public void onEnd(final int result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoadingDialog();
                        if (Constants.handleSuccess == result) {

                            LogUtil.d("旋转成功");
                        }

                    }
                });

            }
        });
    }

    /**
     * 裁剪文件
     */
    private void cutVideo() {
        LogUtil.d("currentProgress = " + currentProgress);
        /**
         *  @param srcFile    源文件
         *      * @param startTime  剪切的开始时间(单位为秒)
         *      * @param duration   剪切时长(单位为秒)
         *      * @param targetFile 目标文件
         */
        XYUtil.getInstance().cutVideo(url, 0, currentProgress, Constants.rootDir + File.separator + "output.mp4", new OnHandleListener() {
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
            public void onEnd(final int result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoadingDialog();
                        if (Constants.handleSuccess == result) {
                            LogUtil.d("成功");
                        }

                    }
                });

            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //正在拖动
        tvSeekBarProgress.setText("裁剪秒数 : " + progress);


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //开始拖动
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //停止拖动
        // 当进度条停止修改的时候触发
        // 取得当前进度条的刻度
        int progress = seekBar.getProgress();
        if (videoViewClip != null) {
            // 设置当前播放的位置
            videoViewClip.seekTo(progress * 100);
            currentProgress = progress;
        }
    }
}
