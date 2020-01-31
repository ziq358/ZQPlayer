package com.zq.zqplayer.mvvm.register

import android.app.Application
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.trello.rxlifecycle3.LifecycleProvider
import com.trello.rxlifecycle3.android.ActivityEvent
import com.ziq.base.baserx.dagger.bean.IRepositoryManager
import com.ziq.base.utils.LifecycleUtil
import com.zq.zqplayer.bean.LiveListItemBean
import com.zq.zqplayer.bean.UserInfoBean
import com.zq.zqplayer.common.SimpleTextWatcher
import com.zq.zqplayer.http.BaseObserver
import com.zq.zqplayer.http.request.LoginRequest
import com.zq.zqplayer.http.response.BaseResponse
import com.zq.zqplayer.http.service.UserService
import com.zq.zqplayer.http.service.VideoService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RegisterViewModel : AndroidViewModel, IRegisterViewModel{

    @Inject lateinit var lifecycleProvider: LifecycleProvider<*>
    @Inject lateinit var iRepositoryManager:IRepositoryManager

    override val isLoading = MutableLiveData<Boolean>()
    override val toastMsg = MutableLiveData<String>()
    override val userInfo = MutableLiveData<UserInfoBean>()
    override val isValid = MutableLiveData<Boolean>()
    override val userName = MutableLiveData<String>()
    override val userPassword = MutableLiveData<String>()
    override val userNameTextWatcher = object : SimpleTextWatcher() {
        override fun afterTextChanged(p0: Editable?) {
            isValid.value = !TextUtils.isEmpty(userName.value) && !TextUtils.isEmpty(userPassword.value)
        }
    }

    override val userPasswordTextWatcher = object : SimpleTextWatcher() {

        override fun afterTextChanged(p0: Editable?) {
            isValid.value = !TextUtils.isEmpty(userName.value) && !TextUtils.isEmpty(userPassword.value)
        }
    }


    override fun register(view: View) {
        val loginRequest = LoginRequest(userName.value!!, userPassword.value!!)
        iRepositoryManager.createService(UserService::class.java)
                .register(loginRequest)
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
                .subscribe(object : BaseObserver<BaseResponse<UserInfoBean>>() {
                    override fun onSuccessful(t: BaseResponse<UserInfoBean>?) {
                        if(t!!.isSuccess && t.data != null){
                            userInfo.value = t.data
                        }else{
                            toastMsg.value = t.msg
                        }
                    }

                    override fun onFailed(msg: String?) {
                        toastMsg.value = msg
                    }

                })
    }



    @Inject
    constructor(application : Application) : super(application){
    }


}