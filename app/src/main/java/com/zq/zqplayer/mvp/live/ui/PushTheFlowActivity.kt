package com.zq.zqplayer.mvp.live.ui

import android.os.Bundle
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.mvp.IBasePresenter
import com.ziq.base.mvp.MvpBaseActivity
import com.zq.zqplayer.R

class PushTheFlowActivity : MvpBaseActivity<IBasePresenter>(){


    override fun initLayoutResourceId(): Int {
        return R.layout.activity_push_the_flow
    }

    override fun initForInject(appComponent: AppComponent?) {}

    override fun initData(savedInstanceState: Bundle?) {

    }
}