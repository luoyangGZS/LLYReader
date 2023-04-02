package com.luoyang.llyreader.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luoyang.llyreader.R;
import com.luoyang.llyreader.adapter.ImportBookAdapter;
import com.luoyang.llyreader.base.MBaseActivity;
import com.luoyang.llyreader.presenter.IImportBookPresenter;
import com.luoyang.llyreader.presenter.impl.ImportBookPresenterImpl;
import com.luoyang.llyreader.utils.PermissionCheck;
import com.luoyang.llyreader.view.IImportBookView;
import com.luoyang.llyreader.widget.dialog.MProgressHUD;

import java.io.File;

/**
 * 导入书本文件tet
 *
 * @author luoyang
 * @date 2023/3/2
 */
public class LocalImportBookActivity extends MBaseActivity<IImportBookPresenter> implements IImportBookView {
    private LinearLayout llContent;
    private ImageButton ivReturn;
    private TextView tvScan;

    private ProgressBar rlLoading;
    private TextView tvCount;

    private TextView tvAddShelf;

    private RecyclerView rcvBooks;

    private ImportBookAdapter importBookAdapter;

    private Animation animIn;
    private Animation animOut;

    private MProgressHUD moProgressHUD;


    @Override
    protected IImportBookPresenter initInjector() {
        return new ImportBookPresenterImpl();
    }

    @Override
    protected void onCreateActivity() {
        setContentView(R.layout.activity_import_book);
    }

    @Override
    protected void initData() {
        animIn = AnimationUtils.loadAnimation(this, R.anim.anim_act_importbook_in);
        animOut = AnimationUtils.loadAnimation(this, R.anim.anim_act_importbook_out);

        importBookAdapter = new ImportBookAdapter(new ImportBookAdapter.OnCheckBookListener() {
            @Override
            public void checkBook(int count) {
                tvAddShelf.setVisibility(count == 0 ? View.INVISIBLE : View.VISIBLE);
            }
        });
    }

    @Override
    protected void bindView() {
        moProgressHUD = new MProgressHUD(this);

        llContent = findViewById(R.id.ll_content);
        ivReturn =  findViewById(R.id.iv_return);
        tvScan =  findViewById(R.id.tv_scan);

        rlLoading =  findViewById(R.id.rl_loading);
        tvCount =  findViewById(R.id.tv_count);

        tvAddShelf =  findViewById(R.id.tv_addshelf);

        rcvBooks =  findViewById(R.id.rcv_books);
        rcvBooks.setAdapter(importBookAdapter);
        rcvBooks.setLayoutManager(new LinearLayoutManager(this));

        PermissionCheck.requestFilePermission(this);
    }

    @Override
    protected void bindEvent() {
        tvScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PermissionCheck.checkPermission(LocalImportBookActivity.this,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //申请权限
                    LocalImportBookActivity.this.requestPermissions(
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            0x11);
                } else {
                    mPresenter.searchLocationBook();
                    tvScan.setVisibility(View.INVISIBLE);
                    rlLoading.setVisibility(View.VISIBLE);
                }
            }
        });
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LocalImportBookActivity.super.finish();
                overridePendingTransition(0, 0);
                isExiting = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvAddShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加书籍
                moProgressHUD.showLoading("放入书架中...");
                mPresenter.importBooks(importBookAdapter.getSelectDatas());
            }
        });
    }
    @Override
    protected void firstRequest() {
        llContent.startAnimation(animIn);
    }

    private Boolean isExiting = false;

    @Override
    public void finish() {
        if (!isExiting) {
            if(moProgressHUD.isShow()){
                moProgressHUD.dismiss();
            }
            isExiting = true;
            llContent.startAnimation(animOut);
        }
    }

    @Override
    public void addNewBook(File newFile) {
        importBookAdapter.addData(newFile);
        tvCount.setText(String.format(getString(R.string.tv_importbook_count), String.valueOf(importBookAdapter.getItemCount())));
    }

    @Override
    public void searchFinish() {
        rlLoading.setVisibility(View.INVISIBLE);
        importBookAdapter.setCanCheck(true);
    }

    @Override
    public void addSuccess() {
        moProgressHUD.dismiss();
        Toast.makeText(this,"添加书籍成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addError() {
        moProgressHUD.showInfo("放入书架失败!");
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 0x11){
            if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && PermissionCheck.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                mPresenter.searchLocationBook();
                tvScan.setVisibility(View.INVISIBLE);
                rlLoading.setVisibility(View.VISIBLE);
            }else{
                if (!this.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    moProgressHUD.showTwoButton("去系统设置打开SD卡读写权限？", "取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            moProgressHUD.dismiss();
                        }
                    }, "设置", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            moProgressHUD.dismiss();
                            PermissionCheck.requestPermissionSetting(LocalImportBookActivity.this);
                        }
                    });
                }else{
                    Toast.makeText(this, "未获取SD卡读取权限", Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Boolean a = moProgressHUD.onKeyDown(keyCode,event);
        if(a) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}

