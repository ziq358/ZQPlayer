package com.zq.zqplayer.fragment.live

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.ziq.base.dagger.component.AppComponent
import com.ziq.base.mvp.BaseFragment
import com.ziq.base.mvp.IBasePresenter
import com.zq.zqplayer.R

/**
 * @author wuyanqiang
 * @date 2018/10/15
 */
class VIPListFragment : BaseFragment<IBasePresenter>() {
    override fun initForInject(appComponent: AppComponent?) {
    }
    override fun initLayoutResourceId(): Int {
        return R.layout.fragment_vip_list
    }

    override fun initData(view: View, savedInstanceState: Bundle?) {

    }

}