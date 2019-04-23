package com.zq.zqplayer.mvvm.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import javax.inject.Inject

class LoginViewModel : AndroidViewModel, ILoginViewModel{

    @Inject
    constructor(application : Application) : super(application){
    }
}