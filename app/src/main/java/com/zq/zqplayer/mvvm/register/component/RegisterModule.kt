package com.zq.zqplayer.mvvm.register.component

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.zq.zqplayer.mvvm.login.ILoginViewModel
import com.zq.zqplayer.mvvm.login.LoginViewModel
import com.zq.zqplayer.mvvm.ViewModelFactory
import com.zq.zqplayer.mvvm.register.IRegisterViewModel
import com.zq.zqplayer.mvvm.register.RegisterViewModel
import dagger.Module
import dagger.Provides

@Module
class RegisterModule {

    var viewModelStoreOwner: ViewModelStoreOwner

    constructor(viewModelStoreOwner: ViewModelStoreOwner) {
        this.viewModelStoreOwner = viewModelStoreOwner
    }

    @Provides
    fun provideLoginViewModel(factory: ViewModelFactory): IRegisterViewModel {
        //多了这一步， 主要是为了利用 ViewModelStore 保存实例
        return ViewModelProvider(viewModelStoreOwner, factory).get(RegisterViewModel::class.java)
    }

}