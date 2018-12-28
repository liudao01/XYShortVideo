package com.xy.www.xylib.shortvideo.play;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;

import com.xy.www.xylib.R;
import com.xy.www.xylib.egl.XYEGLSurfaceView;
import com.xy.www.xylib.egl.XYShaderUtil;
import com.xy.www.xylib.util.LogUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @author liuml
 * @explain
 * @time 2018/12/28 13:55
 */
public class XYPlayVideoRender  implements XYEGLSurfaceView.XYGLRender{

    private Context context;

    private float[] vertexData = {
            -1f, -1f,
            1f, -1f,
            -1f, 1f,
            1f, 1f

    };
    //因为经过了编码的过程 所以这里的纹理坐标需要颠倒
    private float[] fragmentData = {
            0f, 0f,
            1f, 0f,
            0f, 1f,
            1f, 1f
    };
//    private float[] fragmentData = {
//            0f, 1f,
//            1f, 1f,
//            0f, 0f,
//            1f, 0f
//    };

    private FloatBuffer vertexBuffer;
    private FloatBuffer fragmentBuffer;

    private int program;
    private int vPosition;
    private int fPosition;
    private int vboId;
    private int fboId;

    private int fboTextureid;


    //实际渲染的大小
    private int width;
    private int height;

    private SurfaceTexture surfaceTexture;

    private int imgTextureId;//图像的textureid
    private int srcImg = 0;

//    private XYImgFboRender xyImgFboRender;

    private OnRenderCreateListener onRenderCreateListener;


    public XYPlayVideoRender(Context context) {
        this.context = context;
//        xyImgFboRender = new XYImgFboRender(context);

        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexBuffer.position(0);

        fragmentBuffer = ByteBuffer.allocateDirect(fragmentData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(fragmentData);
        fragmentBuffer.position(0);


    }

    public void setOnRenderCreateListener(OnRenderCreateListener onRenderCreateListener) {
        this.onRenderCreateListener = onRenderCreateListener;
    }

    @Override
    public void onSurfaceCreated() {

        //加载shader
//        xyImgFboRender.onCreate();
        String vertexSource = XYShaderUtil.getRawResource(context, R.raw.vertex_shader_fbo);
        String fragmentSource = XYShaderUtil.getRawResource(context, R.raw.fragment_shader_fbo);

        program = XYShaderUtil.createProgram(vertexSource, fragmentSource);

        vPosition = GLES20.glGetAttribLocation(program, "v_Position");
        fPosition = GLES20.glGetAttribLocation(program, "f_Position");

    }

    @Override
    public void onSurfaceChanged(int width, int height) {

        //VBO
        int[] vbos = new int[1];
//        1、创建VBO
        GLES20.glGenBuffers(1, vbos, 0);
        vboId = vbos[0];
        //2.绑定VBO
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId);
//        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexData.length * 4, null, GLES20.GL_STATIC_DRAW);

        //3. 分配VBO需要的缓冲大小
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexData.length * 4 + fragmentData.length * 4, null, GLES20.GL_STATIC_DRAW);

        //4,为VBO设置顶点数据的值(这里想象内存区域 先偏移0用于存储顶点坐标,再偏移顶点坐标的大小,用于存储片元坐标) 大小是顶点坐标加上片元坐标大小
        GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, 0, vertexData.length * 4, vertexBuffer);
        GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, vertexData.length * 4, fragmentData.length * 4, fragmentBuffer);
//        5、解绑VBO
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);


        //fbo
        int[] fbos = new int[1];
        GLES20.glGenBuffers(1, fbos, 0);
        fboId = fbos[0];

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);


        int[] textureIds = new int[1];
        GLES20.glGenTextures(1, textureIds, 0);
        fboTextureid = textureIds[0];

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fboTextureid);
//        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
//        GLES20.glUniform1i(sampler, 0);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, fboTextureid, 0);//把纹理绑定到FBO上

        if (GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER) != GLES20.GL_FRAMEBUFFER_COMPLETE) {
            LogUtil.e("fbo wrong");
        } else {
            LogUtil.e("fbo success");
        }


        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        if (onRenderCreateListener != null) {
            onRenderCreateListener.onCreate(fboTextureid);
        }

        GLES20.glViewport(0, 0, width, height);
//        xyImgFboRender.onChange(width, height);


    }

    @Override
    public void onDrawFrame() {

        imgTextureId = XYShaderUtil.loadTexrute(srcImg, context);
        LogUtil.d("id is : " + imgTextureId);
//        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0f, 1f, 0f, 1f);

        GLES20.glUseProgram(program);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, imgTextureId);


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

        int[] ids = new int[]{imgTextureId};
        GLES20.glDeleteTextures(1, ids, 0);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
//        wlImgFboRender.onDraw(textureid);
//        xyImgFboRender.onDraw(fboTextureid);

    }

    public interface OnRenderCreateListener {
        void onCreate(int textureId);
    }

    public void setCurrentImgSrc(int src) {
        LogUtil.d("src = " + src);
        srcImg = src;
//        imgTextureId = WlShaderUtil.loadTexrute(src, context);
    }
}
