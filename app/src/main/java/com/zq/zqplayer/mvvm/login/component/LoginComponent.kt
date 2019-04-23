package com.zq.zqplayer.mvvm.login.component

import com.ziq.base.baserx.dagger.ActivityScope
import com.ziq.base.baserx.dagger.component.AppComponent
import com.zq.zqplayer.mvvm.login.LoginActivity
import dagger.Component

/**
 *author: wuyanqiang
 *2018/11/20
 */

@ActivityScope
@Component(modules = [LoginModule::class], dependencies = [AppComponent::class])
interface LoginComponent {
    fun inject(loginActivity: LoginActivity)
}