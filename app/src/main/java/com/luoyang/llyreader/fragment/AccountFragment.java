package com.luoyang.llyreader.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luoyang.basemvp.IPresenter;
import com.luoyang.llyreader.R;
import com.luoyang.llyreader.base.MBaseFragment;

/**
 * @author luoyang
 * @date 2023/2/22
 */
public class AccountFragment<P extends IPresenter> extends MBaseFragment<P> {
    @Override
    protected P initInjector() {
        return null;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return View.inflate(getViewContext(), R.layout.fragment_account, null);
    }

    @Override
    protected void initData() {

    }
}
