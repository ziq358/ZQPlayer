package com.zq.zqplayer.mvvm.recommend.component

import com.ziq.base.baserx.dagger.ActivityScope
import com.ziq.base.baserx.dagger.component.AppComponent
import com.zq.zqplayer.mvvm.login.LoginActivity
import com.zq.zqplayer.mvvm.LifecycleProviderModule
import com.zq.zqplayer.mvvm.recommend.NewRecommendFragment
import dagger.Component

/**
 *author: wuyanqiang
 *2018/11/20
 */

@ActivityScope
@Component(modules = [NewRecommendModule::class, LifecycleProviderModule::class], dependencies = [AppComponent::class])
interface NewRecommendComponent {
    fun inject(newRecommendFragment: NewRecommendFragment)
}