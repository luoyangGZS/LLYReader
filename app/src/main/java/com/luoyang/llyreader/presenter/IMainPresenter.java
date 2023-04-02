package com.luoyang.llyreader.presenter;


import com.luoyang.basemvp.IPresenter;

/**
 *
 *
 * @author luoyang
 * @date 2023/3/01
 */
public interface IMainPresenter extends IPresenter {

    /**
     * 刷新书架
     *
     * @param needRefresh
     */
    void queryBookShelf(Boolean needRefresh);

}
