package com.xy.www.xylib;

import android.content.Context;
import android.text.TextUtils;

import com.xy.www.xylib.camera.XYCameraView;
import com.xy.www.xylib.encodec.XYBaseMediaEncoder;
import com.xy.www.xylib.encodec.XYMediaEncodec;
import com.xy.www.xylib.listener.OnHandleListener;
import com.xy.www.xylib.util.AudioRecordUtil;
import com.xy.www.xylib.util.Constants;
import com.xy.www.xylib.util.FFmpegUtil;
import com.xy.www.xylib.util.LogUtil;
import com.ywl5320.libmusic.WlMusic;
import com.ywl5320.listener.OnPreparedListener;
import com.ywl5320.listener.OnShowPcmDataListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

    public static XYUtil getInstance() {
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
                //裁剪0到30s的
                wlMusic.playCutAudio(0, 30);
            }
        });

        wlMusic.setOnShowPcmDataListener(new OnShowPcmDataListener() {
            @Override
            public void onPcmInfo(int samplerate, int bit, int channels) {
                xyMediaEncodec = new XYMediaEncodec(context, imgvideoview.getTextureId());
                xyMediaEncodec.initEncodec(imgvideoview.getEglContext(), Constants.fileDir,
                        Constants.ScreenWidth, Constants.ScreenHeight, samplerate, channels);

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
                    Constants.fileDir, Constants.ScreenWidth, Constants.ScreenHeight, 44100, 2);
        } else {
            xyMediaEncodec.initEncodec(xycamaryview.getEglContext(),
                    url, Constants.ScreenWidth, Constants.ScreenHeight, 44100, 2);
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
     *
     * @param file1Dir
     * @param file2Dir
     * @param outputFile
     */
    public void mergeVideo(String file1Dir, String file2Dir, String outputFile, OnHandleListener onHandleListener) {
//        commandLine = FFmpegUtil.toTs(srcFile, ts1);
//                concatStep ++;
        String concatVideo = file1Dir;
        String appendVideo = file2Dir;
        File concatFile = new File(Constants.rootDir + File.separator + "fileList.txt");
        if (!concatFile.exists()) {
            try {
                concatFile.getParentFile().mkdirs();
                concatFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(concatFile);
            fileOutputStream.write(("file \'" + concatVideo + "\'").getBytes());
            fileOutputStream.write("\n".getBytes());
            fileOutputStream.write(("file \'" + appendVideo + "\'").getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        execute(FFmpegUtil.mergeVideo(concatFile, outputFile), onHandleListener);
    }

    /*
     * 视频抽帧转成图片
     *
     * @param inputFile  输入文件
     * @param startTime  开始时间
     * @param duration   持续时间
     * @param frameRate  帧率
     * @param targetFile 输出文件
     *
     */
    public void videoToImage(String inputFile, int startTime, int duration, int frameRate, String targetFile, OnHandleListener onHandleListener) {
        String[] strings = FFmpegUtil.videoToImage(inputFile, startTime, duration, frameRate, targetFile);
        execute(strings, onHandleListener);
    }


    /**
     * 使用ffmpeg命令行进行视频剪切
     *
     * @param srcFile    源文件
     * @param startTime  剪切的开始时间(单位为秒)
     * @param duration   剪切时长(单位为秒)
     * @param targetFile 目标文件
     * @return 剪切后的文件
     */
    public void cutVideo(String srcFile, int startTime, int duration, String targetFile, OnHandleListener onHandleListener) {
        String[] strings = FFmpegUtil.cutVideo(srcFile, startTime, duration, targetFile);
        execute(strings, onHandleListener);

    }

    /**
     * 旋转视频
     *
     * @param srcFile
     * @param targetFile
     * @param rotate
     * @param onHandleListener
     */
    public void rotateVideo(String srcFile, String targetFile, int rotate, OnHandleListener onHandleListener) {
        rotate = Math.abs(90 - rotate);
        String[] strings = FFmpegUtil.rotate(srcFile, targetFile, rotate);
        execute(strings, onHandleListener);

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
                LogUtil.d("是否执行成功 result =" + result);
                if (onHandleListener != null) {
                    onHandleListener.onEnd(result);
                }
            }
        }).start();
    }


}
