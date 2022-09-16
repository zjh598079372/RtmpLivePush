package com.chuoli.rtmpdump;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chuoli.rtmpdump.livePush.RtmpPush;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("rtmp-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
        final RtmpPush rtmpPush = new RtmpPush();
        rtmpPush.setResultCallback(new RtmpPush.ResultCallback() {
            @Override
            public void onSuccess() {
                Log.e("回调-->","成功");
            }

            @Override
            public void onError(int errorCode, String msg) {
                Log.e("回调-->",msg+"-->"+errorCode);
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rtmpPush.nInitConnect("131");
            }
        });


        TextView openglTV = findViewById(R.id.openglTV);
        openglTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,OpenglActivity.class));
            }
        });


    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

}
