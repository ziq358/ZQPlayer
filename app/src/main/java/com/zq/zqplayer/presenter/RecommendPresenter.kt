package com.zq.zqplayer.presenter

import android.os.Bundle
import android.util.Log
import com.trello.rxlifecycle2.LifecycleTransformer
import com.ziq.base.mvp.BasePresenter
import com.ziq.base.mvp.IBaseView
import com.ziq.base.utils.RetrofitUtil
import com.zq.zqplayer.model.PandaTvDataBean
import com.zq.zqplayer.model.PandaTvListItemBean
import com.zq.zqplayer.model.PandaTvLiveDataBean
import com.zq.zqplayer.model.VideoHttpResult
import com.zq.zqplayer.model.request.BaseRequest
import com.zq.zqplayer.model.response.BaseObserver
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


     fun getVideo(req: BaseRequest) {
        RetrofitUtil.getInstance().retrofit
                .create<VideoService>(VideoService::class.java!!)
                .getVideList("lol", req.pageNo, req.pageSize, 1, "3.3.1.5978")
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    mView.showLoading()
                }
                .subscribeOn(AndroidSchedulers.mainThread())//控制doOnSubscribe 所在线程
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    mView.hideLoading()
                }
                .compose(this.getLifecycleTransformer() as LifecycleTransformer<VideoHttpResult<PandaTvDataBean>>)
                .subscribe(object : BaseObserver<VideoHttpResult<PandaTvDataBean>>() {
                    override fun onSuccessful(t: VideoHttpResult<PandaTvDataBean>?) {
                        if(t != null && t.data != null && t.data.items != null){
                            mView.setData(t.data.items)
                        }
                    }

                    override fun onFailed(msg: String?) {
                        mView.showMessage(msg)
                    }

                })
    }

    fun getVideoUrl(roomId:String ) {
        RetrofitUtil.getInstance().retrofit
                .create<VideoService>(VideoService::class.java!!)
                .getLiveUrl(roomId, "3.3.1.5978", 1, "json", "android")
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    mView.showLoading()
                }
                .subscribeOn(AndroidSchedulers.mainThread())//控制doOnSubscribe 所在线程
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    mView.hideLoading()
                }
                .compose(this.getLifecycleTransformer() as LifecycleTransformer<VideoHttpResult<PandaTvLiveDataBean>>)
                .subscribe(object : BaseObserver<VideoHttpResult<PandaTvLiveDataBean>>() {
                    override fun onSuccessful(t: VideoHttpResult<PandaTvLiveDataBean>?) {
                        val data:PandaTvLiveDataBean = t!!.data
                        val pl = data.info.videoinfo.plflag.split("_")

                        if (pl.isNotEmpty()) {
                            val url = "http://pl" + pl[pl.size - 1] + ".live.panda.tv/live_panda/" + data.info.videoinfo.room_key + "_mid.flv?sign=" + data.info.videoinfo.sign + "&time=" + data.info.videoinfo.ts
                            mView.onGetVideoUrlSuccessful(url)
                        }
                    }

                    override fun onFailed(msg: String?) {
                        mView.showMessage(msg)
                    }

                })
    }


    override fun destroy() {
    }


    interface View : IBaseView {
        fun setData(items:List<PandaTvListItemBean>)
        fun onGetVideoUrlSuccessful(url:String)
        fun showMessage(msg:String?)
    }
}


