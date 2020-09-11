package com.zq.zqplayer.mvvm.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.ziq.base.baserx.dagger.component.AppComponent
import com.ziq.base.mvvm.MvvmBaseActivity
import com.zq.zqplayer.MainActivity
import com.zq.zqplayer.R
import com.zq.zqplayer.bean.UserInfoBean
import com.zq.zqplayer.database.AppDatabase
import com.zq.zqplayer.database.bean.User
import com.zq.zqplayer.databinding.ActivityRegisterBinding
import com.zq.zqplayer.event.LoginSuccessEvent
import com.zq.zqplayer.mvvm.LifecycleProviderModule
import com.zq.zqplayer.mvvm.register.component.DaggerRegisterComponent
import com.zq.zqplayer.mvvm.register.component.RegisterModule
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

class RegisterActivity : MvvmBaseActivity() {
    @Inject lateinit var registerViewModel : IRegisterViewModel

    var activityRegisterBinding: ActivityRegisterBinding? = null

    override fun initView(savedInstanceState: Bundle?) {
        activityRegisterBinding = DataBindingUtil.setContentView(this, R.layout.activity_register)
    }

    override fun initForInject(appComponent: AppComponent?) {
        DaggerRegisterComponent
                .builder()
                .appComponent(appComponent)
                .registerModule(RegisterModule(this))
                .lifecycleProviderModule(LifecycleProviderModule(this))
                .build().inject(this)
    }

    override fun initData(savedInstanceState: Bundle?) {
        activityRegisterBinding?.viewmodel = registerViewModel
        activityRegisterBinding?.lifecycleOwner = this // 设置了这个， livedata 的变化才会应用到界面

        registerViewModel.toastMsg.observe(this, object : Observer<String> {
            override fun onChanged(t: String?) {
                Toast.makeText(this@RegisterActivity, t, Toast.LENGTH_LONG).show()
            }
        })

        registerViewModel.isLoading.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                if(t!!){
                    showLoading()
                }else{
                    hideLoading()
                }
            }
        })

        registerViewModel.userInfo.observe(this, object : Observer<UserInfoBean> {
            override fun onChanged(t: UserInfoBean?) {
                var disposable = Observable.create(ObservableOnSubscribe<UserInfoBean> {
                    AppDatabase.getDatabase(this@RegisterActivity).userDao()?.insert(User(t?.userName!!, t.password!!))
                    it.onNext(t!!)
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    EventBus.getDefault().post(LoginSuccessEvent())
                    startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                    finish()
                }
            }
        })

    }

}