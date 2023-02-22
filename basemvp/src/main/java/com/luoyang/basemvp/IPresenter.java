package com.luoyang.basemvp;

/**
 * 基础Presenter层
 *
 * @author luoyang
 * @date 2023/2/20
 */
public interface IPresenter {

    /**
     * 注入view,使之能与Activity连接
     *
     * @param iView 注入的view
     */
    void attachView(IView iView);

    /**
     * 释放资源，如果请求了网络，可以在这里执行IModel.cancelRequest()
     */
    void detachView();
}


