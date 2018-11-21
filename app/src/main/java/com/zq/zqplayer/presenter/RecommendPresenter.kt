package com.zq.zqplayer.presenter

import android.util.Log
import com.trello.rxlifecycle2.LifecycleTransformer
import com.ziq.base.mvp.BasePresenter
import com.ziq.base.mvp.IBaseView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 *author: wuyanqiang
 *2018/11/20
 */
class RecommendPresenter : BasePresenter {

    @Inject lateinit var mView: RecommendPresenter.View

    @Inject
    constructor() : super()


    fun getData(): Unit {
        Log.e("ziq", "getVideo: " + getLifecycleTransformer())
        mView.showLoading()
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.getLifecycleTransformer() as LifecycleTransformer<Long>)
                .subscribe(object : Observer<Long> {
                    override fun onComplete() {
                        Log.e("ziq", "onComplete: ")
                    }

                    override fun onSubscribe(d: Disposable) {
                        Log.e("ziq", "onSubscribe: ")
                    }

                    override fun onNext(t: Long) {
                        Log.e("ziq", "onNext: $t")
                    }

                    override fun onError(e: Throwable) {
                        Log.e("ziq", "onError: ")
                    }

                })
        mView.setData()


    }

    override fun destroy() {
    }


    interface View : IBaseView {
        fun setData()
    }
}


