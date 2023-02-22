package com.luoyang.basemvp.impl;

import com.luoyang.basemvp.IPresenter;
import com.luoyang.basemvp.IView;

/**
 * 可以使用弱引用防止内存泄漏
 *
 * @author luoyang
 * @date 2023/2/21
 */
public abstract class BasePresenter<V extends IView> implements IPresenter {

    /**
     * presenter 连接 view
     */
    protected V mView;

    @Override
    public void attachView(IView iView) {
        mView = (V) iView;
    }
}
