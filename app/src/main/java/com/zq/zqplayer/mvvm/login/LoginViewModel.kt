package com.zq.zqplayer.mvvm.login

import android.app.Application
import android.text.Editable
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.trello.rxlifecycle3.LifecycleProvider
import com.ziq.base.baserx.dagger.bean.IRepositoryManager
import com.ziq.base.utils.LifecycleUtil
import com.zq.zqplayer.bean.UserInfoBean
import com.zq.zqplayer.common.SimpleTextWatcher
import com.zq.zqplayer.http.BaseObserver
import com.zq.zqplayer.http.request.LoginRequest
import com.zq.zqplayer.http.response.BaseResponse
import com.zq.zqplayer.http.service.UserService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel : AndroidViewModel, ILoginViewModel{


    @Inject lateinit var lifecycleProvider: LifecycleProvider<*>
    @Inject lateinit var iRepositoryManager:IRepositoryManager

    override val isGotoRegisterActivity = MutableLiveData<Boolean>()
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



    override fun login(view: View) {
        val userInfoBean:UserInfoBean = UserInfoBean()
        userInfoBean.userName = userName.value!!
        userInfoBean.password = userPassword.value!!
        userInfo.value = userInfoBean
//        val loginRequest = LoginRequest(userName.value!!, userPassword.value!!)
//        iRepositoryManager.createService(UserService::class.java)
//                .login(loginRequest)
//                .subscribeOn(Schedulers.io())
//                .doOnSubscribe {
//                    isLoading.value = true
//                }
//                .subscribeOn(AndroidSchedulers.mainThread())//控制doOnSubscribe 所在线程
//                .observeOn(AndroidSchedulers.mainThread())
//                .doFinally {
//                    isLoading.value = false
//                }
//                .compose(LifecycleUtil.bindToDestroy(lifecycleProvider))
//                .subscribe(object : BaseObserver<BaseResponse<UserInfoBean>>() {
//                    override fun onSuccessful(t: BaseResponse<UserInfoBean>?) {
//                        if(t!!.isSuccess && t.data != null){
//                            userInfo.value = t.data
//                        }else{
//                            toastMsg.value = t.msg
//                        }
//                    }
//
//                    override fun onFailed(msg: String?) {
//                        toastMsg.value = msg
//                    }
//
//                })
    }

    override fun gotoRegisterPage(view: View) {
        isGotoRegisterActivity.value = true
    }


    @Inject
    constructor(application : Application) : super(application){
        userName.value = "zqplayer"
        userPassword.value = "123456"

    }


}