package com.dkhs.portfolio.common;

import com.dkhs.portfolio.bean.UserEntity;

/**
 * 全局静态变量存储
 * 
 * @author zhangcm
 * 
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
}
