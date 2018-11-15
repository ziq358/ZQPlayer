package com.zq.zqplayer.activity

import android.os.Bundle
import com.ziq.base.mvp.BaseActivity
import com.ziq.base.mvp.IBasePresenter
import com.zq.zqplayer.R

/**
 *author: wuyanqiang
 *2018/11/15
 */
class LiveActivity : BaseActivity<IBasePresenter>(){

    override fun initLayoutResourceId(): Int {
        return R.layout.activity_live
    }

    override fun initData(savedInstanceState: Bundle?) {

    }


}