package com.luoyang.basemvp.impl;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.luoyang.basemvp.BaseLifecycleObserver;
import com.luoyang.basemvp.IPresenter;
import com.luoyang.basemvp.IView;

/**
 * mvp基础Fragment
 *
 * @author luoyang
 * @date 2023/2/21
 */
public abstract class BaseFragment<P extends IPresenter> extends Fragment implements IView, LifecycleOwner {

    protected P mPresenter;
    protected View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        initSDK();
        mView = onCreateView(inflater, container);
        mPresenter = initInjector();
        attachView();
        initData();
        bindView(mView);
        bindEvent();
        firstRequest();
        mLifecycleRegistry.addObserver(new BaseLifecycleObserver());
        return mView;
    }


    /**
     * 首次逻辑操作
     */
    protected void firstRequest() {

    }

    /**
     * 事件触发绑定
     */
    protected void bindEvent() {

    }

    /**
     * 控件绑定
     */
    protected void bindView(View view) {

    }

    /**
     * P层绑定V层
     */
    private void attachView() {
        if (null != mPresenter) {
            mPresenter.attachView(this);
        }
    }

    /**
     * P层解绑V层
     */
    private void detachView() {
        if (null != mPresenter) {
            mPresenter.detachView();
        }
    }

    /**
     * SDK初始化
     */
    protected void initSDK() {

    }

    /**
     * P层绑定   若无则返回null;
     *
     * @return presenter
     */
    protected abstract P initInjector();


    /**
     * 布局载入  onCreateView()
     *
     * @param inflater  充气者
     * @param container 容器
     * @return view
     */
    protected abstract View onCreateView(LayoutInflater inflater, ViewGroup container);

    /**
     * 数据初始化
     */
    protected abstract void initData();

    @Override
    public void onDestroy() {
        super.onDestroy();
        detachView();
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

    protected void startActivityByAnim(Intent intent, int animIn, int animExit) {
        startActivity(intent);
        getActivity().overridePendingTransition(animIn, animExit);
    }

    protected void startActivityByAnim(Intent intent, View view, String transitionName, int animIn, int animExit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, transitionName).toBundle());
        } else {
            startActivityByAnim(intent, animIn, animExit);
        }
    }

}
