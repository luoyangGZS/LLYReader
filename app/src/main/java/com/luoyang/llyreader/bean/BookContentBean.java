package com.luoyang.llyreader.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;


/**
 * 书本内容
 *
 * @author luoyang
 * @date 2023/3/01
 */
@Entity
public class BookContentBean implements Parcelable {
    /**
     * 对应BookInfoBean noteUrl
     */
    @Id
    private String durChapterUrl;

    /**
     * 当前章节  （包括番外）
     */
    private int durChapterIndex;

    /**
     * 当前章节内容
     */
    private String durChapterContent;

    /**
     * 来源  某个网站/本地
     */
    private String tag;

    @Transient
    private Boolean isRight = true;

    @Transient
    private List<String> lineContent = new ArrayList<>();

    @Transient
    private float lineSize;

    public BookContentBean() {

    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(durChapterUrl);
        dest.writeInt(durChapterIndex);
        dest.writeString(durChapterContent);
        dest.writeString(tag);
        dest.writeByte((byte) (isRight == null ? 0 : isRight ? 1 : 2));
        dest.writeStringList(lineContent);
        dest.writeFloat(lineSize);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BookContentBean> CREATOR = new Creator<BookContentBean>() {
        @Override
        public BookContentBean createFromParcel(Parcel in) {
            return new BookContentBean(in);
        }

        @Override
        public BookContentBean[] newArray(int size) {
            return new BookContentBean[size];
        }
    };

    public float getLineSize() {
        return lineSize;
    }

    public void setLineSize(float lineSize) {
        this.lineSize = lineSize;
    }

    protected BookContentBean(Parcel in) {
        durChapterUrl = in.readString();
        durChapterIndex = in.readInt();
        durChapterContent = in.readString();
        tag = in.readString();
        lineContent = in.createStringArrayList();
        isRight = in.readByte() != 0;
    }


    @Generated(hash = 2014210606)
    public BookContentBean(String durChapterUrl, int durChapterIndex,
            String durChapterContent, String tag) {
        this.durChapterUrl = durChapterUrl;
        this.durChapterIndex = durChapterIndex;
        this.durChapterContent = durChapterContent;
        this.tag = tag;
    }


    public String getDurChapterUrl() {
        return durChapterUrl;
    }

    public void setDurChapterUrl(String durChapterUrl) {
        this.durChapterUrl = durChapterUrl;
    }

    public int getDurChapterIndex() {
        return durChapterIndex;
    }

    public void setDurChapterIndex(int durChapterIndex) {
        this.durChapterIndex = durChapterIndex;
    }

    public String getDurChapterContent() {
        return durChapterContent;
    }

    public void setDurChapterContent(String durChapterContent) {
        this.durChapterContent = durChapterContent;
        if (durChapterContent == null || durChapterContent.length() == 0) {
            this.isRight = false;
        }
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<String> getLineContent() {
        return lineContent;
    }

    public void setLineContent(List<String> lineContent) {
        this.lineContent = lineContent;
    }

    public Boolean getRight() {
        return isRight;
    }

    public void setRight(Boolean right) {
        isRight = right;
    }
}
