package com.xy.www.xyvideo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xy.www.xylib.camera.XYCameraView;
import com.xy.www.xyvideo.base.BaseActivity;

/**
 * 回放时添加水印
 */
public class AddWateMarkActivity extends BaseActivity {

    private Button btShowFragment;
    private XYCameraView camaryPlayVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wate_mark);
        initView();
    }

    private void initView() {
        final CustomBottomSheetDialogFragment customBottomSheetDialogFragment = new CustomBottomSheetDialogFragment();
        btShowFragment = findViewById(R.id.bt_showFragment);
        btShowFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customBottomSheetDialogFragment.isHidden()) {
                    customBottomSheetDialogFragment.dismiss();
                } else {
                    customBottomSheetDialogFragment.show(getSupportFragmentManager(), "Dialog");
                }
            }
        });
        camaryPlayVideo = findViewById(R.id.camary_play_video);


    }
}
