package com.zq.zqplayer.mvp.live.ui

import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Toast
import butterknife.BindView
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.mvp.IBasePresenter
import com.ziq.base.mvp.MvpBaseActivity
import com.ziq.base.utils.CameraUtils
import com.zq.playerlib.ZQVideoView
import com.zq.zqplayer.R

class PushTheFlowActivity : MvpBaseActivity<IBasePresenter>(), SurfaceHolder.Callback, Camera.PreviewCallback {


    @BindView(R.id.surfaceView)
    lateinit var mSurfaceView: SurfaceView

    override fun initLayoutResourceId(): Int {
        return R.layout.activity_push_the_flow
    }

    override fun initForInject(appComponent: AppComponent?) {}

    override fun initData(savedInstanceState: Bundle?) {
        mSurfaceView.holder.addCallback(this)
        mSurfaceView.setOnTouchListener { v, event ->
            try {
                CameraUtils.doFocus(event) { success, camera -> Toast.makeText(this@PushTheFlowActivity, "onAutoFocus:\n$success", Toast.LENGTH_LONG).show() }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            false
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        try {
            CameraUtils.openCamera(this@PushTheFlowActivity, false, mSurfaceView.getWidth(), mSurfaceView.getHeight())
            CameraUtils.setCameraDisplayOrientation(this@PushTheFlowActivity)
            CameraUtils.startPreviewDisplay(holder, this@PushTheFlowActivity)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        try {
            CameraUtils.stopPreview()
            CameraUtils.setCameraDisplayOrientation(this@PushTheFlowActivity)
            CameraUtils.startPreviewDisplay(holder, this@PushTheFlowActivity)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        try {
            CameraUtils.stopPreview()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPreviewFrame(data: ByteArray?, camera: Camera?) {
        //Android camera preview默认格式为NV21的, 要 转为 I420（即YUV标准格式4：2：0）再进行下一步
        Log.d("ziq", "onPreviewFrame: " + data?.size)
    }

    override fun onDestroy() {
        try {
            CameraUtils.stopPreview()
            CameraUtils.releaseCamera()
        } catch (e: Exception) {
        }
        super.onDestroy()
    }

}