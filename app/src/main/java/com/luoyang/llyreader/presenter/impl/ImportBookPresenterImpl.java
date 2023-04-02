package com.luoyang.llyreader.presenter.impl;

import android.os.Environment;

import com.hwangjr.rxbus.RxBus;
import com.luoyang.basemvp.impl.BasePresenter;
import com.luoyang.basemvp.utils.LogUtils;
import com.luoyang.llyreader.SimpleObserver;
import com.luoyang.llyreader.bean.LocBookShelfBean;
import com.luoyang.llyreader.constant.RxBusTag;
import com.luoyang.llyreader.model.impl.ImportBookModelImpl;
import com.luoyang.llyreader.presenter.IImportBookPresenter;
import com.luoyang.llyreader.view.IImportBookView;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 导入图书present
 *
 * @author luoyang
 * @date 2023/3/01
 */
public class ImportBookPresenterImpl extends BasePresenter<IImportBookView> implements IImportBookPresenter {

    @Override
    public void searchLocationBook() {
        Observable.create(new ObservableOnSubscribe<File>() {
                    @Override
                    public void subscribe(ObservableEmitter<File> e) throws Exception {
                        LogUtils.INSTANCE.d("searchLocationBook");
                        if (Environment.getExternalStorageState().equals(
                                Environment.MEDIA_MOUNTED)) {
                            LogUtils.INSTANCE.d("searchLocationBook searchBook");
                            searchBook(e, new File(Environment.getExternalStorageDirectory().getAbsolutePath()));
                        }
                        e.onComplete();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SimpleObserver<File>() {
                    @Override
                    public void onNext(File value) {
                        mView.addNewBook(value);
                    }

                    @Override
                    public void onComplete() {
                        mView.searchFinish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    private void searchBook(ObservableEmitter<File> e, File parentFile) {
        if (null != parentFile && parentFile.listFiles().length > 0) {
            File[] childFiles = parentFile.listFiles();
            for (int i = 0; i < childFiles.length; i++) {
                if (childFiles[i].isFile()
                        && childFiles[i].getName()
                        .substring(childFiles[i].getName().lastIndexOf(".") + 1)
                        .equalsIgnoreCase("txt")
                        //100kb
                        && childFiles[i].length() > 100 * 1024) {
                    e.onNext(childFiles[i]);
                    continue;
                }
                if (childFiles[i].isDirectory() && childFiles[i].listFiles().length > 0) {
                    searchBook(e, childFiles[i]);
                }
            }
        }

    }

    @Override
    public void importBooks(List<File> books) {
        Observable.fromIterable(books).flatMap(new Function<File, ObservableSource<LocBookShelfBean>>() {
                    @Override
                    public ObservableSource<LocBookShelfBean> apply(File file) throws Exception {
                        return ImportBookModelImpl.getInstance().importBook(file);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<LocBookShelfBean>() {
                    @Override
                    public void onNext(LocBookShelfBean value) {
                        if (value.getNew()) {
                            RxBus.get().post(RxBusTag.HAD_ADD_BOOK, value.getBookShelfBean());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.addError();
                    }

                    @Override
                    public void onComplete() {
                        mView.addSuccess();
                    }
                });

    }

    @Override
    public void detachView() {

    }
}
