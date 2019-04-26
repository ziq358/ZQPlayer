package com.zq.zqplayer.mvvm.register.component

import com.ziq.base.baserx.dagger.ActivityScope
import com.ziq.base.baserx.dagger.component.AppComponent
import com.zq.zqplayer.mvvm.login.LoginActivity
import com.zq.zqplayer.mvvm.LifecycleProviderModule
import com.zq.zqplayer.mvvm.register.RegisterActivity
import dagger.Component

/**
 *author: wuyanqiang
 *2018/11/20
 */

@ActivityScope
@Component(modules = [RegisterModule::class, LifecycleProviderModule::class], dependencies = [AppComponent::class])
interface RegisterComponent {
    fun inject(activity: RegisterActivity)
}