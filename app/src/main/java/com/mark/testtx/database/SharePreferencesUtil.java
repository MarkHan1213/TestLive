package com.mark.testtx.database;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.mark.testtx.AppData;
import com.mark.testtx.TCApplication;

public class SharePreferencesUtil {
    private static SharePreferencesUtil instance;
    private SharedPreferences settings;
    private Editor editor;

    /**
     * 初始化 SharePreferencesUtil()
     */
    private SharePreferencesUtil() {
        settings = TCApplication.getApplication().getSharedPreferences(AppData.PREF_SETTING_FILENAME, 0);
        if (settings != null) editor = settings.edit();
    }

    /**
     * 单例
     */
    public static SharePreferencesUtil getInstance() {
        if (instance == null) {
            instance = new SharePreferencesUtil();
        }
        return instance;
    }

    /**
     * 销毁
     */
    public void deleteSharepreference() {
        instance = null;
    }

    /**
     * 存储
     */
    public void setSharepreference(String key, Object value) {
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        }
    }

    /**
     * 删除
     */
    public void removeAll() {
        editor.clear();
        setEditorCommit();
    }

    public void removeSharepreference(String key) {
        editor.remove(key);
    }

    /**
     * 保存信息
     */
    public void setLoginValues(String account, String password, boolean autologin) {
        setSharepreference(AppData.PREF_ACCOUNT, account);
        setSharepreference(AppData.PREF_PASSWORD, password);
        setSharepreference(AppData.PREF_AUTO_LOGIN, autologin);
        setEditorCommit();
    }

    public void setVersionName(String versionName, int versionCode) {
        setSharepreference(AppData.PREF_VERSION_CODE, versionCode);
        setSharepreference(AppData.PREF_VERSION_NAME, versionName);
        setEditorCommit();
    }

    public void setIsWelcome(boolean isWelcome) {
        setSharepreference(AppData.PREF_HAS_WELCOME, isWelcome);
        setEditorCommit();
    }

    public void setPassword(String password) {
        setSharepreference(AppData.PREF_PASSWORD, password);
        setEditorCommit();
    }

    public void setLastUpgradeCheckMillis(long time) {
        setSharepreference(AppData.PREF_LAST_UPGRAGE_CHECK_TIME, time);
        setEditorCommit();
    }

    /**
     * 取数据
     */
    public long getLastUpgradeCheckTime() {
        return getSharepreferenceLong(AppData.PREF_LAST_UPGRAGE_CHECK_TIME);
    }

    public boolean getSharepreferenceBooleanDefValueFalse(String key) {
        return settings.getBoolean(key, false);
    }

    public boolean getSharepreferenceBooleanDefValueTrue(String key) // php
    {
        return settings.getBoolean(key, true);
    }

    public int getSharepreferenceInt(String key) {
        return settings.getInt(key, -1);
    }

    public long getSharepreferenceLong(String key) {
        return settings.getLong(key, -1);
    }

    public String getSharepreferenceString(String key) {
        String str = "";
        try {
            str = settings.getString(key, "");
        } catch (ClassCastException e) {
            Log.i("Database", e.getMessage());
        }
        return str;
    }

    /**
     * 回滚
     */
    public void setEditorCommit() {
        editor.commit();
    }
}
