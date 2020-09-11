package com.zq.zqplayer.mvvm.splash

import android.content.Intent
import android.os.Bundle
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.mvvm.MvvmBaseActivity
import com.zq.zqplayer.MainActivity
import com.zq.zqplayer.R
import com.zq.zqplayer.bean.UserInfoBean
import com.zq.zqplayer.database.AppDatabase
import com.zq.zqplayer.database.bean.User
import com.zq.zqplayer.mvvm.login.LoginActivity
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SplashActivity : MvvmBaseActivity() {

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_splash)
    }

    override fun initForInject(appComponent: AppComponent?) {
    }

    override fun initData(savedInstanceState: Bundle?) {
        var disposable = Observable.create(ObservableOnSubscribe<Boolean> {
                    val all: List<User?>? = AppDatabase.getDatabase(this@SplashActivity).userDao()?.all
                    it.onNext(all != null && all.isNotEmpty())
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if(it){
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                        overridePendingTransition(0, 0)
                    }else{
                        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                        finish()
                        overridePendingTransition(0, 0)
                    }
                }

    }

}