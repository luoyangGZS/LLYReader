package com.luoyang.basemvp;

import android.content.Context;

/**
 * 基础View,如Activity
 *
 * @author luoyang
 * @date 2023/2/21
 */
public interface IView {

    /**
     * 上下文
     *
     * @return 返回公共上下文
     */
    Context getViewContext();
}
