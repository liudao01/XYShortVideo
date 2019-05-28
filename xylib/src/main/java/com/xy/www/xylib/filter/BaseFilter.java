package com.xy.www.xylib.filter;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;

import com.xy.www.xylib.R;
import com.xy.www.xylib.util.XYShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * @author liuml
 * @explain
 * @time 2019/2/26 11:01
 */
public class BaseFilter {

    private Context context;
    private FloatBuffer vertexBuffer;
    private FloatBuffer fragmentBuffer;
    //顶点坐标
    private float[] vertexData = {
            -1f, -1f,
            1f, -1f,
            -1f, 1f,
            1f, 1f

    };

    //片元坐标
    private float[] fragmentData = {
            0f, 1f,
            1f, 1f,
            0f, 0f,
            1f, 0f
    };

    private int vertexShader;
    private int fragmentShader;
    private int program = 0;
    private int vPosition;
    private int fPosition;
    private int sTexture;
    private int umatrix;

    private SurfaceTexture surfaceTexture;
    /**
     * 纹理坐标中每个点占的向量个数
     */
    private int TEX_VERTEX_COMPONENT_COUNT = 2;

    /**
     * @param context
     * @param vertexShader   顶点着色器文件
     * @param fragmentShader 偏远着色器文件
     */
    public BaseFilter(Context context, int vertexShader, int fragmentShader) {
        this.context = context;
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
        init();
    }

    private void init() {
        //TODO BufferUtil 工具类
        //创建浮点型buffer 创建一个FloatBuffer
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


    /**
     * 创建
     */
    public void onCreate() {
        if (vertexShader == -1 || vertexShader == 0) {
            //使用默认的顶点着色器
            vertexShader = R.raw.vertex_shader;
        }

        if (fragmentShader == -1 || fragmentShader == 0) {
            //使用默认的片元
            fragmentShader = R.raw.fragment_shader;
        }
        //读取glse文件
        String vertexSource = XYShaderUtil.getRawResource(context, vertexShader);
        String fragmentSource = XYShaderUtil.getRawResource(context, fragmentShader);

        //创建OpenGL程序对象 并链接program
        makeProgram(vertexSource, fragmentSource);

        //顶点坐标索引
        vPosition = getAttrib("v_Position");
        //纹理坐标索引
        fPosition = getAttrib("f_Position");
        //Uniform 类似常量
        //顶点坐标系内的 用于坐标旋转的
        umatrix = getUniform("u_Matrix");
        //纹理坐标内    扩展纹理 samplerExternalOES
        sTexture = getUniform("sTexture");

        //顶点相关. 指定了渲染时索引值为 index 的顶点属性数组的数据格式和位置。
        GLES20.glVertexAttribPointer(vPosition, TEX_VERTEX_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        //允许顶点着色器读取GPU（服务器端）数据
        GLES20.glEnableVertexAttribArray(vPosition);

        //纹理相关. 指定了渲染时索引值为 index 的顶点属性数组的数据格式和位置。
        GLES20.glVertexAttribPointer(fPosition, TEX_VERTEX_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, fragmentBuffer);
        //允许纹理着色器读取GPU（服务器端）数据
        GLES20.glEnableVertexAttribArray(fPosition);


        GLES20.glClearColor(0f, 0f, 0f, 1f);
        // 开启纹理透明混合，这样才能绘制透明图片
        GLES20.glEnable(GL10.GL_BLEND);
        GLES20.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

    }

    public int getUniform(String name) {
        return GLES20.glGetUniformLocation(program, name);
    }

    public int getAttrib(String name) {
        return GLES20.glGetAttribLocation(program, name);
    }


    public void onSizeChanged(int width, int height) {
        GLES20.glViewport(0, 0, width, height);

    }

    public void  createOESTextureId(){

    }
    public void onDraw() {

    }

    /**
     * 创建OpenGL程序对象 并链接program
     *
     * @param vertexShader   顶点着色器代码
     * @param fragmentShader 片段着色器代码
     */
    protected void makeProgram(String vertexShader, String fragmentShader) {
        program = XYShaderUtil.createProgram(vertexShader, fragmentShader);
        // 步骤4：通知OpenGL开始使用该程序
        GLES20.glUseProgram(program);

    }


}
