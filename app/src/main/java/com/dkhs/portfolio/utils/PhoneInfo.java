/**
 * @Title PhoneInfo.java
 * @Package com.dkhs.portfolio.utils
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-11-25 下午3:32:10
 * @version V1.0
 */
package com.dkhs.portfolio.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.dkhs.portfolio.app.PortfolioApplication;

/**
 * @author zjz
 * @version 1.0
 * @ClassName PhoneInfo
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-11-25 下午3:32:10
 */
public class PhoneInfo {
    //
    private static final String WECHAT_PACKNAME = "com.tencent.mm";

    public static boolean hasInstallApp(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo pInfo = packageManager.getPackageInfo(packageName,
                    PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            // 判断是否获取到了对应的包名信息
            if (pInfo != null) {
                return true;
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasInstallWechat() {
        return hasInstallApp(PortfolioApplication.getInstance(), WECHAT_PACKNAME);
    }



}
