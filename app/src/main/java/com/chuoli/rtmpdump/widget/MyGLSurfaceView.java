package com.chuoli.rtmpdump.widget;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.chuoli.rtmpdump.render.Shader;
import com.chuoli.rtmpdump.render.Triangle;

public class MyGLSurfaceView extends GLSurfaceView {
    Shader mShader;
    public MyGLSurfaceView(Context context) {
        this(context,null);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
        setRenderer(mShader = new Triangle());
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }
}
