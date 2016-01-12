package com.dkhs.portfolio.common;

import com.dkhs.portfolio.bean.ThreePlatform;
import com.dkhs.portfolio.bean.UserEntity;

import cn.sharesdk.framework.Platform;

/**
 * 全局静态变量存储
 *
 * @author zhangcm
 */
public class GlobalParams {

    /**
     * wap的代理ip
     */
    public static String PROXY_IP = "";
    /**
     * wap的代理端口
     */
    public static int PROXY_PORT = 0;
    /**
     * 屏幕宽度
     */
    public static int WIN_WIDTH = 0;
    /**
     * 用户登录的标记
     */
    public static boolean isLogin = false;
    /**
     * 用户打开我的资产是否需要输入手势密码的标记
     */
    public static boolean needShowGesture = true;
    /**
     * 用户打开我的资产是否需要输入手势密码的标记
     */
    public static boolean isGestureOpen = false;
//    /**
//     * 用户打开我的资产是否需要输入手势密码的标记
//     */
//    public static boolean needShowGestureImmediately = false;
    /**
     * 用户名
     */
    public static String USERNAME = "";

    public static String MOBILE = "";
    public static String CALLID = "";

    public static String ACCESS_TOCKEN = "";
    public static UserEntity LOGIN_USER = null;

    public static boolean IS_VOICE_CONNECTED = false;
    public static boolean IS_CONFERENCE_CONNECTED = false;
    public static boolean IS_APP_SHOWING = false;
    public static int SHOW_GESTURE_DELAY = 1000 * 15;
    public static int GESTURE_LOCK_TIME = 1000 * 15;

    public static  Platform plat;
    public static  String platname;
    public static  ThreePlatform platData;


    public static void clearUserInfo() {
        GlobalParams.ACCESS_TOCKEN = null;
        GlobalParams.MOBILE = null;
        GlobalParams.LOGIN_USER = null;
    }

    public static void clearPlatformInfo(){
        plat = null;
        platname = "";
        platData = null;
    }

}
