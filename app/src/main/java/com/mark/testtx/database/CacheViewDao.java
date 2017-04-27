package com.mark.testtx.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mark.testtx.AppData;
import com.mark.testtx.TCApplication;

import java.util.Date;

/**
 * @TODO ：json数据缓存dao
 */
public class CacheViewDao {
    private static final String TAG = "CacheViewDao";
    private static CacheViewDao cacheViewDao;
    //创建存储list列表json项表的sql语句
    public static final String CREATE_VIEW_CACHE_TABLE = "create table " + AppData.VIEW_LIST_JSON_TABLE + " ("
            + AppData.KEY + " text primary key, "
            + AppData.VALUE + " text not null,"
            + AppData.TIME + " integer,"
            + " text);";
    //删除存储json表的sql语句
    public static final String DROP_VIEW_CACHE_TABLE_SQL = "drop table if exists " + AppData.VIEW_LIST_JSON_TABLE;

    private CacheViewDatebaseHelper cacheViewDatebaseHelper;

    //初始化CacheViewDao
    private CacheViewDao(Context context) {
        cacheViewDatebaseHelper = new CacheViewDatebaseHelper(context);
    }

    //单例
    public static CacheViewDao getInstance() {
        if (cacheViewDao == null) {
            cacheViewDao = new CacheViewDao(TCApplication.getApplication());
        }
        return cacheViewDao;
    }

    //关闭
    public void close() {
        if (cacheViewDao != null && cacheViewDatebaseHelper != null) {
            cacheViewDatebaseHelper.close();
            cacheViewDao = null;
        }
    }

    //插入操作
    public void insertJsonDate(String key, String value) {
        SQLiteDatabase db = null;
        try {
            db = cacheViewDatebaseHelper.getWritableDatabase();
            db.beginTransaction();        //手动设置开始事务
            ContentValues values = new ContentValues();
            values.put(AppData.KEY, key);
            values.put(AppData.VALUE, value);
            values.put(AppData.TIME, new Date().getTime());
            int isUpdate = db.update(AppData.VIEW_LIST_JSON_TABLE, values, AppData.KEY + " = '" + key + "'", null);
            if (isUpdate <= 0) {
                db.insert(AppData.VIEW_LIST_JSON_TABLE, "", values); //参数2：如果你想插入空值，那么你必须指定它的所在的列
            }

            db.setTransactionSuccessful();        //设置事务处理成功，不设置会自动回滚不提交

        } catch (Exception e) {

            Log.e(TAG, e.getMessage());

        } finally {
            if (db != null) {
                db.endTransaction();        //处理完成
            }
        }
    }

    //删除操作
    public void deleteJsonDate(long time) {
        SQLiteDatabase db = null;
        try {
            db = cacheViewDatebaseHelper.getWritableDatabase();
            db.beginTransaction();        //手动设置开始事务
            db.delete(AppData.VIEW_LIST_JSON_TABLE, AppData.TIME + "<?", new String[]{String.valueOf(time)});
            db.setTransactionSuccessful();        //设置事务处理成功，不设置会自动回滚不提交
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (db != null) {
                db.endTransaction();        //处理完成
            }
        }
    }

    //删除操作
    public void deleteAJsonDateById(String id) {
        if (id == null || id.length() <= 0) {
            return;
        }
        SQLiteDatabase db = null;
        try {
            db = cacheViewDatebaseHelper.getWritableDatabase();
            db.beginTransaction();        //手动设置开始事务
            db.delete(AppData.VIEW_LIST_JSON_TABLE, AppData.KEY + "=", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();        //设置事务处理成功，不设置会自动回滚不提交
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (db != null) {
                db.endTransaction();        //处理完成
            }
        }
    }

    //修改操作 其中key为表id,name：要修改的字段名，Date:要修改的值
    public void upDateJsonDate(String key) {
        SQLiteDatabase db = null;
        try {
            db = cacheViewDatebaseHelper.getWritableDatabase();
            db.beginTransaction();        //手动设置开始事务
            ContentValues values = new ContentValues();
//			values.put(AppData.LAST_CHECKIN_TIME, new Date().getTime());//key为字段名，value为值 
            db.update(AppData.VIEW_LIST_JSON_TABLE, values, AppData.KEY + " = '" + key + "'", null);
            db.setTransactionSuccessful();        //设置事务处理成功，不设置会自动回滚不提交
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (db != null) {
                db.endTransaction();        //处理完成
            }
        }
    }

    //查询缓存表操作
    public String[] qureyCachejson(String key) {
        String[] dataArr = new String[3];
        try {

            SQLiteDatabase db = cacheViewDatebaseHelper.getReadableDatabase();
            Cursor cursor = db.query(AppData.VIEW_LIST_JSON_TABLE, new String[]{AppData.VALUE, AppData.TIME}, AppData.KEY
                    + "='" + key + "'", null, null, null, null);
            while (cursor.moveToNext()) {
                dataArr[0] = cursor.getString(0);//jsonString
                dataArr[1] = cursor.getString(1); //timestamp
                dataArr[2] = cursor.getString(2); //last chinkin time
            }
            cursor.close();
            Log.i("ViewJsonCacheDao", "查询得到的数据：time：" + dataArr[1] + " totalRecord:" + dataArr[2]);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return dataArr;
    }

    //查询缓存表总数据数
    public int qureyCachejsonCount() {
        int cachejsonCount = 0;
        try {
            SQLiteDatabase db = cacheViewDatebaseHelper.getReadableDatabase();
            Cursor cursor = db.query(AppData.VIEW_LIST_JSON_TABLE, new String[]{"count(*)"}, null, null, null, null, null);
            while (cursor.moveToNext()) {
                cachejsonCount = cursor.getInt(0);//jsonString
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return cachejsonCount;
    }

}
