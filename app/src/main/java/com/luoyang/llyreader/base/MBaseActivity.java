package com.luoyang.llyreader.base;

import com.luoyang.basemvp.IPresenter;
import com.luoyang.basemvp.impl.BaseActivity;
import com.luoyang.basemvp.utils.LogUtils;

/**
 * 应用activity入口
 * 留作埋点
 *
 * @author luoyang
 * @date 2023/2/21
 */
public abstract class MBaseActivity<P extends IPresenter> extends BaseActivity<P> {

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.INSTANCE.d("MBaseActivity onResume ");
    }

    @Override
    public void onPause(){
        super.onPause();
        LogUtils.INSTANCE.d("MBaseActivity onPause ");
    }
}
