package com.zq.zqplayer.mvvm.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.mvvm.MvvmBaseActivity
import com.zq.zqplayer.MainActivity
import com.zq.zqplayer.R
import com.zq.zqplayer.bean.UserInfoBean
import com.zq.zqplayer.databinding.ActivityLoginBinding
import com.zq.zqplayer.mvvm.login.component.DaggerLoginComponent
import com.zq.zqplayer.mvvm.LifecycleProviderModule
import com.zq.zqplayer.mvvm.login.component.LoginModule
import com.zq.zqplayer.mvvm.register.RegisterActivity
import com.zq.zqplayer.util.UserInfoUtil
import javax.inject.Inject

class LoginActivity : MvvmBaseActivity() {
    @Inject lateinit var loginViewModel : ILoginViewModel

    private var activityLoginBinding:ActivityLoginBinding? = null

    override fun initView(savedInstanceState: Bundle?) {
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
    }

    override fun initForInject(appComponent: AppComponent?) {
        DaggerLoginComponent.builder()
                .appComponent(appComponent)
                .loginModule(LoginModule(this))
                .lifecycleProviderModule(LifecycleProviderModule(this))
                .build().inject(this)
    }

    override fun initData(savedInstanceState: Bundle?) {
        activityLoginBinding?.viewmodel = loginViewModel
        activityLoginBinding?.lifecycleOwner = this // 设置了这个， livedata 的变化才会应用到界面
        loginViewModel.toastMsg.observe(this, object : Observer<String> {
            override fun onChanged(t: String?) {
                Toast.makeText(this@LoginActivity, t, Toast.LENGTH_LONG).show()
            }
        })

        loginViewModel.isLoading.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                if(t!!){
                    showLoading()
                }else{
                    hideLoading()
                }
            }
        })

        loginViewModel.userInfo.observe(this, object : Observer<UserInfoBean> {
            override fun onChanged(t: UserInfoBean?) {
                UserInfoUtil.saveToSP(t!!)
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        })

        loginViewModel.isGotoRegisterActivity.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                if(t!!){
                    startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                }
            }

        })

        if(UserInfoUtil.getUserInfo() != null){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

}