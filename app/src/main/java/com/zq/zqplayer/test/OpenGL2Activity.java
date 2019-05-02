package com.zq.zqplayer.test;

import android.app.Activity;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;

import com.zq.playerlib.opengl.FullViewRenderer;
import com.zq.playerlib.opengl.OpenGlUtil;
import com.zq.zqplayer.R;

public class OpenGL2Activity extends Activity {

    private static String TAG = "ziq";
    GLSurfaceView glSurfaceView;
    FullViewRenderer myRenderer;
    private String filePath="images/texture_360_n.jpg";
    private static final float sDensity =  Resources.getSystem().getDisplayMetrics().density;
    private static final float sDamping = 0.2f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_gl2);
        glSurfaceView = (GLSurfaceView) findViewById(R.id.glSurfaceView);
        glSurfaceView.setEGLContextClientVersion(2);
        myRenderer = new FullViewRenderer(this);
        glSurfaceView.setRenderer(myRenderer);
//        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myRenderer.setBitmap(OpenGlUtil.loadBitmapFromAssets(getBaseContext(), filePath));
                glSurfaceView.requestRender();
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        myRenderer.onDestry();
        super.onDestroy();
    }
}
