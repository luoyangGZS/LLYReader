package com.luoyang.llyreader.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

/**
 * 章节列表
 *
 * @author luoyang
 * @date 2023/3/01
 */
@Entity
public class ChapterListBean implements Parcelable, Cloneable {

    /**
     * 对应BookInfoBean noteUrl
     */
    private String noteUrl;
    /**
     * 前章节数
     */
    private int durChapterIndex;
    /**
     * 当前章节对应的文章地址
     */
    @Id
    private String durChapterUrl;
    /**
     * 当前章节名称
     */
    private String durChapterName;

    private String tag;

    private Boolean hasCache;
    @Transient
    private BookContentBean bookContentBean;

    public ChapterListBean() {
    }


    protected ChapterListBean(Parcel in) {
        noteUrl = in.readString();
        durChapterIndex = in.readInt();
        durChapterUrl = in.readString();
        durChapterName = in.readString();
        tag = in.readString();
        byte tmpHasCache = in.readByte();
        hasCache = tmpHasCache == 0 ? null : tmpHasCache == 1;
        bookContentBean = in.readParcelable(BookContentBean.class.getClassLoader());
    }


    @Generated(hash = 1225922702)
    public ChapterListBean(String noteUrl, int durChapterIndex, String durChapterUrl,
            String durChapterName, String tag, Boolean hasCache) {
        this.noteUrl = noteUrl;
        this.durChapterIndex = durChapterIndex;
        this.durChapterUrl = durChapterUrl;
        this.durChapterName = durChapterName;
        this.tag = tag;
        this.hasCache = hasCache;
    }

    public static final Creator<ChapterListBean> CREATOR = new Creator<ChapterListBean>() {
        @Override
        public ChapterListBean createFromParcel(Parcel in) {
            return new ChapterListBean(in);
        }

        @Override
        public ChapterListBean[] newArray(int size) {
            return new ChapterListBean[size];
        }
    };

    public String getNoteUrl() {
        return this.noteUrl;
    }

    public void setNoteUrl(String noteUrl) {
        this.noteUrl = noteUrl;
    }

    public int getDurChapterIndex() {
        return this.durChapterIndex;
    }

    public void setDurChapterIndex(int durChapterIndex) {
        this.durChapterIndex = durChapterIndex;
    }

    public String getDurChapterUrl() {
        return this.durChapterUrl;
    }

    public void setDurChapterUrl(String durChapterUrl) {
        this.durChapterUrl = durChapterUrl;
    }

    public String getDurChapterName() {
        return this.durChapterName;
    }

    public void setDurChapterName(String durChapterName) {
        this.durChapterName = durChapterName;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Boolean getHasCache() {
        return this.hasCache;
    }

    public void setHasCache(Boolean hasCache) {
        this.hasCache = hasCache;
    }

    public BookContentBean getBookContentBean() {
        return bookContentBean;
    }

    public void setBookContentBean(BookContentBean bookContentBean) {
        this.bookContentBean = bookContentBean;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(noteUrl);
        parcel.writeInt(durChapterIndex);
        parcel.writeString(durChapterUrl);
        parcel.writeString(durChapterName);
        parcel.writeString(tag);
        parcel.writeByte((byte) (hasCache == null ? 0 : hasCache ? 1 : 2));
        parcel.writeParcelable(bookContentBean, i);
    }

    @Override
    public ChapterListBean clone() {
        try {
            ChapterListBean clone = (ChapterListBean) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
