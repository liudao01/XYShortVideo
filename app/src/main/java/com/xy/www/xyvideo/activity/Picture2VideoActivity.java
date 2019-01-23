package com.xy.www.xyvideo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.xy.www.xylib.XYUtil;
import com.xy.www.xylib.listener.OnHandleListener;
import com.xy.www.xylib.util.FFmpegUtil;
import com.xy.www.xylib.util.LogUtil;
import com.xy.www.xyvideo.R;
import com.xy.www.xyvideo.base.BaseActivity;
import com.xy.www.xyvideo.util.ToastUtils;

import java.io.File;
import java.util.List;


/**
 * @author AndSync
 * @date 2019/1/16
 * Copyright © 2014-2019 AndSync All rights reserved.
 */
public class Picture2VideoActivity extends BaseActivity implements View.OnClickListener {
    private static final String PATH = Environment.getExternalStorageDirectory().getPath();
    private static final int MSG_BEGIN = 101;
    private static final int MSG_FINISH = 102;
    private Button btSingle;
    private Button btMore;
    private Button btFolder;

    private int handleType;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_BEGIN:
                    Toast.makeText(context, "开始处理", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_FINISH:
                    Toast.makeText(context, "处理结束", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture2video);
        btSingle = findViewById(R.id.bt_single);
        btMore = findViewById(R.id.bt_more);
        btFolder = findViewById(R.id.bt_folder);
        btSingle.setOnClickListener(this);
        btMore.setOnClickListener(this);
        btFolder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_single:
                PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofImage())
                    .maxSelectNum(1)
                    .forResult(PictureConfig.CHOOSE_REQUEST);
                handleType = 0;
                break;
            case R.id.bt_more:
                ToastUtils.showToast(context, "开发中...");
                handleType = 1;
                break;
            case R.id.bt_folder:
                ToastUtils.showToast(context, "开发中...");
                handleType = 2;
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (handleType == 0) {
                        LocalMedia media = selectList.get(0);
                        LogUtil.d(TAG, media.getPath() + "");
                        LogUtil.d(TAG, media.getDuration() + "");
                        LogUtil.d(TAG, media.getWidth() + "");
                        LogUtil.d(TAG, media.getHeight() + "");
                        singlePicture2Video(media.getPath());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void singlePicture2Video(String picturePath) {
        String combineVideo = PATH + File.separator + "combineVideo.mp4";
        String[] commandLine = FFmpegUtil.pictureToVideo(picturePath, combineVideo);
        executeFFmpegCmd(commandLine);
    }

    /**
     * 执行ffmpeg命令行
     *
     * @param commandLine commandLine
     */
    private void executeFFmpegCmd(final String[] commandLine) {
        if (commandLine == null) {
            return;
        }
        XYUtil.getInstance().execute(commandLine, new OnHandleListener() {
            @Override
            public void onBegin() {
                Log.i(TAG, "handle video onBegin...");
                mHandler.obtainMessage(MSG_BEGIN).sendToTarget();
            }

            @Override
            public void onEnd(int result) {
                Log.i(TAG, "handle video onEnd..." + result);
                mHandler.obtainMessage(MSG_FINISH).sendToTarget();
            }
        });
    }
}
