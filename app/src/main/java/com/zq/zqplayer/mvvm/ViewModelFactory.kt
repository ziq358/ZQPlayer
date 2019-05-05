package com.zq.zqplayer.mvvm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zq.zqplayer.mvvm.login.LoginViewModel
import com.zq.zqplayer.mvvm.recommend.RecommendViewModel
import com.zq.zqplayer.mvvm.register.RegisterViewModel
import javax.inject.Inject

class ViewModelFactory : ViewModelProvider.NewInstanceFactory {

    @Inject lateinit var application: Application

    @Inject lateinit var loginViewModel: LoginViewModel
    @Inject lateinit var registerViewModel: RegisterViewModel
    @Inject lateinit var recommendviewmodel: RecommendViewModel

    @Inject
    constructor() : super(){
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        //isAssignableFrom()方法是判断是否为某个类的父类
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return loginViewModel as T
        }else if(modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return registerViewModel as T
        }else if(modelClass.isAssignableFrom(RecommendViewModel::class.java)){
            return recommendviewmodel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name) as Throwable
    }

}