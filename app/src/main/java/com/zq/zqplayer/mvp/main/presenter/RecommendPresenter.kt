package com.zq.zqplayer.mvp.main.presenter

import com.ziq.base.mvp.BasePresenter
import com.ziq.base.utils.RetrofitUtil
import com.zq.zqplayer.http.request.ZQPlayerVideoListRequest
import com.zq.zqplayer.http.request.ZQPlayerVideoUrlRequest
import com.zq.zqplayer.http.BaseObserver
import com.zq.zqplayer.http.response.BaseResponse
import com.zq.zqplayer.bean.ZQPlayerVideoListItemBean
import com.zq.zqplayer.bean.ZQPlayerVideoUrlBean
import com.zq.zqplayer.mvp.main.contract.RecommendContract
import com.zq.zqplayer.http.service.VideoService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 *author: wuyanqiang
 *2018/11/20
 */
class RecommendPresenter : BasePresenter {

    @Inject lateinit var mView: RecommendContract.View


    @Inject
    constructor() : super()

    var currentPage:Int = 1;
    fun getZqVideoList(isRefresh:Boolean) {

        if(isRefresh){
            currentPage = 1;
        }

        val request = ZQPlayerVideoListRequest()
        request.pageno = currentPage
        request.pagenum = 20
        request.cate = "lol"
        request.room = 1
        request.version = "3.3.1.5978"
        RetrofitUtil.getInstance().retrofit
                .create<VideoService>(VideoService::class.java!!)
                .getZQVideoList(request)
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
                            currentPage++
                            mView.setData(t.data)
                        }
                    }

                    override fun onFailed(msg: String?) {
                        mView.showMessage(msg)
                    }

                })
    }

    fun getZqVideoUrl(roomId:String , title:String) {
        val request: ZQPlayerVideoUrlRequest = ZQPlayerVideoUrlRequest();
        request.__version = "3.3.1.5978"
        request.roomid = roomId
        request.slaveflag = 1
        request.type = "json"
        request.__plat = "android"
        RetrofitUtil.getInstance().retrofit
                .create<VideoService>(VideoService::class.java!!)
                .getZQVideoListUrl(request)
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
}


