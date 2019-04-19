package com.zq.zqplayer.mvp.main.dagger.module

import com.zq.zqplayer.mvp.main.contract.RecommendContract
import dagger.Module
import dagger.Provides

/**
 *author: wuyanqiang
 *2018/11/20
 */
@Module
class RecommendModule {
    var view: RecommendContract.View

    constructor(view: RecommendContract.View) {
        this.view = view
    }

    @Provides fun getview(): RecommendContract.View {
        return view;
    }

}