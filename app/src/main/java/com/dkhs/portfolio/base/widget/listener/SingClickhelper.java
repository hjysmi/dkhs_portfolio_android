package com.dkhs.portfolio.base.widget.listener;

import android.os.SystemClock;

/**
 * @author zwm
 * @version 2.0
 * @ClassName SingClickhelper
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/9.
 */
public class SingClickHelper {

    private static long L_CLICK_INTERVAL = 800;
    private long preClickTime;


    public boolean clickEnable(){
        long clickTime= SystemClock.elapsedRealtime();
        if ( clickTime - preClickTime > L_CLICK_INTERVAL){
            preClickTime=clickTime;
            return true;
        }
        return false;
    }
}
