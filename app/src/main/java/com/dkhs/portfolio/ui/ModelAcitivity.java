package com.dkhs.portfolio.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * @author zjz
 * @version 1.0
 * @ClassName ModelAcitivity
 * @Description 模板Activity类，实现以下功能
 * 1.带统一风格的标题栏
 * 2.实现点击空白区别，隐藏软键盘
 * 3.在左边100像素范围侧滑后台
 * 4.进入和退出页面的动画风格
 * 5.对Activity的出栈入栈管理
 * 6.添加了友盟/百度统计
 * @date 2015-4-23 下午2:59:54
 */
public class ModelAcitivity extends SwipeBackActivity {

    public final int RIGHTBUTTON_ID = R.id.btn_right;
    public final int BACKBUTTON_ID = R.id.btn_back;
    public final int SECONDRIGHTBUTTON_ID = R.id.btn_right_second;
    public boolean hadFragment;
    private TextView btnBack;
    //    protected UserEngineImpl engine;
//    protected Activity mActivity;
    private View mTitleView;
    private View mProgressView;
    /**
     * 显示子页面的容器
     */
    private RelativeLayout layoutContent;
    private OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case BACKBUTTON_ID:

                    onBackPressed();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 返回按钮
     */

    public void hadFragment() {
        hadFragment = true;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        // 模拟堆栈管理activity
        //方便定位类
        LogUtils.d("start Activity ", this.getClass().getSimpleName());
        PortfolioApplication.getInstance().addActivity(this);
//        mActivity = this;
        onCreate(arg0, R.layout.layout_model_default);
//        setStatusBarColor();

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.theme_color));
        }
    }

    protected void onResume() {
        super.onResume();
//        if (!hadFragment) {
//            MobclickAgent.onPageStart(this.getClass().getSimpleName());
//        }
        //统计时长
        MobclickAgent.onResume(this);
        if (getPageStatisticsStringId() != 0) {
            StatService.onPageStart(mContext, UIUtils.getResString(mContext, getPageStatisticsStringId()));
            MobclickAgent.onPageStart(UIUtils.getResString(mContext, getPageStatisticsStringId()));
        }
    }

    protected void onPause() {
        super.onPause();

//        if (!hadFragment) {
//            MobclickAgent.onPageEnd(this.getClass().getSimpleName());
//        }
        if (getPageStatisticsStringId() != 0) {
            StatService.onPageEnd(mContext, UIUtils.getResString(mContext, getPageStatisticsStringId()));
            MobclickAgent.onPageEnd(UIUtils.getResString(mContext, getPageStatisticsStringId()));
        }

        MobclickAgent.onPause(this);
    }
    public int getPageStatisticsStringId(){
        return 0;
    }

    protected void onCreate(Bundle arg0, int titleLayout) {
        super.onCreate(arg0);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleBundleExtras(extras);
        }

        super.setContentView(R.layout.layout_model);

        SwipeBackLayout mSwipeBackLayout = getSwipeBackLayout();
//         设置可以滑动的区域，推荐用屏幕像素的一半来指定
        mSwipeBackLayout.setEdgeSize(100);
//         设定滑动关闭的方向，SwipeBackLayout.EDGE_ALL表示向下、左、右滑动均可。EDGE_LEFT，EDGE_RIGHT，EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        stepTitleView();
    }

    public void handleBundleExtras(Bundle extras) {
    }

    @Override
    public void setContentView(int layoutResID) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        layoutContent.addView(View.inflate(this, layoutResID, null), params);
        layoutContent.addView(mProgressView, params);
        mProgressView.setVisibility(View.GONE);
    }

    public void setContentView(View view, LayoutParams params) {
        if (view == null)
            return;
        layoutContent.addView(view, params);
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public void replaceContentFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layoutContent, fragment).commitAllowingStateLoss();
    }

    /**
     * @param
     * @return void
     * @throws
     * @Title: findView
     * @Description: 监听以及发现
     */
    private void stepTitleView() {
        // 取得页面容器 用于子页面的视图添加
        layoutContent = (RelativeLayout) findViewById(R.id.layoutContent);
        mProgressView = View.inflate(this, R.layout.progressbar, null);
        mTitleView = findViewById(R.id.view_title);

        btnBack = (TextView) findViewById(BACKBUTTON_ID);
        btnBack.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_back_selector),
                null, null, null);
        // llBack = (LinearLayout) findViewById(R.id.llHeadBack);

        // 监听返回键 使得子页面不必重复监听
        btnBack.setOnClickListener(clickListener);
        // llBack.setOnClickListener(clickListener);
    }

    /**
     * @param @param listener
     * @return void
     * @throws
     * @Title: setBackButtonListener
     * @Description: 重载左边返回键的单击监听
     */
    public void setBackButtonListener(OnClickListener listener) {
        this.clickListener = listener;
        btnBack.setOnClickListener(clickListener);
    }

    public void setTitleText(String title) {
        ((TextView) findViewById(R.id.tv_title)).setText(title);
    }

    public void setTitleText(int stringId) {
        ((TextView) findViewById(R.id.tv_title)).setText(stringId);
    }

    /**
     * @param title
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void setTitle(CharSequence title) {
        // super.setTitle(title);
        ((TextView) findViewById(R.id.tv_title)).setText(title);
    }

    /**
     * @param titleId
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void setTitle(int titleId) {
        // TODO Auto-generated method stub
        // super.setTitle(titleId);
        ((TextView) findViewById(R.id.tv_title)).setText(titleId);

    }

    public void hideBottomLine() {
        findViewById(R.id.bottom_line).setVisibility(View.GONE);
    }

    /**
     * 动态设置Activity的标题
     *
     * @param title 标题名称
     */
    protected void setBackTitle(String title) {
        ((TextView) findViewById(BACKBUTTON_ID)).setText(title);
    }

    public void setBackTitle(int stringId) {
        ((TextView) findViewById(BACKBUTTON_ID)).setText(stringId);
    }

    public void setTitleTipString(int stringId) {
        // ((TextView) findViewById(R.id.tv_title_info)).setText(stringId);
        setTitleTipString(getString(stringId));
    }

    public void setTitleTipString(String text) {
        TextView titleTip = ((TextView) findViewById(R.id.tv_title_info));
        titleTip.setText(text);
        titleTip.setVisibility(View.VISIBLE);
    }

    public TextView getRightButton() {
        TextView btnRight = (TextView) findViewById(RIGHTBUTTON_ID);
        btnRight.setVisibility(View.VISIBLE);
        // btnRight.setTextColor(Color.WHITE);
        return btnRight;
    }

    public TextView getSecondRightButton() {
        TextView btn = (TextView) findViewById(SECONDRIGHTBUTTON_ID);
        btn.setVisibility(View.VISIBLE);
        return btn;
    }

    /**
     * @param
     * @return void
     * @throws
     * @Title: hideHead
     * @Description: 隐藏标题栏
     */
    public void hideHead() {
        View rlHead = findViewById(R.id.view_title);
        if (rlHead.getVisibility() == View.VISIBLE) {
            rlHead.setVisibility(View.GONE);
        }
    }

    public void showHead() {
        View rlHead = findViewById(R.id.view_title);
        if (rlHead.getVisibility() == View.GONE) {
            rlHead.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param @param drawable
     * @return void
     * @throws
     * @Title: setNoContent
     * @Description: 显示一个没有任何消息的提示页面
     */
    public void setNoContent(int drawable) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(drawable);
        imageView.setLayoutParams(params);
        layoutContent.addView(imageView);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UIUtils.outAnimationActivity(this);

    }

    public void manualFinish(){
        finish();
        UIUtils.outAnimationActivity(this);
    }

    @Override
    protected void onDestroy() {
        PortfolioApplication.getInstance().getLists().remove(this);
        super.onDestroy();
    }

    public TextView getBtnBack() {
        return btnBack;
    }

    public void setBtnBack(TextView btnBack) {
        this.btnBack = btnBack;
    }


    public void setBackButtonDrawRes(int resId) {
        btnBack.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
    }

    public int getActionBarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public View getTitleView() {
        return mTitleView;
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        UIUtils.setOverridePendingAnin(this);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
       if("cn.sharesdk.onekeyshare.theme.classic.PlatformListPage".equals(intent.getStringExtra("executor_name"))){
           return;
       }
        if("0".equals(intent.getStringExtra("checktype"))){
            return;
        }
        UIUtils.setOverridePendingAnin(this);
    }

    public void startActivityNoAnim(Intent intent) {
        super.startActivity(intent);
    }

    public void updateTitleBackgroud(int resId) {
        getTitleView().setBackgroundResource(resId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }


    }

    public void updateTitleBackgroudByValue(float value) {
//        if (value < 0) {
//            updateTitleBackgroud(R.color.theme_blue);
//        } else if (value > 0) {
//            updateTitleBackgroud(R.color.tag_red);
//        } else {
//            updateTitleBackgroud(R.color.tag_gray);
//
//        }
    }
    public void showProgress(){
        mProgressView.setVisibility(View.VISIBLE);
    }

    public void dismissProgress(){
        if(mProgressView.getVisibility() == View.VISIBLE){
            mProgressView.setVisibility(View.GONE);
        }
    }

}
