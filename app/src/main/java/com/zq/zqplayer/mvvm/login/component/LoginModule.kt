package com.zq.zqplayer.mvvm.login.component

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.zq.zqplayer.mvvm.login.ILoginViewModel
import com.zq.zqplayer.mvvm.login.LoginViewModel
import com.zq.zqplayer.mvvm.login.LoginViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class LoginModule {

    var viewModelStoreOwner: ViewModelStoreOwner

    constructor(viewModelStoreOwner: ViewModelStoreOwner) {
        this.viewModelStoreOwner = viewModelStoreOwner
    }

    @Provides
    fun provideLoginViewModel(factory: LoginViewModelFactory): ILoginViewModel {
        //多了这一步， 主要是为了利用 ViewModelStore 保存实例
        return ViewModelProvider(viewModelStoreOwner, factory).get(LoginViewModel::class.java)
    }

}