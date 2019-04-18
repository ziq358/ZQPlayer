package com.zq.zqplayer.dagger.component

import com.ziq.base.mvp.dagger.component.AppComponent
import com.ziq.base.mvp.dagger.module.LifecycleProviderModule
import com.zq.zqplayer.dagger.module.RecommendModule
import com.zq.zqplayer.fragment.topnavigation.RecommendFragment
import dagger.Component

/**
 *author: wuyanqiang
 *2018/11/20
 */

@Component(modules = [RecommendModule::class, LifecycleProviderModule::class], dependencies = [AppComponent::class])
interface RecommendComponent {
    fun inject(fragment: RecommendFragment)
}