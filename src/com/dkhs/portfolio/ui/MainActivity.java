package com.dkhs.portfolio.ui;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.AppBean;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.fragment.MainFragment;
import com.dkhs.portfolio.ui.widget.ITitleButtonListener;
import com.dkhs.portfolio.ui.widget.kline.DisplayUtil;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends FragmentActivity implements ITitleButtonListener,OnClickListener {

    private DrawerLayout mDrawerLayout;
    // private View mRightMenu;
    private View mLeftMenu;

    private MainFragment mainFragment;
    private Context context;
    private ImageView mainIntroImage;
    private boolean intro = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayUtil.initDisplayUtil(this);
        if(UIUtils.hasSmartBar()){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // mRightMenu = findViewById(R.id.menu_layout_right);
        mLeftMenu = findViewById(R.id.menu_layout_left);
        int k = PortfolioPreferenceManager.getIntValue(PortfolioPreferenceManager.KEY_APP_INTRODUS);
        if(k != 555){
            mainIntroImage = (ImageView) findViewById(R.id.main_intro_image);
            mainIntroImage.setOnClickListener(this);
            mainIntroImage.setVisibility(View.VISIBLE);
        }
        context = this;
        // 如果保存的状态不为空则得到之前保存的Fragment，否则实例化MainFragment
        if (savedInstanceState != null) {
            mainFragment = (MainFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        }
        if (mainFragment == null) {
            mainFragment = new MainFragment();
        }
        mainFragment.setTitleClickListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, mainFragment).commit();
        // 删除之前的activity,防止返回键退回到登录界面
        PortfolioApplication.getInstance().clearActivities();
        PortfolioApplication.getInstance().addActivity(this);
        UserEngineImpl mUserEngineImpl = new UserEngineImpl();
		mUserEngineImpl.getAppVersion("portfolio_android", userInfoListener);
		userInfoListener.setLoadingDialog(this);
    }
    ParseHttpListener userInfoListener = new ParseHttpListener<AppBean>() {

        @Override
        protected AppBean parseDateTask(String jsonData) {

            return DataParse.parseObjectJson(AppBean.class, jsonData);
        }

        @Override
        protected void afterParseData(AppBean object) {
            try {
				if (null != object) {
					final AppBean bean = object;
					PackageManager manager = context.getPackageManager();
					PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
					String version = info.versionName;
					String s = bean.getVersion().replaceAll("\\.", "");
					int service = Integer.parseInt(s);
					int local = Integer.parseInt(version.replaceAll("\\.", ""));
					if(service > local){
						AlertDialog.Builder alert = new AlertDialog.Builder(context);
						alert.setTitle("软件升级")
								.setMessage("发现新版本,建议立即更新使用.")//"发现新版本,建议立即更新使用."
								.setCancelable(false)
								.setPositiveButton("更新",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												 DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
								            	  
								            	 Uri uri = Uri.parse(bean.getUrl());
								            	 Request request = new Request(uri);
								            	 //设置允许使用的网络类型，这里是移动网络和wifi都可以 
								            	 request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI); 
								            	 //禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION 
								            	 request.setShowRunningNotification(true); 
								            	 //不显示下载界面 
								            	 request.setVisibleInDownloadsUi(true);
								            	       
								            	request.setDestinationInExternalFilesDir(context, null, "dkhs.apk");
								            	long id = downloadManager.enqueue(request);
								            	PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_APP_ID,id +"");
								            	PromptManager.showToast("开始下载");
											}
										})
								.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												PortfolioApplication.getInstance().exitApp();
											}
										});
						if(object.isUpgrade()){
							alert.show();
						}
					}
					
				}
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }
    };
    /**
     * 保存Fragment的状态
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mainFragment);
    }

    // @Override
    // public void rightButtonClick() {
    // if (null != mDrawerLayout && null != mRightMenu) {
    // mDrawerLayout.openDrawer(mRightMenu);
    // }
    //
    // }

    @Override
    public void leftButtonClick() {
        if (null != mDrawerLayout && null != mLeftMenu) {
            mDrawerLayout.openDrawer(mLeftMenu);
        }

    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (mDrawerLayout.isDrawerOpen(mLeftMenu)) {
            mDrawerLayout.closeDrawer(mLeftMenu);
        }
        // if (mDrawerLayout.isDrawerOpen(mRightMenu)) {
        // mDrawerLayout.closeDrawer(mRightMenu);
        // }
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        LogUtils.i("================onDestroy()====================");
    }
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
		MobclickAgent.onResume(this);
	}

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.main_intro_image:
                if(intro){
                    mainIntroImage.setVisibility(View.GONE);
                    PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_APP_INTRODUS, 555);
                }
                intro = true;
                mainIntroImage.setImageResource(R.drawable.main_intro_show);
                break;

            default:
                break;
        }
    }
}
