package com.luoyang.llyreader.base;

import android.view.View;

import com.luoyang.basemvp.IPresenter;
import com.luoyang.basemvp.impl.BaseFragment;
import com.luoyang.basemvp.utils.LogUtils;

/**
 * 应用fragment入口
 * 可以留作买点
 *
 * @author luoyang
 * @date 2023/2/21
 */
public abstract class MBaseFragment<P extends IPresenter> extends BaseFragment<P> {

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.INSTANCE.d("MBaseFragment onResume ");
    }

    @Override
    public void onPause(){
        super.onPause();
        LogUtils.INSTANCE.d("MBaseFragment onPause ");
    }
}
