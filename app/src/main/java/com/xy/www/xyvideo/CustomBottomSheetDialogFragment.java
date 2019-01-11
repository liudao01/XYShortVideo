package com.xy.www.xyvideo;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.xy.www.xylib.util.Constant;

/**
 * @author liuml
 * @explain
 * @time 2018/12/28 10:15
 */
public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {


    public interface FragmentCallBack {
        void onConfirm(String content);

        void onCancel();
    }

    FragmentCallBack fragmentCallBack;
    private View v;
    private TextView tvCancel;
    private TextView tvConfirm;
    private EditText etMarkContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.bottom_sheet, container, false);
        initView();
        return v;
    }


    public void setFragmentCallBack(FragmentCallBack fragmentCallBack) {
        this.fragmentCallBack = fragmentCallBack;
    }

    private void initView() {
        tvCancel = v.findViewById(R.id.tv_cancel);
        tvConfirm = v.findViewById(R.id.tv_confirm);
        etMarkContent = v.findViewById(R.id.et_mark_content);
        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                this.dismiss();
                break;
            case R.id.tv_confirm:
                if (fragmentCallBack != null) {
                    if (etMarkContent.getText().toString().isEmpty()) {
                        fragmentCallBack.onConfirm(Constant.addMarkText);
                    } else {
                        fragmentCallBack.onConfirm(etMarkContent.getText().toString());
                    }
                }
                this.dismiss();
                break;
        }
    }
}