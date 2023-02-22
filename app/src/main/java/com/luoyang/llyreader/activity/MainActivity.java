package com.luoyang.llyreader.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.luoyang.llyreader.R;
import com.luoyang.llyreader.base.MBaseFragment;
import com.luoyang.llyreader.fragment.AccountFragment;
import com.luoyang.llyreader.fragment.CommunityFragment;
import com.luoyang.llyreader.fragment.HomeFragment;

import java.util.ArrayList;

/**
 * 基础入口
 *
 * @author luoyang
 */
public class MainActivity extends FragmentActivity {

    private RadioGroup mRadioGroup;

    private int mPosition = 0;

    private ArrayList<MBaseFragment> mFragmentList;

    private MBaseFragment mTempFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRadioGroup = findViewById(R.id.rg_main);
        // 初始化fragments
        initFragment();
        //设置RadioGroup的监听
        initListener();
    }

    private void initListener() {

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.rb_community:
                        mPosition = 1;
                        break;
                    case R.id.rb_user:
                        mPosition = 2;
                        break;
                    case R.id.rb_home:
                    default:
                        mPosition = 0;
                        break;
                }

                MBaseFragment nextFragment = getFragment(mPosition);
                switchFragment(nextFragment);
            }
        });
        //一开始选中首页
        mRadioGroup.check(R.id.rb_home);
    }

    /**
     * 切换fragment
     *
     * @param nextFragment 当前需要显示的Fragment
     */
    private void switchFragment(MBaseFragment nextFragment) {
        if ((nextFragment != null) && (mTempFragment != nextFragment)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //隐藏当前fragment
            if (mTempFragment != null) {
                transaction.hide(mTempFragment);
            }
            //判断nextFragment是否在缓存里
            if (nextFragment.isAdded()) {
                transaction.show(nextFragment).commit();
            } else {
                transaction.add(R.id.frameLayout, nextFragment).commit();
            }
            mTempFragment = nextFragment;
        }
    }

    private MBaseFragment getFragment(int position) {
        if (mFragmentList != null && mFragmentList.size() > position) {
            return mFragmentList.get(position);
        }
        return null;
    }

    private void initFragment() {
        mFragmentList = new ArrayList<>(3);
        mFragmentList.add(new HomeFragment());
        mFragmentList.add(new CommunityFragment());
        mFragmentList.add(new AccountFragment());
    }


    /**
     * 双击back键退出
     **/
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private long exitTime = 0;

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getContext().getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}