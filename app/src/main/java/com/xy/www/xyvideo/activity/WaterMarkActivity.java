package com.xy.www.xyvideo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.xy.www.xylib.XYUtil;
import com.xy.www.xylib.camera.XYCameraView;
import com.xy.www.xylib.egl.XYShaderUtil;
import com.xy.www.xylib.util.AppUtils;
import com.xy.www.xylib.util.Constant;
import com.xy.www.xylib.util.LogUtil;
import com.xy.www.xyvideo.CustomBottomSheetDialogFragment;
import com.xy.www.xyvideo.R;
import com.xy.www.xyvideo.base.BaseActivity;

/**
 * 录制时添加水印
 */
public class WaterMarkActivity extends BaseActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);
        initView();
    }

    private void initView() {
        xyUtil = XYUtil.getInstance(this);
        btRecoder = findViewById(R.id.bt_recoder);
        xycamaryview = findViewById(R.id.xycameraview);


        btRecoder.setOnClickListener(this);
        cbAddMark = findViewById(R.id.cb_add_mark);

        //是否使用水印
        cbAddMark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                xycamaryview.isAddMark = isChecked;
            }
        });
        btPlay = findViewById(R.id.bt_play);
        btPlay.setOnClickListener(this);

        cbAddDynamicMark = findViewById(R.id.cb_add_dynamic_mark);

        cbAddDynamicMark.setVisibility(View.GONE);

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
        btBackgroundMusic = findViewById(R.id.bt_background_music);
        btBackgroundMusic.setOnClickListener(this);
        btAddMarkContent = findViewById(R.id.bt_addMark_content);
        btAddMarkContent.setOnClickListener(this);
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
                if (xyUtil.isStart) {
                    xyUtil.isStart = false;
                    btRecoder.setText("录制视频");
                    xyUtil.stopRecoder();
                } else {
                    xyUtil.isStart = true;
                    xyUtil.startRecoder(this, xycamaryview);
                    btRecoder.setText("正在录制中...");
                }
                break;

            case R.id.bt_background_music://加入背景音乐录制
                if (xyUtil.isStart) {
                    xyUtil.isStart = false;
                    xyUtil.stopRecoder();
                    btBackgroundMusic.setText("录制视频");
                } else {
                    xyUtil.isStart = true;
                    xyUtil.setMusic(this, Constant.musicfileDir, xycamaryview);
                    btBackgroundMusic.setText("正在录制中...");
                }
                break;
            case R.id.bt_addMark_content:
                final CustomBottomSheetDialogFragment customBottomSheetDialogFragment = new CustomBottomSheetDialogFragment();
                customBottomSheetDialogFragment.show(getSupportFragmentManager(),null);
                customBottomSheetDialogFragment.setFragmentCallBack(new CustomBottomSheetDialogFragment.FragmentCallBack() {
                    @Override
                    public void onConfirm(String content) {
                        Constant.addMarkText = content;
//                        bitmap = XYShaderUtil.getCommon(Constant.addMarkText);//添加水印
                        xycamaryview.updateCurrentBitmap(XYShaderUtil.getCommon(Constant.addMarkText));//添加水印);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;
            case R.id.bt_play://播放
                readyGo(PreViewActivity.class);
                break;
        }
    }
}
