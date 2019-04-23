package com.zq.zqplayer.mvp.main.presenter

import com.ziq.base.baserx.dagger.bean.IRepositoryManager
import com.ziq.base.mvp.BasePresenter
import com.ziq.base.utils.RetrofitUtil
import com.zq.zqplayer.http.request.ZQPlayerVideoListRequest
import com.zq.zqplayer.http.request.ZQPlayerVideoUrlRequest
import com.zq.zqplayer.http.BaseObserver
import com.zq.zqplayer.http.response.BaseResponse
import com.zq.zqplayer.bean.LiveListItemBean
import com.zq.zqplayer.bean.LiveItemDetailBean
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
    @Inject lateinit var mRepositoryManager: IRepositoryManager



    @Inject
    constructor() : super()

    var currentPage:Int = 0;
    fun getZqVideoList(isRefresh:Boolean) {

        if(isRefresh){
            currentPage = 0;
        }

        val request = ZQPlayerVideoListRequest()
        request.offset = "${currentPage * 20}"
        request.limit = "20"
        request.game_type = "ow"
        mRepositoryManager.createService(VideoService::class.java!!)
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
                .subscribe(object : BaseObserver<BaseResponse<List<LiveListItemBean>>>() {
                    override fun onSuccessful(t: BaseResponse<List<LiveListItemBean>>?) {
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

    fun getZqVideoUrl(roomId:String , live_type:String, game_type:String) {
        val request: ZQPlayerVideoUrlRequest = ZQPlayerVideoUrlRequest();
        request.live_id = roomId
        request.live_type = live_type
        request.game_type = game_type
        mRepositoryManager.createService(VideoService::class.java!!)
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
                .subscribe(object : BaseObserver<BaseResponse<LiveItemDetailBean>>() {
                    override fun onSuccessful(t: BaseResponse<LiveItemDetailBean>?) {
                        if(t != null && t.data != null){
                            mView.onGetVideoUrlSuccessful(t.data)
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


