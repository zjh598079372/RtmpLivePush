package com.chuoli.rtmpdump.render;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

public abstract class Shader implements GLSurfaceView.Renderer {


    protected int loadShader(int shaderType, String shaderCode){
//        int shader = GLES20.glCreateShader(shaderType);
//        GLES20.glShaderSource(shader,shaderCode);
//        GLES20.glCompileShader(shader);
//        return shader;

        //根据type创建顶点着色器或者片元着色器
        int shader = GLES20.glCreateShader(shaderType);
        //将资源加入到着色器中，并编译
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
