package com.zq.zqplayer.mvvm.login

import androidx.lifecycle.MutableLiveData

interface ILoginViewModel {
    val isPhoneValid: MutableLiveData<Boolean>
}