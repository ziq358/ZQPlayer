package com.zq.zqplayer.mvp.main.presenter

import com.ziq.base.baserx.dagger.bean.IRepositoryManager
import com.ziq.base.mvp.BasePresenter
import com.zq.zqplayer.http.request.LiveRequest
import com.zq.zqplayer.http.BaseObserver
import com.zq.zqplayer.http.response.BaseResponse
import com.zq.zqplayer.bean.RoomInfoBean
import com.zq.zqplayer.mvp.main.contract.RecommendContract
import com.zq.zqplayer.http.service.VideoService
import com.zq.zqplayer.util.UserInfoUtil
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

    lateinit var headers:HashMap<String, String>

    @Inject
    constructor() : super(){
        headers = HashMap()
        var token  = ""
        if(UserInfoUtil.getUserInfo()?.token != null){
            token = UserInfoUtil.getUserInfo()?.token!!
        }
        headers["token"] = token
    }

    var currentPage:Int = 0;
    fun getLiveList(isRefresh:Boolean) {

        if(isRefresh){
            currentPage = 0;
        }

        val request = LiveRequest()
        request.offset = "${currentPage * 20}"
        request.limit = "20"

        mRepositoryManager.createService(VideoService::class.java)
                .getLiveList(headers, request)
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
                .subscribe(object : BaseObserver<BaseResponse<ArrayList<RoomInfoBean>>>() {
                    override fun onSuccessful(t: BaseResponse<ArrayList<RoomInfoBean>>?) {
                        if(t?.data != null){
                            currentPage++
                            mView.setData(t.data)
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


