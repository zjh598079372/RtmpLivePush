package com.chuoli.rtmpdump.render;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 带相机的三角形
 */
public class Square extends Shader {

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
    private FloatBuffer colorBuffer;
    private IntBuffer indexBuffer;

    float[] triangleCoords = {
            -0.5f,0.5f,0.0f,  //leftTop 0
            0.5f, 0.5f, 0.0f,  //rightTop 1
            -0.5f, -0.5f, 0.0f, //leftBottom 2
            0.5f, -0.5f, 0.0f  //rightBottom 3
    };

    int[] index = {
            0,2,3,
            0,3,1
    };

    float[] color = {
            1.0f,0.0f,0.0f,1.0f

    };
    private int mPositionHandle;
    private int mColorHandle;
    private int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    /*****透视矩阵******/
    private float[] mPerspectiveMatrix = new float[16];
    private float[] mCameraMatrix = new float[16];
    private float[] mResultMatrix = new float[16];
    private int mMatrixHandler;
    private int count = 0;


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        //准备顶点数据
        floatBuffer = ByteBuffer.allocateDirect(triangleCoords.length * 4).
                order(ByteOrder.nativeOrder()).
                asFloatBuffer();
        floatBuffer.put(triangleCoords);
        floatBuffer.position(0);

        //准备颜色数据
        colorBuffer = ByteBuffer.allocateDirect(color.length*4).
                order(ByteOrder.nativeOrder()).
                asFloatBuffer();
        colorBuffer.put(color);
        colorBuffer.position(0);


        //准备画正方形的数据
        indexBuffer = ByteBuffer.allocateDirect(index.length*4).
                order(ByteOrder.nativeOrder()).
                asIntBuffer();
        indexBuffer.put(index);
        indexBuffer.position(0);


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
        count++;
        Log.e("onDrawFrame-->","count=="+count);
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

        //获取颜色的句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(mColorHandle,1,color,0);

        //
        /**
         * 绘制正方形
         * 数据类型一定要传无符号的 GLES20.GL_UNSIGNED_INT
         * 传GLES20.GL_INT 这个会画不出来
         *
         */

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.length,GLES20.GL_UNSIGNED_INT,indexBuffer);
        //禁止顶点数组
        GLES20.glDisableVertexAttribArray(mPositionHandle);



    }


}
