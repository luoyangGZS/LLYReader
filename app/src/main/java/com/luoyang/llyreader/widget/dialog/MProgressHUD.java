package com.luoyang.llyreader.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.luoyang.llyreader.R;

/**
 *
 *
 * @author luoyang
 * @date 2023/3/16
 */
public class MProgressHUD {
    private Boolean isFinishing = false;

    private Context context;
    /**
     * activity的根View
     */
    private ViewGroup decorView;
    /**
     * mSharedView 的 根View
     */
    private ViewGroup rootView;
    private MProgressView mSharedView;


    private Animation inAnim;
    private Animation outAnim;

    private Boolean canBack = false;

    public MProgressHUD(Context context) {
        this.context = context;
        initViews();
        initCenter();
        initAnimation();
    }

    private void initAnimation() {
        inAnim = getInAnimation();
        outAnim = getOutAnimation();
    }

    private void initCenter() {
        mSharedView.setGravity(Gravity.CENTER);
        if(mSharedView != null){
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mSharedView.getLayoutParams();
            if(layoutParams!=null){
                layoutParams.setMargins(0, 0, 0, 0);
                mSharedView.setLayoutParams(layoutParams);
            }
            mSharedView.setPadding(0,0,0,0);
        }
    }

    private void initBottom() {
        mSharedView.setGravity(Gravity.BOTTOM);
        if(mSharedView != null){
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mSharedView.getLayoutParams();
            if(layoutParams!=null){
                layoutParams.setMargins(0, 0, 0, 0);
                mSharedView.setLayoutParams(layoutParams);
            }
            mSharedView.setPadding(0,0,0,0);
        }
    }

    private void initMarRightTop() {
        mSharedView.setGravity(Gravity.RIGHT | Gravity.TOP);
        if(mSharedView != null){
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mSharedView.getLayoutParams();
            if(layoutParams!=null){
                layoutParams.setMargins(0, 0, 0, 0);
                mSharedView.setLayoutParams(layoutParams);
            }
            mSharedView.setPadding(0, 0, 0, 0);
        }
    }

    private void initViews() {
        decorView = (ViewGroup) ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        rootView = new FrameLayout(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        rootView.setLayoutParams(layoutParams);
        rootView.setClickable(true);

        mSharedView = new MProgressView(context);

    }

    public Animation getInAnimation() {
        return AnimationUtils.loadAnimation(context, R.anim.moprogress_in);
    }

    public Animation getOutAnimation() {
        return AnimationUtils.loadAnimation(context, R.anim.moprogress_out);
    }

    public boolean isShowing() {
        return rootView.getParent() != null;
    }

    private void onAttached() {
        decorView.addView(rootView);
        if (mSharedView.getParent() != null) {
            ((ViewGroup) mSharedView.getParent()).removeView(mSharedView);
        }
        rootView.addView(mSharedView);

        isFinishing = false;
    }

    public void dismiss() {
        //消失动画
        if (mSharedView != null && rootView != null && mSharedView.getParent() != null) {
            if (!isFinishing) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        outAnim.setAnimationListener(outAnimListener);
                        mSharedView.getChildAt(0).startAnimation(outAnim);
                    }
                });
            }
        }
    }

    public Boolean isShow() {
        if (mSharedView != null && mSharedView.getParent() != null) return true;
        return false;
    }

    Animation.AnimationListener outAnimListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            isFinishing = true;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            dismissImmediately();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    public void dismissImmediately() {
        if (mSharedView != null && rootView != null && mSharedView.getParent() != null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    rootView.removeView(mSharedView);
                    decorView.removeView(rootView);
                }
            });
        }
        isFinishing = false;
    }

    //转圈的载入
    public void showLoading() {
        showLoading(null);
    }

    //同上
    public void showLoading(String msg) {
        initCenter();
        initAnimation();
        canBack = false;
        rootView.setBackgroundColor(Color.parseColor("#00000000"));
        rootView.setOnClickListener(null);
        if (!isShowing()) {
            onAttached();
        }
        mSharedView.showLoading(msg);
        mSharedView.getChildAt(0).startAnimation(inAnim);
    }

    //单个按钮的提示信息
    public void showInfo(String msg) {
        initCenter();
        initAnimation();
        canBack = true;
        rootView.setBackgroundColor(Color.parseColor("#00000000"));
        rootView.setOnClickListener(null);
        mSharedView.showInfo(msg, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (!isShowing()) {
            onAttached();
        }
        mSharedView.getChildAt(0).startAnimation(inAnim);
    }

    //单个按钮的提示信息
    public void showInfo(String msg, String btnText, View.OnClickListener listener) {
        initCenter();
        initAnimation();
        canBack = true;
        rootView.setBackgroundColor(Color.parseColor("#CC000000"));
        rootView.setOnClickListener(null);
        mSharedView.showInfo(msg, btnText, listener);
        if (!isShowing()) {
            onAttached();
        }
        mSharedView.getChildAt(0).startAnimation(inAnim);
    }

    //////////////////////两个不同等级的按钮//////////////////////
    public void showTwoButton(String msg, String b_f, View.OnClickListener c_f, String b_s, View.OnClickListener c_s) {
        initCenter();
        initAnimation();
        canBack = true;
        rootView.setBackgroundColor(Color.parseColor("#CC000000"));
        rootView.setOnClickListener(null);
        mSharedView.showTwoButton(msg, b_f, c_f, b_s, c_s);
        if (!isShowing()) {
            onAttached();
        }
        mSharedView.getChildAt(0).startAnimation(inAnim);
    }
    ////////////////////离线章节选择////////////////////////////
    public interface OnClickDownload{
        public void download(int start,int end);
    }
    public void showDownloadList(int startIndex, int endIndex,int all, OnClickDownload clickDownload){
        initCenter();
        initAnimation();
        canBack = true;
        rootView.setBackgroundColor(Color.parseColor("#00000000"));
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mSharedView.showDownloadList(startIndex, endIndex, all, clickDownload, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (!isShowing()) {
            onAttached();
        }
        mSharedView.getChildAt(0).startAnimation(inAnim);
    }
    //////////////////////////////////////////////////////////

    public Boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isShowing() && canBack) {
                dismiss();
                return true;
            }
        }
        return false;
    }

    public Boolean getCanBack() {
        return canBack;
    }

    public Boolean onPressBack() {
        if (isShowing() && canBack) {
            dismiss();
            return true;
        }
        return false;
    }
}
