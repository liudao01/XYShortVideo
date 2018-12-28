package com.xy.www.xylib.camera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.WindowManager;

import com.xy.www.xylib.egl.XYEGLSurfaceView;
import com.xy.www.xylib.util.LogUtil;


/**
 * @author liuml
 * @explain
 * @time 2018/12/7 15:53
 */
public class XYCameraView extends XYEGLSurfaceView {

    private XYCameraRender xyCameraRender;
    private XYCamera xyCamera;
    //摄像头
    private int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private int textureId = -1;

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
            public void onSurfaceCreate(SurfaceTexture surfaceTexture,int tid) {
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


    public int getTextureId(){
        return textureId;
    }
    public void onDestory() {
        if (xyCamera != null) {
            xyCamera.stopPreview();
        }
    }
}
