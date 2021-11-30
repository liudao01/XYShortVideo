package com.xy.www.xylib.camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;

import com.xy.www.xylib.util.DisplayUtil;

import java.io.IOException;
import java.util.List;

/**
 * @author liuml
 * @explain
 * @time 2018/12/10 11:08
 */
public class XYCamera {

    private SurfaceTexture surfaceTexture;
    //导包注意用硬件的
    private Camera camera;


    private CameraManager cameraManager;

    private int width;
    private int height;

    public XYCamera(Context context) {
        this.width = DisplayUtil.getScreenWidth(context);
        this.height = DisplayUtil.getScreenHeight(context);
    }


    public void initCamera2() {
       String mCameraID = "" + CameraCharacteristics.LENS_FACING_BACK;//后摄像头

    }
    /**
     * @param surfaceTexture
     * @param cameraId       前置还是后置
     */
    public void initCamera(SurfaceTexture surfaceTexture, int cameraId) {
        this.surfaceTexture = surfaceTexture;

        setCameraParm(cameraId);

        setCameraParm();
    }

    private void setCameraParm() {
//        HandlerThread handlerThread=new HandlerThread("Camera2");
//        handlerThread.start();
//        Handler  childHandler=new Handler(handlerThread.getLooper());
//        Handler mainHandler=new Handler(getMainLooper());
//        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() { //可以在这里处理拍照得到的临时照片 例如，写入本地
//            @Override
//            public void onImageAvailable(ImageReader reader) {
//                mCameraDevice.close();
//                surfaceView.setVisibility(View.GONE);
//                iv_show.setVisibility(View.VISIBLE);
//                // 拿到拍照照片数据
//                Image image = reader.acquireNextImage();
//                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
//                byte[] bytes = new byte[buffer.remaining()];
//                buffer.get(bytes);//由缓冲区存入字节数组
//                final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                if (bitmap != null) {
//                    iv_show.setImageBitmap(bitmap);
//                }
//            }
//        }, mainHandler);
//
//        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
//        try {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            //打开摄像头
//            cameraManager.openCamera(mCameraID, stateCallback, mainHandler);
//        } catch (CameraAccessException e) {
//            e.printStackTrace();
//        }
    }

    private void setCameraParm(int cameraId) {
        try {
            camera = Camera.open(cameraId);
            camera.setPreviewTexture(surfaceTexture);
            Camera.Parameters parameters = camera.getParameters();

            parameters.setFlashMode("off");//闪光灯
            parameters.setPreviewFormat(ImageFormat.NV21);

            Camera.Size size = getFitSize(parameters.getSupportedPictureSizes());
            parameters.setPictureSize(size.width, size.height);

            size = getFitSize(parameters.getSupportedPreviewSizes());
            parameters.setPreviewSize(size.width, size.height);

            camera.setParameters(parameters);
            camera.startPreview();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopPreview() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    public void changeCamera(int cameraId) {

        if (camera != null) {
            stopPreview();
        }
        setCameraParm(cameraId);

    }

    /**
     * 动态的设置宽高
     * @param sizes
     * @return
     */
    private Camera.Size getFitSize(List<Camera.Size> sizes) {
        if (width < height) {
            int t = height;
            height = width;
            width = t;
        }

        for (Camera.Size size : sizes) {
            if (1.0f * size.width / size.height == 1.0f * width / height) {
                return size;
            }
        }
        return sizes.get(0);
    }

}






