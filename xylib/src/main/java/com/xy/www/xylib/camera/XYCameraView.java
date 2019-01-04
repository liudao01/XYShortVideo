package com.xy.www.xylib.camera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.WindowManager;

import com.xy.www.xylib.RenderInterface;
import com.xy.www.xylib.egl.XYEGLSurfaceView;
import com.xy.www.xylib.util.LogUtil;


/**
 * @author liuml
 * @explain
 * @time 2018/12/7 15:53
 */
public class XYCameraView extends XYEGLSurfaceView {

    private float oldX;
    private float oldY;
    private final float TOUCH_SCALE = 0.2f;        //Proved to be good for normal rotation ( NEW )


    private RenderInterface renderInterface;
    private XYCameraRender xyCameraRender;
    private XYCamera xyCamera;

    private Context context;

    public static boolean isAddMark = false;//是否添加水印
    public static boolean isDyNamicMark = false;//是否动态水印

    //摄像头
    private int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private int textureId = -1;

    private int count = 0;

    public XYCameraView(Context context) {
        this(context, null);
    }

    public XYCameraView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XYCameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        xyCameraRender = new XYCameraRender(context);
        xyCamera = new XYCamera(context);
        setRender(xyCameraRender);
//        setRenderMode(RENDERMODE_WHEN_DIRTY);//手动
        previewAngle(context);
        setDyNamickMark();//动态贴图
        xyCameraRender.setOnSurfaceCreateListener(new XYCameraRender.OnSurfaceCreateListener() {
            @Override
            public void onSurfaceCreate(SurfaceTexture surfaceTexture, int tid) {
                LogUtil.d("render 回调");
                xyCamera.initCamera(surfaceTexture, cameraId);
                textureId = tid;
            }
        });


    }


    /**
     * 预览旋转角度
     *
     * @param context
     */
    public void previewAngle(Context context) {
        int angle = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        xyCameraRender.resetMatrix();

        switch (angle) {
            case Surface.ROTATION_0:
                LogUtil.d("当前角度 0");
                if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    //后置摄像头
                    xyCameraRender.setAngle(90, 0, 0, 1);
                    xyCameraRender.setAngle(180, 1, 0, 0);

                } else {
                    xyCameraRender.setAngle(90, 0, 0, 1);

                }

                break;
            case Surface.ROTATION_90:
                LogUtil.d("当前角度 90");
                if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {

                    xyCameraRender.setAngle(180, 0, 0, 1);
                    xyCameraRender.setAngle(180, 0, 1, 0);
                } else {
                    xyCameraRender.setAngle(90f, 0f, 0f, 1f);
                }
                break;
            case Surface.ROTATION_180:
                LogUtil.d("当前角度 180");
                if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    xyCameraRender.setAngle(90, 0, 0, 1);
                    xyCameraRender.setAngle(180, 0, 1, 0);
                } else {
                    xyCameraRender.setAngle(-90f, 0f, 0f, 1f);
                }
                break;
            case Surface.ROTATION_270:
                LogUtil.d("当前角度 270");
                if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
//                    xyCameraRender.setAngle(180, 0, 0, 1);
                    xyCameraRender.setAngle(180, 0, 1, 0);
                } else {
                    xyCameraRender.setAngle(0f, 0f, 0f, 1f);
                }
                break;
        }

    }


    public int getTextureId() {
        return textureId;
    }

    Handler mHandler = new Handler();
    Runnable r = new Runnable() {

        @Override
        public void run() {
            //do something
            //每隔1s循环执行run方法
            mHandler.postDelayed(this, 100);

            LogUtil.d("子线程");
            if (isDyNamicMark) {
                LogUtil.d("当前动态图 = " + count);
                for (int i = 0; i < 8; i++) {
                    //拿到资源
                    int imgsrc = getResources().getIdentifier("img_" + i, "drawable", "com.xyyy.livepusher");
//                    Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), imgsrc);
//                    xyCameraRender.setCurrentBitmap(bitmap);
                    xyCameraRender.setCurrentImgSrc(imgsrc);
                    if (i == 7) {
                        i = 0;
                    }

                }


            }
        }
    };

    public void setIsDyNamicMark(final boolean isDyNamicMark) {
        this.isDyNamicMark = isDyNamicMark;
        LogUtil.d("isDyNamicMark = " + isDyNamicMark);
        if (isDyNamicMark) {
            mHandler.postDelayed(r, 100);//延时100毫秒
        } else {
            mHandler.removeCallbacks(r);
        }
    }

    //
//    public void setCurrentImg(int imgsrc) {
//        if (xyImgVideoRender != null) {
//            xyImgVideoRender.setCurrentImgSrc(imgsrc);
//            requestRender();//手动刷新 调用一次
//            LogUtil.d("手动刷新 "+imgsrc);
//        }
//    }
    private void setDyNamickMark() {


        LogUtil.d("获取bitmap 数组");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //
        float x = event.getX();
        float y = event.getY();

        //If a touch is moved on the screen
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //Calculate the change
            float dx = x - oldX;
            float dy = y - oldY;
            //Define an upper area of 10% on the screen
            int upperArea = this.getHeight() / 10;

            //Zoom in/out if the touch move has been made in the upper

            //A press on the screen
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            //Define an upper area of 10% to define a lower area
            int upperArea = this.getHeight() / 10;
            int lowerArea = this.getHeight() - upperArea;
        }

        //Remember the values
        oldX = x;
        oldY = y;

        //We handled the event
        return true;
    }

    public void onDestory() {
        if (xyCamera != null) {
            mHandler.removeCallbacks(r);
            xyCamera.stopPreview();
        }
    }
}
