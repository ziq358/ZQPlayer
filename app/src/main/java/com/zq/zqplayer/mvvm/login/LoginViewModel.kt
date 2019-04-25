package com.zq.zqplayer.mvvm.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class LoginViewModel : AndroidViewModel, ILoginViewModel{

    override val isPhoneValid = MutableLiveData<Boolean>()

    @Inject
    constructor(application : Application) : super(application){
        isPhoneValid.value = true
    }


}