package com.luoyang.llyreader.model;

import com.luoyang.llyreader.bean.LocBookShelfBean;

import java.io.File;

import io.reactivex.Observable;

/**
 * @author luoyang
 * @date 2023/3/6
 */
public interface IImportBookModel  {
    /**
     * 导入本地数据
     * @param book 本地txt
     * @return
     */
    Observable<LocBookShelfBean> importBook(File book);
}