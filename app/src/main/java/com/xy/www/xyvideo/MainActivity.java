package com.xy.www.xyvideo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xy.www.xylib.XYUtil;
import com.xy.www.xyvideo.activity.WaterMarkActivity;
import com.xy.www.xyvideo.base.BaseActivity;

public class MainActivity extends BaseActivity {


    private Button btAddMark;
    private XYUtil xyUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }


    private void initView() {
        final Intent intent = new Intent(this, WaterMarkActivity.class);
        btAddMark = findViewById(R.id.bt_addMark);
        btAddMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
//                readyGo(AddWateMarkActivity.class);
            }
        });
    }
}
