package com.zq.zqplayer.mvvm.login

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class LoginViewModelFactory : ViewModelProvider.NewInstanceFactory {

    @Inject lateinit var application: Application

    @Inject lateinit var loginViewModel: LoginViewModel

    @Inject
    constructor() : super(){
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        //isAssignableFrom()方法是判断是否为某个类的父类
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return loginViewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name) as Throwable
    }

}