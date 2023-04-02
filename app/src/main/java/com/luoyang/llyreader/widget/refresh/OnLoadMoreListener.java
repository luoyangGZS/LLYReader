package com.luoyang.llyreader.widget.refresh;

/**
 * 加载更新监听接口
 *
 * @author luoyang
 * @date 2023/2/28
 */
public interface OnLoadMoreListener {

    /**
     * 开始加载更多
     */
     void startLoadmore();

    /**
     * 加载更多失败，再次尝试
     */
    void loadMoreErrorTryAgain();
}
