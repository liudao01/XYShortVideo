package com.xy.www.xyvideo.util;

import android.app.Activity;
import android.content.Intent;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.xy.www.xylib.util.Constants;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * @author liuml
 * @explain
 * @time 2019/1/14 10:40
 */
public class FileChooserUtil {
    private static int requestcode_from_activity;

    private Activity activity;
    private static FileChooserUtil fileChooserUtil = null;

    public FileChooserUtil(Activity activity) {
        this.activity = activity;
    }

    public static FileChooserUtil getInstanse(Activity activity) {
        if (fileChooserUtil == null) {
            fileChooserUtil = new FileChooserUtil(activity);
        }
        return fileChooserUtil;
    }


    public void fileChooser() {
        requestcode_from_activity = 1000;
        new LFilePicker()
                .withActivity(activity)
                .withRequestCode(requestcode_from_activity)
                .withStartPath(Constants.rootDir)//指定初始显示路径
                .withIsGreater(true)//过滤文件大小 小于指定大小的文件
                .withFileSize(500)//指定文件大小为500K
                .start();
    }

    public String onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == requestcode_from_activity) {
                List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
                ToastUtils.showToast(activity, list.get(0));
                return list.get(0);
            }
        }
        return null;
    }
}
