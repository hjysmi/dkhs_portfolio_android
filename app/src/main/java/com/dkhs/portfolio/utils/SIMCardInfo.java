package com.dkhs.portfolio.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * class name：SIMCardInfo<BR>
 * class description：读取Sim卡信息<BR>
 * 
 */
public class SIMCardInfo {
	/**
	 * TelephonyManager提供设备上获取通讯服务信息的入口。 应用程序可以使用这个类方法确定的电信服务商和国家 以及某些类型的用户访问信息。
	 * 应用程序也可以注册一个监听器到电话收状态的变化。不需要直接实例化这个类
	 * 使用Context.getSystemService(Context.TELEPHONY_SERVICE)来获取这个类的实例。
	 */
	private TelephonyManager telephonyManager;
	/**
	 * 国际移动用户识别码
	 */
	private String IMSI;

	public SIMCardInfo(Context context) {
		telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
	}

	/**
	 * Role:获取当前设置的电话号码
	 */
	public String getNativePhoneNumber() {
		String NativePhoneNumber = null;
		NativePhoneNumber = telephonyManager.getLine1Number();
		return NativePhoneNumber;
	}

	/**
	 * Role:Telecom service providers获取手机服务商信息 <BR>
	 * 需要加入权限<uses-permission
	 * android:name="android.permission.READ_PHONE_STATE"/> <BR>
	 * 
	 */
	public String getProvidersName() {
		String ProvidersName = null;
		// 返回唯一的用户ID;就是这张卡的编号神马的
		IMSI = telephonyManager.getSubscriberId();
		// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
		if(TextUtils.isEmpty(IMSI))
			return null;
		System.out.println(IMSI);
		if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
			ProvidersName = "中国移动";
		} else if (IMSI.startsWith("46001")) {
			ProvidersName = "中国联通";
		} else if (IMSI.startsWith("46003")) {
			ProvidersName = "中国电信";
		}
		return ProvidersName;
	}
	
	/**
     * 验证手机号码
     * 
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9])|(147))\\d{8}$");
            Matcher m = p.matcher(mobiles);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
    public static boolean isEmail(String email) {
    	String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    	Pattern p = Pattern.compile(str);
    	Matcher m = p.matcher(email);

    	return m.matches();
    	}
}