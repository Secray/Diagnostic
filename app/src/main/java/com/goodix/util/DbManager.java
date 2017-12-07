package com.goodix.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbManager {

    public static void getInfo() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/sdcard/featuresmgr.db", null, SQLiteDatabase.OPEN_READONLY);
        if (db.isOpen()) {
            String[] projection = { "userid" };
            String selection = "userid=?";
            String[] selectionArgs = { "N0001" };
            String sortOrder = null;
            // Cursor c = db.query("Tbl_2", projection, selection,
            // selectionArgs, null, null, null);
            Cursor c = db.query("Tbl_2", null, null, null, null, null, null);
            if (c != null) {
                c.moveToFirst();
                do {
                    int index = c.getColumnIndex("userid");
                    String str = c.getString(index);
                } while (c.moveToNext());
            }
            c.close();
            c = null;
            db.close();
        }

    }

}
