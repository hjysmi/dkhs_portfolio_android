/**
 * @Title PortfolioPreferenceManager.java
 * @Package com.dkhs.portfolio.utils
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-17 下午3:53:07
 * @version V1.0
 */
package com.dkhs.portfolio.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.dkhs.portfolio.app.PortfolioApplication;

/**
 * @ClassName PortfolioPreferenceManager
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-17 下午3:53:07
 * @version 1.0
 */
public class PortfolioPreferenceManager {
    // Preference的文件名
    private static final String PREFERENCE_NAME = "dkhs_preference";

    public static final String KEY_HAS_LOADSEARCHSTOCK = "key_hasloadsearchstock";

    public static SharedPreferences getSharePreferences() {
        return PortfolioApplication.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static Editor getSharePreferencesEditor() {
        return getSharePreferences().edit();
    }

    public static String getStringValue(String key) {
        return getSharePreferences().getString(key, null);
    }

    public static int getIntValue(String key) {
        return getSharePreferences().getInt(key, 0);
    }

    public static long getLongValue(String key) {
        return getSharePreferences().getLong(key, 1L);
    }

    public static boolean getBooleanValue(String key) {
        return getSharePreferences().getBoolean(key, false);
    }

    public static void saveValue(String key, String value) {
        getSharePreferencesEditor().putString(key, value).commit();
    }

    public static void saveValue(String key, boolean value) {
        getSharePreferencesEditor().putBoolean(key, value).commit();
    }

    public static void saveValue(String key, int value) {
        getSharePreferencesEditor().putInt(key, value).commit();
    }

    public static void saveValue(String key, long value) {
        getSharePreferencesEditor().putLong(key, value).commit();
    }

    public static boolean hasLoadSearchStock() {
        return getBooleanValue(KEY_HAS_LOADSEARCHSTOCK);
    }

    public static void setLoadSearchStock() {
        saveValue(KEY_HAS_LOADSEARCHSTOCK, true);
    }
}