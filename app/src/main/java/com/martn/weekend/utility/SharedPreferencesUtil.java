package com.martn.weekend.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.martn.weekend.base.BaseApplication;

import java.util.Hashtable;

public class SharedPreferencesUtil {
    private static final Hashtable<String, Object> DATA;
    private static final String SHARED_PREFERENCES_NAME = "xdsm";

    static {
        DATA = new Hashtable();
    }

    public static Object getTmpData(String key) {
        return DATA.get(key);
    }

    public static void saveTmpData(String key, Object value) {
        if (value != null && !"".equals(value)) {
            DATA.put(key, value);
        }
    }

    public static void removeTmpData(String key) {
        DATA.remove(key);
    }

    public static void clearTmpData() {
        DATA.clear();
    }

    public static int getSize() {
        return DATA.size();
    }

    public static boolean getBooleanSharedPreference(Context context, String key, boolean defValue) {
        return getSharedPreferences(BaseApplication.context()).getBoolean(key, defValue);
    }

    public static long getLongSharedPreference(Context context, String key, long defValue) {
        return getSharedPreferences(BaseApplication.context()).getLong(key, defValue);
    }

    public static String getStringSharedPreference(Context context, String key, String defValue) {
        return getSharedPreferences(BaseApplication.context()).getString(key, defValue);
    }

    public static int getIntSharedPreference(Context context, String key, int defValue) {
        return getSharedPreferences(BaseApplication.context()).getInt(key, defValue);
    }

    public static synchronized void saveBooleanSharedPreference(Context context, String key, boolean value) {
        synchronized (SharedPreferencesUtil.class) {
            Editor editor = getSharedPreferences(BaseApplication.context()).edit();
            editor.putBoolean(key, value);
            editor.commit();
        }
    }

    public static synchronized void saveStringSharedPreference(Context context, String key, String value) {
        synchronized (SharedPreferencesUtil.class) {
            Editor editor = getSharedPreferences(BaseApplication.context()).edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public static synchronized void saveLongSharedPreference(Context context, String key, long value) {
        synchronized (SharedPreferencesUtil.class) {
            Editor editor = getSharedPreferences(BaseApplication.context()).edit();
            editor.putLong(key, value);
            editor.commit();
        }
    }

    public static synchronized void saveIntSharedPreference(Context context, String key, int value) {
        synchronized (SharedPreferencesUtil.class) {
            Editor editor = getSharedPreferences(BaseApplication.context()).edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }

    public static synchronized void removeKey(Context context, String key, SharedPreferences share) {
        synchronized (SharedPreferencesUtil.class) {
            if (share == null) {
                share = getSharedPreferences(BaseApplication.context());
            }
            Editor editor = share.edit();
            editor.remove(key);
            editor.commit();
        }
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
    }
}