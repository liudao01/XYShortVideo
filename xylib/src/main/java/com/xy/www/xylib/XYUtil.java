package com.xy.www.xylib;

import android.content.Context;
import android.text.TextUtils;

import com.xy.www.xylib.camera.XYCameraView;
import com.xy.www.xylib.encodec.XYBaseMediaEncoder;
import com.xy.www.xylib.encodec.XYMediaEncodec;
import com.xy.www.xylib.listener.OnHandleListener;
import com.xy.www.xylib.util.AudioRecordUtil;
import com.xy.www.xylib.util.Constant;
import com.xy.www.xylib.util.FFmpegUtil;
import com.xy.www.xylib.util.LogUtil;
import com.ywl5320.libmusic.WlMusic;
import com.ywl5320.listener.OnPreparedListener;
import com.ywl5320.listener.OnShowPcmDataListener;

/**
 * @author liuml
 * @explain
 * @time 2018/12/26 10:27
 */
public class XYUtil {

    private int result = 0;
    public boolean isStart = false;

    private native static int handle(String[] commands);


    private AudioRecordUtil audioRecordUtil;
    private XYMediaEncodec xyMediaEncodec;

    private Context mContext;
    private XYCameraView imgvideoview;

    private static class staticXYUtil {
        private static XYUtil xyUtil = new XYUtil();
    }

    private XYUtil() {

    }

    public static XYUtil getInstance(Context context) {
        return staticXYUtil.xyUtil;
    }

    static {
        System.loadLibrary("native-lib");
    }

    //这里用了个第三方库 主要用于把音频解析成Pcm数据  其实也可以用ffmpeg 解析
    private WlMusic wlMusic;

    public void setMusic(final Context context, String url, final XYCameraView imgvideoview) {
        if (wlMusic == null) {
            wlMusic = WlMusic.getInstance();
        }
        wlMusic.setCallBackPcmData(true);
        wlMusic.setSource(url);


        wlMusic.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                //裁剪0到60s的

                wlMusic.playCutAudio(0, 60);
            }
        });

        wlMusic.setOnShowPcmDataListener(new OnShowPcmDataListener() {
            @Override
            public void onPcmInfo(int samplerate, int bit, int channels) {
                xyMediaEncodec = new XYMediaEncodec(context, imgvideoview.getTextureId());
                xyMediaEncodec.initEncodec(imgvideoview.getEglContext(), Constant.fileDir,
                        Constant.ScreenWidth, Constant.ScreenHeight, samplerate, channels);

                xyMediaEncodec.startRecord();
            }

            @Override
            public void onPcmData(byte[] pcmdata, int size, long clock) {

                if (xyMediaEncodec != null) {
                    xyMediaEncodec.putPCMData(pcmdata, size);
                }
            }


        });

        wlMusic.prePared();
    }


    public void stopRecoder() {
        isStart = false;
        if (audioRecordUtil != null) {
            audioRecordUtil.stopRecord();
        }
        if (xyMediaEncodec != null) {
            xyMediaEncodec.stopRecord();
        }
        if (wlMusic != null) {
            wlMusic.stop();
        }
    }

    public void startRecoder(Context context, XYCameraView xycamaryview) {
        startRecoder(context, xycamaryview, "");
    }

    public void startRecoder(Context context, XYCameraView xycamaryview, String url) {
        isStart = true;
        audioRecordUtil = new AudioRecordUtil();
        xyMediaEncodec = new XYMediaEncodec(context, xycamaryview.getTextureId());
        if (TextUtils.isEmpty(url)) {
            xyMediaEncodec.initEncodec(xycamaryview.getEglContext(),
                    Constant.fileDir, Constant.ScreenWidth, Constant.ScreenHeight, 44100, 2);
        } else {
            xyMediaEncodec.initEncodec(xycamaryview.getEglContext(),
                    url, Constant.ScreenWidth, Constant.ScreenHeight, 44100, 2);
        }

        xyMediaEncodec.setOnMediaInfoListener(new XYBaseMediaEncoder.OnMediaInfoListener() {
            @Override
            public void onMediaTime(int times) {
//                LogUtil.d("time = " + times);
            }
        });

        audioRecordUtil.setOnRecordListener(new AudioRecordUtil.OnRecordListener() {
            @Override
            public void recordByte(byte[] audioData, int readSize) {

                if (xyMediaEncodec != null) {
                    xyMediaEncodec.putPCMData(audioData, readSize);
                }

            }
        });
        audioRecordUtil.startRecord();
        xyMediaEncodec.startRecord();
    }

    /**
     * 合并视频
     * @param file1Dir
     * @param file2Dir
     * @param outputFile
     */
    public void mergeVideo(String file1Dir,String file2Dir,String outputFile,OnHandleListener onHandleListener) {
        execute(FFmpegUtil.mergeVideo(file1Dir,file2Dir,outputFile),onHandleListener);
    }

    public void execute(final String[] commands, final OnHandleListener onHandleListener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (onHandleListener != null) {
                    onHandleListener.onBegin();
                }
                //调用ffmpeg进行处理
                LogUtil.d("commands = " + commands);

                int result = handle(commands);//result 0 是成功
                LogUtil.d("是否执行成功 result ="+result);
                if (onHandleListener != null) {
                    onHandleListener.onEnd(result);
                }
            }
        }).start();
    }


//    public int handle(String[] commands){
//        return n_handle(commands);
//    }


}
