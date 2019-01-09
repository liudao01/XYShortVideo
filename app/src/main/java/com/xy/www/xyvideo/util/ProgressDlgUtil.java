package com.xy.www.xyvideo.util;

import android.app.ProgressDialog;
import android.content.Context;

import com.xy.www.xyvideo.R;

/**
 * @author liuml
 * @explain
 * @time 2019/1/9 10:52
 */
public class ProgressDlgUtil {
    static ProgressDialog progressDlg = null;

    /**
     * 启动进度条
     *
     * @param strMessage
     *            进度条显示的信息
     * @param ctx
     *            当前的activity
     */
    public static void showProgressDlg(String strMessage, Context ctx) {

        if (null == progressDlg) {
            progressDlg = new ProgressDialog(ctx);
            //设置进度条样式
            progressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //设置进度条标题
            progressDlg.setTitle(ctx.getString(
                    R.string.app_name));
            //提示的消息
            progressDlg.setMessage(strMessage);
            progressDlg.setIndeterminate(false);
            progressDlg.setCancelable(false);
            progressDlg.setIcon(R.drawable.ic_launcher_background);
            progressDlg.show();
        }
    }

    /**
     * 结束进度条
     */
    public static void stopProgressDlg() {
        if (null != progressDlg) {
            progressDlg.dismiss();
            progressDlg = null;
        }
    }
}
