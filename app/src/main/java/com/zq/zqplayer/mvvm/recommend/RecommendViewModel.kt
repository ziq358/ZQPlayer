package com.zq.zqplayer.mvvm.recommend

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.trello.rxlifecycle3.LifecycleProvider
import com.ziq.base.baserx.dagger.bean.IRepositoryManager
import com.ziq.base.utils.LifecycleUtil
import com.zq.zqplayer.bean.RoomInfoBean
import com.zq.zqplayer.http.BaseObserver
import com.zq.zqplayer.http.request.LiveRequest
import com.zq.zqplayer.http.response.BaseResponse
import com.zq.zqplayer.http.service.VideoService
import com.zq.zqplayer.util.UserInfoUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RecommendViewModel : AndroidViewModel, IRecommendViewModel{


    @Inject lateinit var lifecycleProvider: LifecycleProvider<*>
    @Inject lateinit var mRepositoryManager:IRepositoryManager
    lateinit var headers:HashMap<String, String>

    override val toastMsg = MutableLiveData<String>()
    override val isLoading = MutableLiveData<Boolean>()
    override val onRefreshItems = MutableLiveData<ArrayList<RoomInfoBean>>()
    override val onLoadMoreItems = MutableLiveData<ArrayList<RoomInfoBean>>()


    @Inject
    constructor(application : Application) : super(application){
        headers = HashMap()
        var token  = ""
        if(UserInfoUtil.getUserInfo()?.token != null){
            token = UserInfoUtil.getUserInfo()?.token!!
        }
        headers["token"] = token
    }


    var currentPage:Int = 0;
    override fun getZqVideoList(isRefresh:Boolean) {

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
                    isLoading.value = true
                }
                .subscribeOn(AndroidSchedulers.mainThread())//控制doOnSubscribe 所在线程
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    isLoading.value = false
                }
                .compose(LifecycleUtil.bindToDestroy(lifecycleProvider))
                .subscribe(object : BaseObserver<BaseResponse<ArrayList<RoomInfoBean>>>() {
                    override fun onSuccessful(t: BaseResponse<ArrayList<RoomInfoBean>>?) {
                        if(t?.data != null){
                            currentPage++
                            if(isRefresh){
                                onRefreshItems.value = t.data
                            }else{
                                onLoadMoreItems.value = t.data
                            }
                        }
                    }

                    override fun onFailed(msg: String?) {
                        toastMsg.value = msg
                    }

                })
    }

}