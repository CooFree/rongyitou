package com.yxh.ryt.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wangchen on 2015/11/18.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "wine.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //表--
        db.execSQL("create table rzxq_praise(_id text primary key," +//ID，主键
                "project_workID text," +
                "current_id text," +
                "isPraise text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("drop table if exists rzxq_praise");
            onCreate(db);
        }
    }
}