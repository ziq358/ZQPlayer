package com.zq.zqplayer.mvp.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.mvp.IBasePresenter
import com.ziq.base.mvp.MvpBaseFragment
import com.zq.zqplayer.R
import com.zq.zqplayer.bean.UserInfoBean
import com.zq.zqplayer.mvvm.login.LoginActivity
import com.zq.zqplayer.util.UserInfoUtil

/**
 * @author wuyanqiang
 * @date 2018/10/15
 */
class MineFragment : MvpBaseFragment<IBasePresenter>() {

    @BindView(R.id.tv_login_or_logout)
    lateinit var tv_login_or_logout: TextView

    override fun initLayoutResourceId(): Int {
        return R.layout.fragment_mine
    }
    override fun initForInject(appComponent: AppComponent?) {
    }
    override fun initData(view: View, savedInstanceState: Bundle?) {

    }

    override fun onResume() {
        super.onResume()
        val userInfoBean: UserInfoBean? = UserInfoUtil.getUserInfo()
        tv_login_or_logout.text = if (userInfoBean == null) "登录" else "退出登录"
    }

    @OnClick(R.id.tv_login_or_logout)
    fun onclick(view: View): Unit {
        when(view.id){
            R.id.tv_login_or_logout -> {
                val userInfoBean: UserInfoBean? = UserInfoUtil.getUserInfo()
                if (userInfoBean == null) {
                    startActivity(Intent(activity, LoginActivity::class.java))
                } else {
                    UserInfoUtil.cleanSP()
                    tv_login_or_logout.text = "登录"
                }
            }
            else -> {}
        }
    }



}