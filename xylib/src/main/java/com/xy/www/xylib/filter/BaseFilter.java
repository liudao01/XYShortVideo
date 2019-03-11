package com.xy.www.xylib.filter;

import android.content.Context;
import android.text.TextUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

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

    private final String vertexShader;
    private final String fragmentShader;

    public BaseFilter(Context context, String vertexShader, String fragmentShader) {
        this.context = context;
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
        init();
    }

    private void init() {
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


    private void onCreate(){
        if (TextUtils.isEmpty(vertexShader)) {
            //使用默认的顶点着色器

        }

        if (TextUtils.isEmpty(fragmentShader)) {
            //使用默认的片元

        }
    }


    /**
     * 创建OpenGL程序对象
     *
     * @param vertexShader   顶点着色器代码
     * @param fragmentShader 片段着色器代码
     */
    protected void makeProgram(String vertexShader, String fragmentShader) {
        // 步骤1：编译顶点着色器
//        val vertexShaderId = ShaderHelper.compileVertexShader(vertexShader)
        // 步骤2：编译片段着色器
//        val fragmentShaderId = ShaderHelper.compileFragmentShader(fragmentShader)
        // 步骤3：将顶点着色器、片段着色器进行链接，组装成一个OpenGL程序
//        program = ShaderHelper.linkProgram(vertexShaderId, fragmentShaderId)

//        if (LoggerConfig.ON) {
//            ShaderHelper.validateProgram(program)
//        }

        // 步骤4：通知OpenGL开始使用该程序
//        GLES20.glUseProgram(program);
    }


}
