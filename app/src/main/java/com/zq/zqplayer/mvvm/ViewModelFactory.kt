package com.zq.zqplayer.mvvm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zq.zqplayer.mvvm.login.LoginViewModel
import com.zq.zqplayer.mvvm.recommend.RecommendViewModel
import com.zq.zqplayer.mvvm.register.RegisterViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor() : ViewModelProvider.NewInstanceFactory() {

    @Inject lateinit var application: Application

    @Inject lateinit var loginViewModel: LoginViewModel
    @Inject lateinit var registerViewModel: RegisterViewModel
    @Inject lateinit var recommendviewmodel: RecommendViewModel

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        //isAssignableFrom()方法是判断是否为某个类的父类
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                loginViewModel as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                registerViewModel as T
            }
            modelClass.isAssignableFrom(RecommendViewModel::class.java) -> {
                recommendviewmodel as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name) as Throwable
        }
    }

}