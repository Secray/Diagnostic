package com.wingtech.diagnostic.util;

/**
 * Created by xiekui on 17-8-23.
 */

public class TestItem {
    private int mId;
    private String mName;
    private int mIcon;
    private int mImg;
    private boolean mIsAutoTest;
    private boolean mIsDisable;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        this.mIcon = icon;
    }

    public int getImg() {
        return mImg;
    }

    public void setImg(int img) {
        this.mImg = img;
    }

    public boolean isDisable() {
        return mIsDisable;
    }

    public void setDisable(boolean isDisable) {
        this.mIsDisable = isDisable;
    }

    public boolean isAutoTest() {
        return mIsAutoTest;
    }

    public void setAutoTest(boolean isAutoTest) {
        this.mIsAutoTest = isAutoTest;
    }
}
