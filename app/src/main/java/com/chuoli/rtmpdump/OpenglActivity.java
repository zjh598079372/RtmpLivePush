package com.chuoli.rtmpdump;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.chuoli.rtmpdump.widget.MyGLSurfaceView;

public class OpenglActivity extends AppCompatActivity {

    private MyGLSurfaceView mGlSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opengl);

        mGlSurfaceView = findViewById(R.id.glSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGlSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGlSurfaceView.onPause();
    }
}
