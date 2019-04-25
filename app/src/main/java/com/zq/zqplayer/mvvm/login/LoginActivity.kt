package com.zq.zqplayer.mvvm.login

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.mvvm.MvvmBaseActivity
import com.zq.zqplayer.R
import com.zq.zqplayer.databinding.ActivityLoginBinding
import com.zq.zqplayer.mvvm.login.component.DaggerLoginComponent
import com.zq.zqplayer.mvvm.login.component.LoginModule
import javax.inject.Inject

class LoginActivity : MvvmBaseActivity() {
    @Inject lateinit var loginViewModel : ILoginViewModel

    var activityLoginBinding:ActivityLoginBinding? = null

    override fun initView(savedInstanceState: Bundle?) {
        activityLoginBinding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
    }

    override fun initForInject(appComponent: AppComponent?) {
        DaggerLoginComponent.builder()
                .appComponent(appComponent)
                .loginModule(LoginModule(this))
                .build().inject(this)
    }

    override fun initData(savedInstanceState: Bundle?) {
        activityLoginBinding?.viewmodel = loginViewModel



        Log.d("ziq", "initData %d  "+ loginViewModel )
    }
}