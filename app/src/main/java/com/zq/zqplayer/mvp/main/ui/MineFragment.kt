package com.zq.zqplayer.mvp.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import butterknife.OnClick
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.mvp.IBasePresenter
import com.ziq.base.mvp.MvpBaseFragment
import com.zq.zqplayer.R
import com.zq.zqplayer.mvvm.login.LoginActivity
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

    @OnClick(R.id.tv_logout)
    fun onclick(view: View): Unit {
        when(view.id){
            R.id.tv_logout -> {
                UserInfoUtil.cleanSP()
                startActivity(Intent(activity, LoginActivity::class.java))
                activity?.finish()
            }
            else -> {}
        }
    }

}