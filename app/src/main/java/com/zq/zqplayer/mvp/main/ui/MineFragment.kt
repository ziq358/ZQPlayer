package com.zq.zqplayer.mvp.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.mvp.IBasePresenter
import com.ziq.base.mvp.MvpBaseFragment
import com.zq.zqplayer.R
import com.zq.zqplayer.bean.UserInfoBean
import com.zq.zqplayer.mvp.live.ui.PushTheFlowActivity
import com.zq.zqplayer.mvvm.login.LoginActivity
import com.zq.zqplayer.test.TestListActivity
import com.zq.zqplayer.util.UserInfoUtil

/**
 * @author wuyanqiang
 * @date 2018/10/15
 */
class MineFragment : MvpBaseFragment<IBasePresenter>() {

    override fun initLayoutResourceId(): Int {
        return R.layout.fragment_mine
    }
    override fun initForInject(appComponent: AppComponent?) {
    }
    override fun initData(view: View, savedInstanceState: Bundle?) {

    }

    override fun onResume() {
        super.onResume()
    }

    @OnClick(R.id.floatingBtn)
    fun onclick(view: View): Unit {
        when(view.id){
            R.id.floatingBtn -> {
                startActivity(Intent(activity, TestListActivity::class.java))
            }
            else -> {}
        }
    }



}