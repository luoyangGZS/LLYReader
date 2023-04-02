package com.luoyang.llyreader.view;

import com.luoyang.basemvp.IView;

import java.io.File;

/**
 * 导入图书试图
 *
 * @author luoyang
 * @date 2023/3/01
 */
public interface IImportBookView extends IView {
    /**
     * 新增书籍
     * @param newFile
     */
    void addNewBook(File newFile);

    /**
     * 书籍搜索完成
     */
    void searchFinish();

    /**
     * 添加成功
     */
    void addSuccess();

    /**
     * 添加失败
     */
    void addError();
}
