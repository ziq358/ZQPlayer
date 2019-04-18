package com.zq.zqplayer.presenter

import com.trello.rxlifecycle2.LifecycleTransformer
import com.ziq.base.mvp.BasePresenter
import com.ziq.base.mvp.IBaseView
import com.ziq.base.utils.RetrofitUtil
import com.zq.zqplayer.model.request.BaseRequest
import com.zq.zqplayer.model.request.ZQPlayerVideoListRequest
import com.zq.zqplayer.model.request.ZQPlayerVideoUrlRequest
import com.zq.zqplayer.model.response.BaseObserver
import com.zq.zqplayer.model.response.BaseResponse
import com.zq.zqplayer.model.response.ZQPlayerVideoListItemBean
import com.zq.zqplayer.model.response.ZQPlayerVideoUrlBean
import com.zq.zqplayer.service.VideoService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 *author: wuyanqiang
 *2018/11/20
 */
class RecommendPresenter : BasePresenter {

    @Inject lateinit var mView: RecommendPresenter.View


    @Inject
    constructor() : super()


    fun getZqVideoList(req: BaseRequest) {
        val request:ZQPlayerVideoListRequest = ZQPlayerVideoListRequest();
        request.pageno = req.pageNo;
        request.pagenum = req.pageSize
        request.cate = "lol"
        request.room = 1
        request.version = "3.3.1.5978"
        RetrofitUtil.getInstance().retrofit
                .create<VideoService>(VideoService::class.java!!)
                .getZQVideoList("http://193.112.65.251:1234/live/list", request)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    mView.showLoading()
                }
                .subscribeOn(AndroidSchedulers.mainThread())//控制doOnSubscribe 所在线程
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    mView.hideLoading()
                }
                .compose(this.getDestroyLifecycleTransformer())
                .subscribe(object : BaseObserver<BaseResponse<List<ZQPlayerVideoListItemBean>>>() {
                    override fun onSuccessful(t: BaseResponse<List<ZQPlayerVideoListItemBean>>?) {
                        if(t != null && t.data != null){
                            mView.setData(t.data)
                        }
                    }

                    override fun onFailed(msg: String?) {
                        mView.showMessage(msg)
                    }

                })
    }

    fun getZqVideoUrl(roomId:String , title:String) {
        val request:ZQPlayerVideoUrlRequest = ZQPlayerVideoUrlRequest();
        request.__version = "3.3.1.5978"
        request.roomid = roomId
        request.slaveflag = 1
        request.type = "json"
        request.__plat = "android"
        RetrofitUtil.getInstance().retrofit
                .create<VideoService>(VideoService::class.java!!)
                .getZQVideoListUrl("http://193.112.65.251:1234/live/list/item", request)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    mView.showLoading()
                }
                .subscribeOn(AndroidSchedulers.mainThread())//控制doOnSubscribe 所在线程
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    mView.hideLoading()
                }
                .compose(this.getDestroyLifecycleTransformer())
                .subscribe(object : BaseObserver<BaseResponse<ZQPlayerVideoUrlBean>>() {
                    override fun onSuccessful(t: BaseResponse<ZQPlayerVideoUrlBean>?) {
                        if(t != null && t.data != null){
                            mView.onGetVideoUrlSuccessful(t.data.videoUrl, title)
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
        fun setData(items:List<ZQPlayerVideoListItemBean>)
        fun onGetVideoUrlSuccessful(url:String, title:String)
        fun showMessage(msg:String?)
    }
}


