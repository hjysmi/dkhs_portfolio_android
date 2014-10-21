package com.dkhs.portfolio.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;

/**
 * 提示信息的管理
 */

public class PromptManager {
    private static Dialog dialog;
    private AlertDialog a;

    /**
     * 默认进度条可取消
     * 
     * @param context
     * @param msg
     */
    public static void showProgressDialog(Context context, String msg) {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
        }
        View v = View.inflate(context, R.layout.progressbar, null);
        TextView tv = (TextView) v.findViewById(R.id.tv_desc);
        tv.setText(msg);
        dialog = new Dialog(context, R.style.dialog);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        dialog.setContentView(v, params);
        // dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * 默认进度条可以取消
     * 
     * @param context
     * @param msgid
     */
    public static void showProgressDialog(Context context, int msgid) {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
        }
        View v = View.inflate(context, R.layout.progressbar, null);
        TextView tv = (TextView) v.findViewById(R.id.tv_desc);
        tv.setText(msgid);
        dialog = new Dialog(context, R.style.dialog);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        dialog.setContentView(v, params);
        // dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * @param context
     * @param msgid
     * @param flag 是否可以取消进度条
     */
    public static void showProgressDialog(Context context, int msgid, boolean flag) {
        showProgressDialog(context, msgid);
        dialog.setCancelable(flag);
    }

    /**
     * @param context
     * @param msg
     * @param flag 是否可以取消进度条
     */
    public static void showProgressDialog(Context context, String msg, boolean flag) {
        showProgressDialog(context, msg);
        dialog.setCancelable(flag);
    }

    public static void closeProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 当判断当前手机没有网络时使用
     * 
     * @param context
     */
    public static void showNoNetWork(final Context context) {
        /*
         * AlertDialog.Builder builder = new Builder(context);
         * builder.setIcon(R.drawable.ic_launcher)
         * //
         * .setTitle(R.string.app_name)
         * //
         * .setMessage("当前无网络")
         * .setPositiveButton("设置", new OnClickListener() {
         * 
         * @Override
         * public void onClick(DialogInterface dialog, int which) {
         * // 跳转到系统的网络设置界面
         * if (android.os.Build.VERSION.SDK_INT > 10) {
         * context.startActivity(new Intent(
         * android.provider.Settings.ACTION_SETTINGS));
         * } else {
         * context.startActivity(new Intent(
         * android.provider.Settings.ACTION_WIRELESS_SETTINGS));
         * }
         * 
         * }
         * }).setNegativeButton("知道了", null).show();
         */
        showToast(R.string.no_net);

    }

    /**
     * 退出系统
     * 
     * @param context
     */
    public static void showExitSystem(Context context) {
        AlertDialog.Builder builder = new Builder(context);
        builder.setIcon(R.drawable.ic_launcher)
        //
                .setTitle(R.string.app_name)
                //
                .setMessage("是否退出应用").setPositiveButton("确定", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        // 多个Activity——懒人听书：没有彻底退出应用
                        // 将所有用到的Activity都存起来，获取全部，干掉
                        // BaseActivity——onCreated——放到容器中
                    }
                })//
                .setNegativeButton("取消", null)//
                .show();

    }

    /**
     * 显示错误提示框
     * 
     * @param context
     * @param msg
     */
    public static void showErrorDialog(Context context, String msg) {
        new AlertDialog.Builder(context)//
                .setIcon(R.drawable.ic_launcher)//
                .setTitle(R.string.app_name)//
                .setMessage(msg)//
                .setNegativeButton("确定", null)//
                .show();
    }

    public static void showToast(String msg) {

        Toast.makeText(PortfolioApplication.getInstance(), msg, Toast.LENGTH_LONG).show();
    }

    public static void showToast(int msgResId) {

        Toast.makeText(PortfolioApplication.getInstance(), msgResId, Toast.LENGTH_LONG).show();
    }

    // 当测试阶段时true
    private static final boolean isShow = true;

    /**
     * 测试用 在正式投入市场：删
     * 
     * @param context
     * @param msg
     */
    public static void showToastTest(String msg) {
        if (isShow) {
            showToast(msg);
        }
    }

}
