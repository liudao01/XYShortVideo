package com.xy.www.xylib.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.WindowManager;

import com.xy.www.xylib.R;
import com.xy.www.xylib.RenderInterface;
import com.xy.www.xylib.egl.XYEGLSurfaceView;
import com.xy.www.xylib.util.LogUtil;

import java.util.ArrayList;
import java.util.List;


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

    private List<Bitmap> bitmapList = new ArrayList<>();

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
        previewAngle(context);
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
            mHandler.postDelayed(this, 500);

            LogUtil.d("子线程");
            if (isDyNamicMark) {
                LogUtil.d("当前动态图 = " + count);
                LogUtil.d("当前动态图  " + bitmapList.get(count));
                xyCameraRender.setCurrentBitmap(bitmapList.get(count));
                count++;
                if(count==7){
                    count=0;
                }

            }
        }
    };

    public void setIsDyNamicMark(final boolean isDyNamicMark) {
        this.isDyNamicMark = isDyNamicMark;
        LogUtil.d("isDyNamicMark = " + isDyNamicMark);
        if (isDyNamicMark) {
            setDyNamickMark();//动态贴图
            mHandler.postDelayed(r, 100);//延时100毫秒
        }else {
            mHandler.removeCallbacks(r);
        }
    }


    private void setDyNamickMark() {
        int[] drawArr = {R.drawable.img_1, R.drawable.img_2, R.drawable.img_3, R.drawable.img_4,
                R.drawable.img_5, R.drawable.img_6, R.drawable.img_7, R.drawable.img_8};
        for (int i = 0; i < 8; i++) {

            Bitmap bitmap = BitmapFactory.decodeResource(this.getContext().getResources(), drawArr[i]);
            bitmapList.add(bitmap);
        }
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
            xyCamera.stopPreview();
        }
    }
}