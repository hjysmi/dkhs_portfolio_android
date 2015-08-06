package com.dkhs.portfolio.ui.widget;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.text.TextUtils;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.AppBean;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.PromptManager;

/**
 * @author zwm
 * @version 2.0
 * @ClassName UpdateDialog
 * @Description TODO(更新对话框)
 * @date 2015/6/30.
 */
public class UpdateDialog  extends  MAlertDialog{


    private Context mContext;

    public UpdateDialog(Context context) {
        super(context);
        mContext=context;
    }
    public void showByAppBean(final AppBean appBean){

        this.setTitle(context.getString(R.string.update)).setMessage(appBean.getDesc())// 
                .setCancelable(false).setPositiveButton(context.getString(R.string.update_rightnow), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(appBean.getUrl());
                //
                DownloadManager.Request request = new DownloadManager.Request(uri);
                // 设置允许使用的网络类型，这里是移动网络和wifi都可以
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                        | DownloadManager.Request.NETWORK_WIFI);
                // 禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
                request.setShowRunningNotification(true);
                // 不显示下载界面
                request.setVisibleInDownloadsUi(true);
                request.setDestinationInExternalFilesDir(context, null, "dkhs.apk");
                long id = downloadManager.enqueue(request);
                PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_APP_ID, id
                        + "");
                PromptManager.showToast(context.getString(R.string.start_download));

                dialog.dismiss();

                if(appBean.isUpgrade()){
                    PortfolioApplication.getInstance().exitApp();
                }


            }
        });

        
        if(TextUtils.isEmpty(appBean.getDesc())){
           this.setMessage(context.getString(R.string.def_update_dec));
        }
        
        if(appBean.isUpgrade()){
            this.setNegativeButton(context.getString(R.string.exit_app), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    PortfolioApplication.getInstance().exitApp();
                    dialog.dismiss();
                }
            });
        }else{
            this.setNegativeButton(context.getString(R.string.no_update), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_VERSIONY, appBean.getVersion());
                    dialog.dismiss();
                }
            });
        }


        this.show();
    }
}
