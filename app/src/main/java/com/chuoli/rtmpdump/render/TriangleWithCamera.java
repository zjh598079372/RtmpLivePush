package com.chuoli.rtmpdump.render;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 带相机的三角形
 */
public class TriangleWithCamera extends Shader {

    private final int COORDS_PER_VERTEX = 3;
    private final int BYTES_PER_FLOAT = 4;
    private final int VERTEX_STRIDE = COORDS_PER_VERTEX * BYTES_PER_FLOAT;
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 vMatrix;"+
                    "void main() {" +
                    "gl_Position = vMatrix*vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "gl_FragColor = vColor;" +
                    "}";

    private int mProgram;
    private FloatBuffer floatBuffer;

    float[] triangleCoords = {
            0.5f, 0.5f, 0.0f,  //top
            -0.5f, -0.5f, 0.0f, //leftBottom
            0.5f, -0.5f, 0.0f  //rightBottom
    };
    private int mPositionHandle;
    private int mFragmentHandle;
    private float[] color = {1.0f, 1.0f, 1.0f, 1.0f};
    private int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    /*****透视矩阵******/
    private float[] mPerspectiveMatrix = new float[16];
    private float[] mCameraMatrix = new float[16];
    private float[] mResultMatrix = new float[16];
    private int mMatrixHandler;


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        //准备顶点数据
        floatBuffer = ByteBuffer.allocateDirect(triangleCoords.length * 4).
                order(ByteOrder.nativeOrder()).
                asFloatBuffer();
        floatBuffer.put(triangleCoords);
        floatBuffer.position(0);

        //创建顶点着色器
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        //创建片元着色器
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        //生成一个程序
        mProgram = GLES20.glCreateProgram();
        //为程序添加顶点着色器
        GLES20.glAttachShader(mProgram, vertexShader);
        //为程序添加片元着色器
        GLES20.glAttachShader(mProgram, fragmentShader);
        //链接程序
        GLES20.glLinkProgram(mProgram);
        //


    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
//        GLES20.glViewport(0,0,width,height);
        //求宽高比
        float ratio = (float) width / height;
        //设置透视投影
        Matrix.frustumM(mPerspectiveMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        //设置相机位置
        Matrix.setLookAtM(mCameraMatrix,0,0,0,7,0,0,0,0,1,0);
        //计算变幻矩阵
        Matrix.multiplyMM(mResultMatrix,0,mPerspectiveMatrix,0,mCameraMatrix,0);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glUseProgram(mProgram);

        mMatrixHandler= GLES20.glGetUniformLocation(mProgram,"vMatrix");
        //指定vMatrix的值
        GLES20.glUniformMatrix4fv(mMatrixHandler,1,false,mResultMatrix,0);

        //获取顶点的句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //起用句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //向句柄提交顶点数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, floatBuffer);

        //获取片元的句柄
        mFragmentHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mFragmentHandle, 1, color, 0);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        //禁止顶点数组
        GLES20.glDisableVertexAttribArray(mPositionHandle);


    }


}
