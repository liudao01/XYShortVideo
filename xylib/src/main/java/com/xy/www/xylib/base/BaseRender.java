package com.xy.www.xylib.base;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.xy.www.xylib.BuildConfig;
import com.xy.www.xylib.util.XYShaderUtil;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * @author liuml
 * @explain
 * @time 2019/3/13 14:37
 */
public abstract class BaseRender implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    private Context context;

    private float[] vertexData = {
            -1f, -1f,
            1f, -1f,
            -1f, 1f,
            1f, 1f

    };

    private float[] fragmentData = {
            0f, 1f,
            1f, 1f,
            0f, 0f,
            1f, 0f
    };
    public volatile float moveX;
    public volatile float moveY;

    private FloatBuffer vertexBuffer;
    private FloatBuffer fragmentBuffer;

    private int program;
    private int vPosition;
    private int fPosition;
    private int vboId;
    private int fboId;

    private int fboTextureid;
    private int cameraTextureid;//摄像头纹理

    private int screenW = 1080;
    private int screenH = 1920;

    //实际渲染的大小
    private int width;
    private int height;

    //自己生成的SurfaceTexture
    private SurfaceTexture surfaceTexture;
//    private XYCameraFboRender xyCameraFboRender;

    //用于操作矩阵
    private int umatrix;
    private float[] matrix = new float[16];

    private OnSurfaceCreateListener onSurfaceCreateListener;
    public boolean isReadCurrentFrame = false;
    private int cameraRenderTextureId;

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {

    }

    public BaseRender(Context context) {
        this.context = context;
    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void makeProgram(int vertexShader, int fragmentShader) {

        String vertexSource = XYShaderUtil.getRawResource(context, vertexShader);
        String fragmentSource = XYShaderUtil.getRawResource(context, fragmentShader);

        program = XYShaderUtil.createProgram(vertexSource, fragmentSource);

        if (BuildConfig.DEBUG) {
            XYShaderUtil.validateProgram(program);
        }

        // 步骤4：通知OpenGL开始使用该程序
        GLES20.glUseProgram(program);
    }
    /**
     * 创建摄像头预览扩展纹理
     */
    private void createCameraRenderTexture() {
        int[] textureIds = new int[1];
        GLES20.glGenTextures(1, textureIds, 0);
        cameraRenderTextureId = textureIds[0];
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, cameraRenderTextureId);
        //环绕（超出纹理坐标范围）  （s==x t==y GL_REPEAT 重复）
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        //过滤（纹理像素映射到坐标点）  （缩小、放大：GL_LINEAR线性）
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        surfaceTexture = new SurfaceTexture(cameraRenderTextureId);
        surfaceTexture.setOnFrameAvailableListener(this);

        if (onSurfaceCreateListener != null) {
            //这里相机拿到surfaceTexture绑定
            onSurfaceCreateListener.onSurfaceCreate(surfaceTexture,fboTextureid);
        }

        // 解绑扩展纹理
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
    }

    public void createOESTextureId() {

        //扩展纹理  eos
        int[] textureidsEos = new int[1];
        GLES20.glGenTextures(1, textureidsEos, 0);
        cameraTextureid = textureidsEos[0];
//

        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, cameraTextureid);

        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        surfaceTexture = new SurfaceTexture(cameraTextureid);
        surfaceTexture.setOnFrameAvailableListener(this);

        if (onSurfaceCreateListener != null) {
            onSurfaceCreateListener.onSurfaceCreate(surfaceTexture, fboTextureid);
        }
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);

    }

    /**
     * 获取uniform 自己定义的常量
     * @param name
     * @return
     */
    public int getUniform(String name) {
        return GLES20.glGetUniformLocation(program, name);
    }

    /**
     * 获取顶点索引 或者获取片元索引
     * @param name
     * @return
     */
    public int getAttrib(String name) {
        return GLES20.glGetAttribLocation(program, name);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        surfaceTexture.updateTexImage();

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(1f, 0f, 0f, 1f);

        GLES20.glUseProgram(program);

        GLES20.glViewport(0, 0, screenW, screenH);
        //使用program后调用矩阵
        GLES20.glUniformMatrix4fv(umatrix, 1, false, matrix, 0);


        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId);


        GLES20.glEnableVertexAttribArray(vPosition);
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 8,
                0);

        GLES20.glEnableVertexAttribArray(fPosition);
        GLES20.glVertexAttribPointer(fPosition, 2, GLES20.GL_FLOAT, false, 8,
                vertexData.length * 4);


        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

//        xyCameraFboRender.onChange(width, height);//
//        //绘制
//        xyCameraFboRender.onDraw(fboTextureid);
    }

    public interface OnSurfaceCreateListener {
        void onSurfaceCreate(SurfaceTexture surfaceTexture, int textureId);
    }
}
