package com.zq.zqplayer.presenter

import android.util.Log
import com.trello.rxlifecycle2.LifecycleTransformer
import com.ziq.base.mvp.BasePresenter
import com.ziq.base.mvp.IBaseView
import com.ziq.base.utils.RetrofitUtil
import com.zq.zqplayer.model.PandaTvDataBean
import com.zq.zqplayer.model.VideoHttpResult
import com.zq.zqplayer.service.VideoService
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
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


    fun getVideo() {
        RetrofitUtil.getInstance().retrofit
                .create<VideoService>(VideoService::class.java!!)
                .getVideList("lol", 1, 20, 1, "3.3.1.5978")
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    Log.e("ziq", "doOnSubscribe: " + Thread.currentThread().name)
                    mView.showLoading()
                }
                .subscribeOn(AndroidSchedulers.mainThread())//控制doOnSubscribe 所在线程
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    Log.e("ziq", "doFinally: " + Thread.currentThread().name)
                    mView.hideLoading()
                }
                .compose(this.getLifecycleTransformer() as LifecycleTransformer<VideoHttpResult<PandaTvDataBean>>)
                .subscribe(object : Observer<VideoHttpResult<PandaTvDataBean>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(pandaTvDataBeanVideoHttpResult: VideoHttpResult<PandaTvDataBean>) {
                        Log.e("ziq", "onNext: " + Thread.currentThread().name)
                        mView.setData()
                    }

                    override fun onError(e: Throwable) {
                        Log.e("ziq", "onError: $e")
                    }

                    override fun onComplete() {

                    }
                })
    }


    override fun destroy() {
    }


    interface View : IBaseView {
        fun setData()
    }
}


