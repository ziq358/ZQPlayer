package com.zq.zqplayer.dagger.module

import com.zq.zqplayer.presenter.RecommendPresenter
import dagger.Module
import dagger.Provides

/**
 *author: wuyanqiang
 *2018/11/20
 */
@Module
class RecommendModule {
    var view: RecommendPresenter.View

    constructor(view: RecommendPresenter.View) {
        this.view = view
    }

    @Provides fun getview(): RecommendPresenter.View {
        return view;
    }

}