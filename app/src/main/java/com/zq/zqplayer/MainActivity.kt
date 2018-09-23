package com.zq.zqplayer

import android.os.Bundle
import android.widget.TextView
import butterknife.BindView
import com.ziq.base.mvp.BaseActivity
import com.ziq.base.mvp.IBasePresenter

class MainActivity : BaseActivity<IBasePresenter>() {

    init {
        System.loadLibrary("native-lib")
    }

//    companion object {
//
//        // Used to load the 'native-lib' library on application startup.
//        init {
//            System.loadLibrary("native-lib")
//        }
//    }

    @BindView(R.id.content)
    lateinit var mContent: TextView

    override fun initLayoutResourceId(): Int {
        return R.layout.activity_main
    }

    override fun initData(savedInstanceState: Bundle?) {
        mContent.text = stringFromJNI()
    }

    external fun stringFromJNI(): String


}
