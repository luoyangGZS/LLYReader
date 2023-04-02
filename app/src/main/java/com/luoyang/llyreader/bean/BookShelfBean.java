package com.luoyang.llyreader.bean;


import android.os.Parcel;
import android.os.Parcelable;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 书架信息
 *
 * @author luoyang
 * @date 2023/3/01
 */
@Entity
public class BookShelfBean implements Parcelable, Cloneable {
    /**
     * 更新时间间隔 至少
     */
    @Transient
    public static final long REFRESH_TIME = 5 * 60 * 1000;
    @Transient
    public static final String LOCAL_TAG = "loc_book";

    /**
     * 对应BookInfoBean noteUrl
     */
    @Id
    private String noteUrl;

    /**
     * 当前章节 （包括番外）
     */
    private int durChapter;

    /**
     * 当前章节位置   用页码
     * BookContentView.DURPAGEINDEXBEGIN
     */
    private int durChapterPage = -1;

    /**
     * 最后阅读时间
     */
    private long finalDate;

    private String tag;

    @Transient
    private BookInfoBean bookInfoBean = new BookInfoBean();

    public BookShelfBean() {
    }

    protected BookShelfBean(Parcel in) {
        noteUrl = in.readString();
        durChapter = in.readInt();
        durChapterPage = in.readInt();
        finalDate = in.readLong();
        tag = in.readString();
        bookInfoBean = in.readParcelable(BookInfoBean.class.getClassLoader());
    }

    @Generated(hash = 2028192361)
    public BookShelfBean(String noteUrl, int durChapter, int durChapterPage, long finalDate,
            String tag) {
        this.noteUrl = noteUrl;
        this.durChapter = durChapter;
        this.durChapterPage = durChapterPage;
        this.finalDate = finalDate;
        this.tag = tag;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(noteUrl);
        dest.writeInt(durChapter);
        dest.writeInt(durChapterPage);
        dest.writeLong(finalDate);
        dest.writeString(tag);
        dest.writeParcelable(bookInfoBean, flags);
    }

    public static final Creator<BookShelfBean> CREATOR = new Creator<BookShelfBean>() {
        @Override
        public BookShelfBean createFromParcel(Parcel in) {
            return new BookShelfBean(in);
        }

        @Override
        public BookShelfBean[] newArray(int size) {
            return new BookShelfBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getNoteUrl() {
        return this.noteUrl;
    }

    public void setNoteUrl(String noteUrl) {
        this.noteUrl = noteUrl;
    }

    public int getDurChapter() {
        return this.durChapter;
    }

    public void setDurChapter(int durChapter) {
        this.durChapter = durChapter;
    }

    public int getDurChapterPage() {
        return this.durChapterPage;
    }

    public void setDurChapterPage(int durChapterPage) {
        this.durChapterPage = durChapterPage;
    }

    public long getFinalDate() {
        return this.finalDate;
    }

    public void setFinalDate(long finalDate) {
        this.finalDate = finalDate;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public BookInfoBean getBookInfoBean() {
        return bookInfoBean;
    }

    public void setBookInfoBean(BookInfoBean bookInfoBean) {
        this.bookInfoBean = bookInfoBean;
    }

    @Override
    public BookShelfBean clone() {
        try {
            BookShelfBean clone = (BookShelfBean) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}