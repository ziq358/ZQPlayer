package com.zq.zqplayer.mvvm.register

import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.zq.zqplayer.bean.UserInfoBean

interface IRegisterViewModel {
    val isValid: MutableLiveData<Boolean>
    val userName: MutableLiveData<String>
    val userPassword: MutableLiveData<String>
    val userNameTextWatcher: TextWatcher
    val userPasswordTextWatcher: TextWatcher

    val toastMsg: MutableLiveData<String>
    val isLoading: MutableLiveData<Boolean>
    fun register(view: View):Unit
    val userInfo: MutableLiveData<UserInfoBean>
}