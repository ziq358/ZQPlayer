package com.zq.zqplayer.mvp.main.dagger.component

import com.ziq.base.baserx.dagger.ActivityScope
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.baserx.dagger.module.LifecycleProviderModule
import com.zq.zqplayer.mvp.main.dagger.module.RecommendModule
import com.zq.zqplayer.mvp.main.ui.RecommendFragment
import dagger.Component

/**
 *author: wuyanqiang
 *2018/11/20
 */

@ActivityScope
@Component(modules = [RecommendModule::class, LifecycleProviderModule::class], dependencies = [AppComponent::class])
interface RecommendComponent {
    fun inject(fragment: RecommendFragment)
}