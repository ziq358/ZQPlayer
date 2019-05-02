package com.zq.playerlib.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import com.zq.playerlib.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class FullViewRenderer implements GLSurfaceView.Renderer {
    private int surfaceWidth,surfaceHeight;

    private int imageTextureId;
    private FloatBuffer mVerticesBuffer;
    private FloatBuffer mTexCoordinateBuffer;
    private final float TRIANGLES_DATA_CW[] = {
            -1.0f, -1.0f, 0f, //LD
            -1.0f, 1.0f, 0f,  //LU
            1.0f, -1.0f, 0f,  //RD
            1.0f, 1.0f, 0f    //RU
    };

    public final float TEXTURE_NO_ROTATION[] = {
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
    };

    protected float[] projectionMatrix = new float[16];


    private int mProgramId;
    private int mPositionHandle;
    private int mTextureCoordinateHandle;
    private int uMVPMatrixHandle;
    private int sTextureSamplerHandle;




    Context context;
    public FullViewRenderer(Context context) {
        this.context = context;

        mVerticesBuffer = ByteBuffer.allocateDirect(
                TRIANGLES_DATA_CW.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(TRIANGLES_DATA_CW);
        mVerticesBuffer.position(0);

        mTexCoordinateBuffer = ByteBuffer.allocateDirect(
                TEXTURE_NO_ROTATION.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(TEXTURE_NO_ROTATION);
        mTexCoordinateBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        mProgramId = OpenGlUtil.createProgram(context, R.raw.vertexshader, R.raw.fragmentshader);
        if (mProgramId == 0) {
            return;
        }
        mPositionHandle = GLES20.glGetAttribLocation(mProgramId, "position");
        OpenGlUtil.checkGlError("glGetAttribLocation position");
        if (mPositionHandle == -1) {
            throw new RuntimeException("Could not get attrib location for position");
        }
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramId, "texcoord");
        OpenGlUtil.checkGlError("glGetAttribLocation aTextureCoord");
        if (mTextureCoordinateHandle == -1) {
            throw new RuntimeException("Could not get attrib location for texcoord");
        }

        sTextureSamplerHandle= GLES20.glGetUniformLocation(mProgramId,"s_texture");
        OpenGlUtil.checkGlError("glGetUniformLocation uniform s_texture");

        uMVPMatrixHandle= GLES20.glGetUniformLocation(mProgramId,"uMVPMatrix");
        OpenGlUtil.checkGlError("glGetUniformLocation uMVPMatrix");
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int surfaceWidth, int surfaceHeight) {
        this.surfaceWidth = surfaceWidth;
        this.surfaceHeight = surfaceHeight;
    }

    @Override
    public void onDrawFrame(GL10 gl10){
        Log.e("ziq", "onDrawFrame: ");
        onDrawFrame();
    }

    Bitmap bitmap;
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void onDrawFrame() {
        if(imageTextureId == 0){
            int[] textureObjectIds=new int[1];
            GLES20.glGenTextures(1,textureObjectIds,0);
            if (textureObjectIds[0]!=0){
                imageTextureId = textureObjectIds[0];
            }

        }

        if (imageTextureId == 0) {
            return;
        }

        if(bitmap != null){
            //对imageTextureId 进行配置
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, imageTextureId);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmap,0);
            bitmap.recycle();
            bitmap = null;
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        }


        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(mProgramId);
        Matrix.setIdentityM(projectionMatrix,0);

        GLES20.glViewport(0,0,surfaceWidth,surfaceHeight);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, imageTextureId);
        GLES20.glUniform1i(sTextureSamplerHandle, 0);

        mTexCoordinateBuffer.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, 2, GLES20.GL_FLOAT, false, 0, mTexCoordinateBuffer);
        OpenGlUtil.checkGlError("glVertexAttribPointer mTextureCoordinateHandle");
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        OpenGlUtil.checkGlError("glEnableVertexAttribArray mTextureCoordinateHandle");

        mVerticesBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, mVerticesBuffer);
        OpenGlUtil.checkGlError("glVertexAttribPointer mPositionHandle");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        OpenGlUtil.checkGlError("glEnableVertexAttribArray mPositionHandle");

        Matrix.setIdentityM(projectionMatrix,0);
        GLES20.glUniformMatrix4fv(uMVPMatrixHandle, 1, false, projectionMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisable(GLES20.GL_BLEND);
    }

    public void onDestry(){
        GLES20.glDeleteProgram(mProgramId);
    }
}