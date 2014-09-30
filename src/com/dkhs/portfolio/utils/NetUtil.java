package com.dkhs.portfolio.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

/**
 * 判断网络
 * 
 * @author zhangcm
 * 
 */
public class NetUtil {
	private static Uri PREFERRED_APN_URI = Uri
			.parse("content://telephony/carriers/preferapn");// 4.0模拟器屏蔽掉该权限

	/**
	 * 判断网络工具
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNetWork(Context context) {
		// ①判断WIFI是否处于链接状态
		boolean isWIFI = isWIFIConnectivity(context);
		// ②判断MOBILE是否处于链接状态
		boolean isMOBILE = isMOBILEConnectivity(context);
		// ③判断是否有可以利用的通信渠道
		if (!isWIFI && !isMOBILE) {
			// 提示用户
			return false;
		}

		// ④如果链接是MOBILE，读取APN信息（proxy port），如果proxy非空，wap方式上网，需要设置代理的ip和端口
		if (isMOBILE) {
			// readAPN(context);// 读取联系人信息
		}

		return true;
	}

	/**
	 * 读取apn信息
	 * 
	 * @param context
	 */
	// private static void readAPN(Context context) {
	// ContentResolver resolver = context.getContentResolver();
	// Cursor cursor = resolver.query(PREFERRED_APN_URI, null, null, null,
	// null);// 结果只有一条数据
	// if (cursor != null && cursor.moveToFirst()) {
	// // ip:proxy
	// GlobalParams.PROXY_IP = cursor.getString(cursor.getColumnIndex("proxy"));
	// GlobalParams.PROXY_PORT = cursor.getInt(cursor.getColumnIndex("port"));
	// }
	//
	// }

	/**
	 * 判断MOBILE是否处于链接状态
	 * 
	 * @param context
	 * @return
	 */
	private static boolean isMOBILEConnectivity(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (networkInfo != null) {
			return networkInfo.isConnected();
		}
		return false;
	}

	/**
	 * 判断WIFI是否处于链接状态
	 * 
	 * @param context
	 * @return
	 */
	private static boolean isWIFIConnectivity(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo != null) {
			return networkInfo.isConnected();
		}
		return false;
	}
}
