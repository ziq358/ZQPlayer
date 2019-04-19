package com.zq.zqplayer.http;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<T> {

        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(T t) {
            if (t == null) {
                onFailed("网络异常");
            } else {
                onSuccessful(t);
            }
            onComplete();
        }

        @Override
        public void onError(Throwable e) {
            onFailed(e.getMessage());
            onComplete();
        }

        @Override
        public void onComplete() {

        }

        public abstract void onSuccessful(T t);
        public abstract void onFailed(String msg);


}