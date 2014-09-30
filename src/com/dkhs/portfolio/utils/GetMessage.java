package com.dkhs.portfolio.utils;

import java.text.SimpleDateFormat;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

/**
 * @ClassName: GetMessage
 * @Description: 取得指定条件的单条短信内容
 * @date 2014-3-02 下午4:35:30
 * @version 1.0
 */
public class GetMessage {

	Context context = null;
	public String[] proj;
	public String selection;
	public String[] selectionArgment;
	public String url;
	public String orderBy;

	/**
	 * 取得信息的构造函数
	 * 
	 * @param smsurl
	 *            短信的收信箱，建议为：content://sms/inbox
	 * @param con
	 *            当前上下文
	 * @param projection
	 *            要查询的哪些列
	 * @param selection1
	 *            查询条件
	 * @param selectionArgs
	 *            查询条件的值，对应参数projection数组
	 */
	public GetMessage(Context con, String smsurl, String[] projection,
			String selection1, String[] selectionArgs, String order) {
		context = con;
		url = smsurl;
		proj = projection;
		selection = selection1;
		selectionArgment = selectionArgs;
		orderBy = order;

	}

	/**
	 * 返回读取到的信息内容
	 * 
	 * @return 返回为信息内容String格式，若未查询到信息，返回空值。
	 */
	public String getSmsOfPhone() {
		// 这里枚举出短信获取的url
		StringBuilder smsBuilder = new StringBuilder();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss");
		try {
			ContentResolver cr = context.getContentResolver();
			Uri uri = Uri.parse(url);
			Cursor cur = cr.query(uri, proj, selection, selectionArgment,
					orderBy);
			if (cur.moveToFirst()) {
				for (int i = 0; i < proj.length; i++) {
					smsBuilder.append(proj[i]);
				}
			} else {
				smsBuilder.append("");
			}
		} catch (SQLiteException ex) {
			Log.d("读取到信息", ex.getMessage());
		}
		return smsBuilder.toString();

	}

}
