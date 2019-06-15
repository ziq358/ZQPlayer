package com.zq.playerlib.service

import android.app.Service
import android.content.*
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.WindowManager.LayoutParams.*
import android.widget.Toast
import com.zq.playerlib.R
import com.zq.playerlib.ZQPlayer
import java.lang.ref.WeakReference

/**
 *@author wuyanqiang
 *@date 2019/1/16
 */
class ZQPlayerService: Service() {


    companion object {
        fun startZQPlayerService(context: Context) {
            val intent = Intent(context, ZQPlayerService::class.java)
            context.startService(intent)
        }

        fun stopZQPlayerService(context: Context) {
            val intent = Intent(context, ZQPlayerService::class.java)
            context.stopService(intent)
        }

        fun bindZQPlayerService(context: Context, serviceConnection: ServiceConnection) {
            //需要先启动服务 start service first，不然直接绑定不会立即回调onServiceConnected， 待 服务start后，统一回调所有之前 进行的绑定动作
            val intent = Intent(context, ZQPlayerService::class.java)
            context.bindService(intent, serviceConnection, 0)
        }

        fun unbindZQPlayerService(context: Context, serviceConnection: ServiceConnection) {
            //activity 绑定的时候  把ServiceConnection 给了 服务，serviceConnection 强引用了activity, 退出activity时 不解绑会 内存泄漏
            //不会调用  ServiceConnection.onServiceDisconnected  只有服务crash 或被kill 才调用
            context.unbindService(serviceConnection)
        }

        val TAG = "ZQPlayerService"

        val PLAY_CMD = "com.zq.playerlib.service.zqplayerservice.play"
        val PAUSE_CMD = "com.zq.playerlib.service.zqplayerservice.pause"
        val STOP_CMD = "com.zq.playerlib.service.zqplayerservice.stop"

    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        val intentFilter = IntentFilter()
        intentFilter.addAction(PLAY_CMD)
        intentFilter.addAction(PAUSE_CMD)
        intentFilter.addAction(STOP_CMD)
        registerReceiver(mBroadcastReceiver, intentFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        handleIntent(intent)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        unregisterReceiver(mBroadcastReceiver)
        stop()
        super.onDestroy()
    }

    //匿名内部类
    private var mBroadcastReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            handleIntent(intent)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return ZQPlayerBind(this)
    }

    class ZQPlayerBind() : ZQPlayerServiceBinder.Stub() {
        override fun isPlaying(): Boolean {
            return mService?.get()?.isPlaying()!!
        }

        override fun initPlayer(info: PlayerItemInfo?, surface: Surface?, listener: StatusListener?) {
            mService?.get()?.initPlayer(info, surface, listener)
        }

        override fun showFloatingWindow() {
            mService?.get()?.showFloatingWindow()
        }

        private var mService: WeakReference<ZQPlayerService>? = null

        constructor(service: ZQPlayerService) :this(){
            mService = WeakReference(service)
        }

    }

    // 功能
    fun handleIntent(intent: Intent?): Unit {
        val action = intent?.action
        when(action){
            PLAY_CMD -> {zqPlayer?.play()}
            PAUSE_CMD -> {zqPlayer?.pause()}
            STOP_CMD -> stop()
            else ->{
            }
        }
    }

    fun stop(): Unit {
        zqPlayer?.stop()
        if(floatingWindow != null){
            windowManager?.removeView(floatingWindow)
            floatingWindow = null
        }
    }

    var zqPlayer:ZQPlayer? = null
    var playerItemInfo: PlayerItemInfo? = null

    fun initPlayer(info: PlayerItemInfo?, surface: Surface?, listener: StatusListener?): Unit {
        if(zqPlayer != null){
            zqPlayer?.stop()
        }
        Log.d(TAG, info?.url)
        playerItemInfo = info
        zqPlayer = ZQPlayer()
        zqPlayer?.setStatusListener(object : StatusListener.Stub() {
            override fun onLoading() {
                listener?.onLoading()
            }

            override fun onPrepareFinished() {
                listener?.onPrepareFinished()
            }

            override fun onPlaying() {
                listener?.onPlaying()
            }

            override fun onPause() {
                listener?.onPause()
            }

            override fun onStop() {
                listener?.onStop()
            }

            override fun onError(msg: String?) {
                listener?.onError(msg)
            }
        })
        zqPlayer?.init(playerItemInfo?.url!!)
        zqPlayer?.setSurfaceTarget(surface)
    }

    fun isPlaying() : Boolean {
        if(zqPlayer != null){
            return zqPlayer!!.isPlaying()
        }
        return false
    }


    internal var floatingWindow: View? = null
    internal var windowManager: WindowManager? = null
    internal var wmParams: WindowManager.LayoutParams? = null
    internal var displayHeight: Int = 0
    internal var displayWidth: Int = 0
    internal var statusBarHeight: Int = 0
    internal var floatingWindowHeight: Int = 0
    internal var floatingWindowWidth: Int = 0
    internal var surfaceView: SurfaceView? = null
    fun showFloatingWindow() {

        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "当前无权限使用悬浮窗，请授权！", Toast.LENGTH_SHORT).show()
            stop()
            return
        }

        if(windowManager == null){
            displayHeight = DeviceInfoUtil.getDisplayHeight(applicationContext)
            displayWidth = DeviceInfoUtil.getDisplayWidth(applicationContext)
            statusBarHeight = DeviceInfoUtil.getStatusBarHeight(applicationContext)

            windowManager = applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wmParams = WindowManager.LayoutParams()
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1 && Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//                wmParams?.type = WindowManager.LayoutParams.TYPE_TOAST
//            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1){
//                wmParams?.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
//            }else{
//                wmParams?.type = WindowManager.LayoutParams.TYPE_PHONE
//            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1 && Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                wmParams!!.type = WindowManager.LayoutParams.TYPE_TOAST
            } else {
                wmParams!!.type = WindowManager.LayoutParams.TYPE_PHONE
            }

            wmParams?.flags = FLAG_NOT_TOUCH_MODAL or FLAG_NOT_FOCUSABLE or FLAG_LAYOUT_NO_LIMITS

            wmParams?.gravity = Gravity.LEFT or Gravity.TOP
            wmParams?.width = displayWidth / 20 * 12
            wmParams?.height = wmParams!!.width /16 * 9
        }

        if(floatingWindow != null){
            surfaceView?.visibility = View.INVISIBLE
            surfaceView?.visibility = View.VISIBLE
        }else {
            val inflater = LayoutInflater.from(this)
            floatingWindow = inflater.inflate(R.layout.layout_service_video, null)
            surfaceView = floatingWindow?.findViewById(R.id.surfaceView)
            val ivClose: View? = floatingWindow?.findViewById(R.id.iv_close)
            floatingWindow?.setOnTouchListener(View.OnTouchListener { v, event ->
                // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标

                if (floatingWindowHeight == 0) {
                    floatingWindowWidth = floatingWindow!!.getMeasuredWidth()
                    floatingWindowHeight = floatingWindow!!.getMeasuredHeight()
                }

                var targetX = event.rawX.toInt() - floatingWindowWidth / 2
                var targetY = event.rawY.toInt() - floatingWindowHeight / 2 - statusBarHeight

                if (targetX <= 0) {
                    targetX = 0
                } else if (targetX + floatingWindowWidth >= displayWidth) {
                    targetX = displayWidth - floatingWindowWidth
                }
                if (targetY <= 0) {
                    targetY = 0
                } else if (targetY + floatingWindowHeight >= displayHeight) {
                    targetY = displayHeight - floatingWindowHeight - statusBarHeight
                }

                wmParams?.x = targetX
                wmParams?.y = targetY
                windowManager?.updateViewLayout(floatingWindow, wmParams)// 刷新
                return@OnTouchListener false// 此处必须返回false，否则OnClickListener获取不到监听
            })
            ivClose?.setOnClickListener { stop() }


            windowManager?.addView(floatingWindow, wmParams)
            surfaceView?.holder?.addCallback(object : SurfaceHolder.Callback {

                override fun surfaceCreated(holder: SurfaceHolder?) {
                    zqPlayer?.setSurfaceTarget(holder?.surface)
                }

                override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                }

                override fun surfaceDestroyed(holder: SurfaceHolder?) {

                }
            })
        }
    }


}