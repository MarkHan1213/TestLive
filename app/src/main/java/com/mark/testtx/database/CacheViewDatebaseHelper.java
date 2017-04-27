/**
 * ::
 */
package com.mark.testtx.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * view json缓存类
 */
public class CacheViewDatebaseHelper extends SDSQLiteOpenHelper {

    private static final String VIEW_DATABASE_NAME = "zfwx_viewcache.db";
    private static final int VIEW_DATABASE_VERSION = 8;

    //初始化helper
    public CacheViewDatebaseHelper(Context context) {
        super(context, VIEW_DATABASE_NAME, null, VIEW_DATABASE_VERSION);
    }

    //删除
    public void deleteTables(SQLiteDatabase db) {

        try {
            db.execSQL(CacheViewDao.DROP_VIEW_CACHE_TABLE_SQL);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //初始化
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            deleteTables(db);
            db.execSQL(CacheViewDao.CREATE_VIEW_CACHE_TABLE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //更新（在新版本时）
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    //重启
    public void resetDB(SQLiteDatabase db) {
        onCreate(db);
    }

}
