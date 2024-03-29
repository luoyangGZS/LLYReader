package com.luoyang.llyreader.bean;

import android.os.Parcel;
import android.os.Parcelable;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

/**
 * 书本信息
 *
 * @author luoyang
 * @date 2023/3/01
 */
@Entity
public class BookInfoBean implements Parcelable, Cloneable {

    @Transient
    public static final long REFRESH_DUR = 10 * 60 * 1000;

    /**
     * 小说名
     */
    private String name;

    private String tag;

    /**
     * 如果是来源网站   则小说根地址 /如果是本地  则是小说本地MD5
     */
    @Id
    private String noteUrl;

    /**
     * 章节目录地址
     */
    private String chapterUrl;

    /**
     * 章节列表
     */
    @Transient
    private List<ChapterListBean> chapterList = new ArrayList<>();

    /**
     * 章节最后更新时间
     */
    private long finalRefreshData;

    /**
     * 小说封面
     */
    private String coverUrl;

    /**
     * 作者
     */
    private String author;
    /**
     * 简介
     */
    private String introduce;
    /**
     * 来源
     */
    private String origin;

    @Generated(hash = 506126111)
    public BookInfoBean() {
    }

    protected BookInfoBean(Parcel in) {
        name = in.readString();
        tag = in.readString();
        noteUrl = in.readString();
        chapterUrl = in.readString();
        chapterList = in.createTypedArrayList(ChapterListBean.CREATOR);
        finalRefreshData = in.readLong();
        coverUrl = in.readString();
        author = in.readString();
        introduce = in.readString();
        origin = in.readString();
    }

    @Generated(hash = 1627552162)
    public BookInfoBean(String name, String tag, String noteUrl, String chapterUrl,
            long finalRefreshData, String coverUrl, String author, String introduce, String origin) {
        this.name = name;
        this.tag = tag;
        this.noteUrl = noteUrl;
        this.chapterUrl = chapterUrl;
        this.finalRefreshData = finalRefreshData;
        this.coverUrl = coverUrl;
        this.author = author;
        this.introduce = introduce;
        this.origin = origin;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(tag);
        dest.writeString(noteUrl);
        dest.writeString(chapterUrl);
        dest.writeTypedList(chapterList);
        dest.writeLong(finalRefreshData);
        dest.writeString(coverUrl);
        dest.writeString(author);
        dest.writeString(introduce);
        dest.writeString(origin);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BookInfoBean> CREATOR = new Creator<BookInfoBean>() {
        @Override
        public BookInfoBean createFromParcel(Parcel in) {
            return new BookInfoBean(in);
        }

        @Override
        public BookInfoBean[] newArray(int size) {
            return new BookInfoBean[size];
        }
    };

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getNoteUrl() {
        return this.noteUrl;
    }

    public void setNoteUrl(String noteUrl) {
        this.noteUrl = noteUrl;
    }

    public String getChapterUrl() {
        return this.chapterUrl;
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }


    public List<ChapterListBean> getChapterList() {
        return chapterList;
    }

    public void setChapterList(List<ChapterListBean> chapterList) {
        this.chapterList = chapterList;
    }

    public void addChapterList(List<ChapterListBean> chapterlist) {
        this.chapterList.addAll(chapterlist);
    }


    public long getFinalRefreshData() {
        return this.finalRefreshData;
    }

    public void setFinalRefreshData(long finalRefreshData) {
        this.finalRefreshData = finalRefreshData;
    }

    public String getCoverUrl() {
        return this.coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIntroduce() {
        return this.introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getOrigin() {
        return this.origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }


    @Override
    public BookInfoBean clone() {
        try {
            BookInfoBean clone = (BookInfoBean) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
