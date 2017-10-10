package com.wingtech.diagnostic.util;

import android.content.Context;
import android.os.Build;

import com.android.helper.Helper;
import com.wingtech.diagnostic.App;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by xiekui on 17-8-23.
 */

public class TestItemHandler extends Thread {
    private WeakReference<Context> mContext;
    private String mConfigName;
    public TestItemHandler(Context context) {
        this.mContext = new WeakReference<>(context);
        mConfigName = "config/config_" + Build.MODEL + ".xml";
    }

    @Override
    public void run() {
        parseConfig();
    }

    private void parseConfig() {
        InputStream is = null;
        try {
            is = mContext.get().getResources().getAssets().open(mConfigName);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(is, "UTF-8");
            int eventType = xmlPullParser.getEventType();
            TestItem item = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.i("start parse config");
                        break;
                    case XmlPullParser.START_TAG:
                        if ("TEST_ITEM".equals(xmlPullParser.getName())) {
                            item = new TestItem();
                            item.setId(Integer.parseInt(xmlPullParser.getAttributeValue(0)));
                            item.setName(xmlPullParser.getAttributeValue(1));
                            item.setDisable("1".equals(xmlPullParser.getAttributeValue(2)));
                            item.setAutoTest("1".equals(xmlPullParser.getAttributeValue(3)));
                            item.setIcon(getId(xmlPullParser.getAttributeValue(4)));
                            item.setImg(getId(xmlPullParser.getAttributeValue(5)));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("TEST_ITEM".equals(xmlPullParser.getName())
                                && item != null && !item.isDisable()) {
                            App.addItems(item);
                        }
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int getId(String name) {
        return mContext.get().getResources().getIdentifier(name, "drawable", mContext.get().getPackageName());
    }
}
