package com.luoyang.llyreader;

import com.luoyang.basemvp.impl.BaseApplication;
import com.luoyang.basemvp.utils.LogUtils;

/**
 * 全局上下文
 *
 * @author luoyang
 * @date 2023/2/21
 */
public class MApplication extends BaseApplication {
    @Override
    public void init() {
        LogUtils.INSTANCE.d("MainApplication init");
    }
}
