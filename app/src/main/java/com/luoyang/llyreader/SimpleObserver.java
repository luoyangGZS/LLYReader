package com.luoyang.llyreader;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 全局观察者
 *
 * @author luoyang
 * @date 2023/2/21
 */
public abstract class SimpleObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onComplete() {

    }
}
