package com.zq.zqplayer.mvvm.login

import android.os.Bundle
import android.util.Log
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.mvvm.MvvmBaseActivity
import com.zq.zqplayer.R
import com.zq.zqplayer.mvvm.login.component.DaggerLoginComponent
import com.zq.zqplayer.mvvm.login.component.LoginModule
import javax.inject.Inject

class LoginActivity : MvvmBaseActivity() {

    @Inject lateinit var loginViewModel : ILoginViewModel

    override fun initForInject(appComponent: AppComponent?) {
        DaggerLoginComponent.builder()
                .appComponent(appComponent)
                .loginModule(LoginModule(this))
                .build().inject(this)
    }

    override fun initLayoutResourceId(): Int {
        return R.layout.activity_login
    }

    override fun initData(savedInstanceState: Bundle?) {
        Log.d("ziq", "initData %d  "+ loginViewModel )
    }
}