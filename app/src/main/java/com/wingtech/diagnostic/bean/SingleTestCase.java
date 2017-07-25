package com.wingtech.diagnostic.bean;

/**
 * Created by xiekui on 17-7-19.
 */

public final class SingleTestCase extends BaseTestCase {
    private boolean mIsPassed;
    private boolean mIsShowResult;

    public boolean isPassed() {
        return mIsPassed;
    }

    public void setPassed(boolean passed) {
        mIsPassed = passed;
    }

    public boolean isShowResult() {
        return mIsShowResult;
    }

    public void setShowResult(boolean isShowResult) {
        this.mIsShowResult = isShowResult;
    }
}
