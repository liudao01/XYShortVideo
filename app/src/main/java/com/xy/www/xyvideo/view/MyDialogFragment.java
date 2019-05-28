package com.xy.www.xyvideo.view;

/**
 * @author liuml
 * @explain
 * @time 2019/5/28 14:29
 */

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xy.www.xylib.util.LogUtil;
import com.xy.www.xyvideo.R;

/**
 * @author liumaolin
 * @explain
 * @time 2018/7/6 17:17
 */
public class MyDialogFragment extends BottomDialog {

    public DialogInterface dialogInterface;
    public Activity activity;
    private View inflate;
    private int height;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String string;

    public static final MyDialogFragment newInstance(int param1) {

        MyDialogFragment fragment = new MyDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }


    public void setInterface(DialogInterface dialogInterface) {
        this.dialogInterface = dialogInterface;
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置 dialog 的宽高
        if (height > 0) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height);
        } else {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        //设置 dialog 的背景为 null
        getDialog().getWindow().setBackgroundDrawable(null);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        if (getArguments() != null) {
            height = getArguments().getInt(ARG_PARAM1);
            LogUtil.d("传递过来的 = " + string);
        }
        inflate = inflater.inflate(R.layout.bottom_dialog_view, container);
//        TextView viewById = inflate.findViewById(R.id.tv_login);
//        viewById.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });


        return inflate;
    }


//
}


