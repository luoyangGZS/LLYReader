package com.luoyang.llyreader.widget.refresh;

/**
 * 刷新监听接口
 *
 * @author luoyang
 * @date 2023/2/28
 */
public interface OnRefreshWithProgressListener extends BaseRefreshListener {

    /**
     * 获取最大进度
     *
     * @return
     */
    int getMaxProgress();
}
