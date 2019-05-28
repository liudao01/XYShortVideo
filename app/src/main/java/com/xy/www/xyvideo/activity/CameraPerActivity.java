package com.xy.www.xyvideo.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.xy.www.xylib.XYUtil;
import com.xy.www.xylib.camera.XYCameraView;
import com.xy.www.xylib.listener.OnHandleListener;
import com.xy.www.xylib.util.AppUtils;
import com.xy.www.xylib.util.Constants;
import com.xy.www.xylib.util.LogUtil;
import com.xy.www.xyvideo.R;
import com.xy.www.xyvideo.base.BaseActivity;
import com.xy.www.xyvideo.util.ProgressDlgUtil;
import com.xy.www.xyvideo.util.ToastUtils;

/**
 * 录制时添加水印,添加背景音乐.断点录制
 */
public class CameraPerActivity extends BaseActivity implements View.OnClickListener {

    private Button btRecoder;
    private XYCameraView xycamaryview;

    private CheckBox cbAddMark;
    private Button btPlay;
    private CheckBox cbAddDynamicMark;
    private Thread thread;
    boolean isDynamic = false;
    private Button btBackgroundMusic;


    private XYUtil xyUtil;
    private Button btAddMarkContent;
    private int type = 1;
    private Button btBreakpoint;

    private Context mContext;

    private int count = 0;//计数器 第一次录制还是第二次
    private Button btSelectFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);
        initView();
    }

    private void initView() {
        this.mContext = this;
        type = getIntent().getIntExtra("key", 1);
        xyUtil = XYUtil.getInstance();
        btRecoder = findViewById(R.id.bt_recoder);
        xycamaryview = findViewById(R.id.xycameraview);


        cbAddMark = findViewById(R.id.cb_add_mark);

        //是否使用水印
        cbAddMark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                xycamaryview.isAddMark = isChecked;
            }
        });
        btPlay = findViewById(R.id.bt_play);
        btBreakpoint = findViewById(R.id.bt_breakpoint);
        btBackgroundMusic = findViewById(R.id.bt_background_music);
        btAddMarkContent = findViewById(R.id.bt_addMark_content);


        cbAddDynamicMark = findViewById(R.id.cb_add_dynamic_mark);

        cbAddDynamicMark.setVisibility(View.GONE);
        btSelectFilter = findViewById(R.id.bt_select_filter);

        cbAddDynamicMark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LogUtil.d("调用动态水印");
                isDynamic = isChecked;
                if (isChecked) {
                    startImg();
                } else {
                    xycamaryview.isDyNamicMark = !xycamaryview.isDyNamicMark;
                }
            }
        });

        btRecoder.setOnClickListener(this);
        btPlay.setOnClickListener(this);
        btBackgroundMusic.setOnClickListener(this);
        btAddMarkContent.setOnClickListener(this);
        btBreakpoint.setOnClickListener(this);
        btSelectFilter.setOnClickListener(this);

        switch (type) {
            case 1:
                btRecoder.setVisibility(View.VISIBLE);
                break;
            case 2:
                btBackgroundMusic.setVisibility(View.VISIBLE);
                break;
            case 3:
                btPlay.setText("合并视频播放");
                btBreakpoint.setVisibility(View.VISIBLE);
                break;
        }
    }


    private void startImg() {

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 8; i++) {
                    //拿到资源
                    int imgsrc = getResources().getIdentifier("img_" + i, "drawable", AppUtils.getPackageName(getApplicationContext()));
                    xycamaryview.setCurrentImg(imgsrc);

                    if (i == 7) {
                        i = 0;
                    }
                    try {
                        Thread.sleep(80);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        thread.start();

    }

    private void startRecoder() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_recoder://录制
                if (xyUtil.isRecording) {
                    xyUtil.isRecording = false;
                    btRecoder.setText("录制视频");
                    xyUtil.stopRecoder();
                } else {
                    xyUtil.isRecording = true;
                    xyUtil.startRecoder(this, xycamaryview);
                    btRecoder.setText("正在录制中...");
                }
                break;

            case R.id.bt_background_music://加入背景音乐录制
                if (xyUtil.isRecording) {
                    xyUtil.isRecording = false;
                    xyUtil.stopRecoder();
                    btBackgroundMusic.setText("录制视频");
                } else {
                    xyUtil.isRecording = true;
                    xyUtil.setMusic(this, Constants.musicfileDir, xycamaryview);
                    btBackgroundMusic.setText("正在录制中...");
                }
                break;
            case R.id.bt_addMark_content://TODO 暂时不用 后面改
//                final CustomBottomSheetDialogFragment customBottomSheetDialogFragment = new CustomBottomSheetDialogFragment();
//                customBottomSheetDialogFragment.show(getSupportFragmentManager(), null);
//                customBottomSheetDialogFragment.setFragmentCallBack(new CustomBottomSheetDialogFragment.FragmentCallBack() {
//                    @Override
//                    public void onConfirm(String content) {
//                        Constant.addMarkText = content;
////                        bitmap = XYShaderUtil.getCommon(Constant.addMarkText);//添加水印
//                        xycamaryview.updateCurrentBitmap(XYShaderUtil.getCommon(Constant.addMarkText));//添加水印);
//                    }
//
//                    @Override
//                    public void onCancel() {
//
//                    }
//                });
                break;
            case R.id.bt_breakpoint://断点录制
                if (count == 2 && !xyUtil.isRecording) {
                    ToastUtils.showToast(context, "当前只未开始或者录制超过两个视频");
                    //调用合成

                    return;
                }
                String url = null;
                if (xyUtil.isRecording) {
                    if (count == 0) {
                        btBreakpoint.setText("录制第一个视频");
                    } else {
                        btBreakpoint.setText("录制第二个视频");
                    }
                    xyUtil.isRecording = false;
                    xyUtil.stopRecoder();
                } else {
                    xyUtil.isRecording = true;
                    if (count == 0) {
                        btBreakpoint.setText("正在录制第一个...");
                        url = Constants.breakPointfile1;
                    } else {
                        btBreakpoint.setText("正在录制第二个...");
                        url = Constants.breakPointfile2;
                    }
                    xyUtil.startRecoder(this, xycamaryview, url);
                    count++;
                }


                break;
            case R.id.bt_select_filter://选择滤镜


                break;
            case R.id.bt_play://播放
                if (type == 3) {
                    xyUtil.mergeVideo(Constants.breakPointfile1, Constants.breakPointfile2, Constants.fileDir, new OnHandleListener() {
                        @Override
                        public void onBegin() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ProgressDlgUtil.showProgressDlg("正在处理中...", mContext);

                                }
                            });
                        }

                        @Override
                        public void onEnd(int result) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ProgressDlgUtil.stopProgressDlg();
                                }
                            });
                            if (result == 0) {
                                readyGo(PlayBackActivity.class);
                            }
                        }
                    });
                } else {
                    readyGo(PlayBackActivity.class);
                }
                break;
        }
    }
}
