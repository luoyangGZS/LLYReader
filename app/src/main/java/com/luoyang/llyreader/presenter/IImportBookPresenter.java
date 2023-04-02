package com.luoyang.llyreader.presenter;


import com.luoyang.basemvp.IPresenter;

import java.io.File;
import java.util.List;

/**
 * 导入图书present
 *
 * @author luoyang
 * @date 2023/3/01
 */
public interface IImportBookPresenter extends IPresenter {
    /**
     * 搜索本地
     */
    void searchLocationBook();

    /**
     * 导入本地电子书
     *
     * @param books txt书架
     */
    void importBooks(List<File> books);
}
