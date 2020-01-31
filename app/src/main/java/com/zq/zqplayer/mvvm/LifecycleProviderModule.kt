package com.zq.zqplayer.mvvm;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.trello.rxlifecycle3.android.ActivityEvent;

import dagger.Module;
import dagger.Provides;

@Module
class LifecycleProviderModule {

    private var lifecycleProvider: LifecycleProvider<*>

    constructor(lifecycleProvider: LifecycleProvider<*>) {
        this.lifecycleProvider = lifecycleProvider
    }

    @Provides
    fun getLifecycleProvider(): LifecycleProvider<*>{
        return lifecycleProvider
    }

}