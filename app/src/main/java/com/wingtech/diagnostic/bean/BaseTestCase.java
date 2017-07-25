package com.wingtech.diagnostic.bean;

import android.graphics.drawable.Drawable;

/**
 * @author xiekui
 * @date 2017-7-18
 */
class BaseTestCase {
    protected Drawable mIcon;
    protected String mTitle;

    public Drawable getIcon() {
        return mIcon;
    }

    public void setIcon(Drawable mIcon) {
        this.mIcon = mIcon;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
