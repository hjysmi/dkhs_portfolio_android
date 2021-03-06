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
import android.content.SharedPreferences.Editor;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.security.SecurePreferences;

/**
 * @author zjz
 * @version 1.0
 * @ClassName PortfolioPreferenceManager
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-17 下午3:53:07
 */
public class PortfolioPreferenceManager {
    // Preference的文件名
    private static final String PREFERENCE_NAME = "dkhs_preference";

    public static final String KEY_HAS_LOADSEARCHSTOCK = "key_hasloadsearchstock";
    public static final String KEY_IS_REQUESTTESTSERVER = "key_is_requesttestserver";
    public static final String KEY_USERNAME = "key_username";
    public static final String KEY_USERID = "key_userid";
    public static final String KEY_USER_HEADER_URL = "key_user_header_url";
    public static final String KEY_USER_ACCOUNT = "key_user_account";
    public static final String KEY_APP_ID = "key_app_id";
    public static final String KEY_LAST_LOAD_DATETIME = "key_last_load_datetime";
    public static final String KEY_APP_URL = "key_app_url";
    public static final String KEY_APP_INTRODUS = "key_app_INTRODUS";
    public static final String KEY_KLIN_COMPLEX = "key_klin_complex";
    public static final String KEY_KLIN_DEPUTY = "key_klin_DEPUTY";
    public static final String KEY_VERSIONY = "key_version";
    public static final String KEY_CLICK_TIME = "key_click_time";
    public static final String KEY_APP_UPDATE_INFO = "key_app_update_info";
    public static final String KEY_RECOMMEND_FUND_JSON = "key_recommend_fund_json";
    public static final String KEY_RECOMMEND_FUND_MANAGER_JSON = "key_recommend_fund_manager_json";
    public static final String KEY_RECOMMEND_PORTFOLIO_JSON = "key_recommend_portfolio_json";
    public static final String KEY_HOME_BANNER_JSON = "key_home_banner_json";
    public static final String KEY_HOME_REWARD_JSON = "key_home_reward_json";
    public static final String KEY_HOME_TOPIC_JSON = "key_home_topic_json";
    public static final String KEY_HOME_SUB_BANNER_JSON = "key_home_sub_banner_json";
    public static final String KEY_VERIFIED = "key_verified";
    public static final String KEY_VERIFIED_TYPE = "key_verified_type";
    public static final String KEY_VERIFIED_STATUS = "key_verified_status";
    public static final String KEY_FUNDS_HOME_MAIN_VALUE_JSON = "key_funds_home_main_value_json";
    public static final String KEY_FUNDS_HOME_SPECIAL_BANNERS_JSON = "key_funds_home_special_banners_json";
    public static final String KEY_FUNDS_HOME_SPECIAL_LINES_JSON = "key_funds_home_special_lines_json";
    public static final String KEY_FUNDS_HOME_SPECIAL_FINANCINGS_JSON = "key_funds_home_special_financings_json";
    public static final String KEY_FUNDS_HOME_SPECIAL_FUNDMANAGERS_JSON = "key_funds_home_special_fundmanagers_json";
    public static final String KEY_STOCK_ALL_MARKET_JSON = "key_stock_all_market_json";
    public static final String KEY_REWARDS = "key_rewards";
    public static final String KEY_HOT_TOPICS = "key_hot_topics";
    public static final String KEY_LATEST_TOPICS = "key_latest_topics";
    public static final String KEY_TOPIC_BANNER = "key_topic_banner";
    public static final String KEY_STICK_TOPIC = "key_stick_topic";
    public static final String KEY_FIRST_IN= "key_first_in";



    public static final String KEY_GESTRUE_PASSWORD_ISOPEN = "key_gestrue_password_isopen";

    /**
     * app 显示新消息的key
     */
    // public static final String S_APP_NEW_MESSAGE="app_new_message";
    public static SecurePreferences getSharePreferences() {
        return new SecurePreferences(PortfolioApplication.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE), "dkhs");
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

    public static void setRequestByTestServer(boolean isRequestTest) {
        saveValue(KEY_IS_REQUESTTESTSERVER, isRequestTest);
    }

    public static boolean isRequestByTestServer() {
        return getBooleanValue(KEY_IS_REQUESTTESTSERVER);
    }

}
