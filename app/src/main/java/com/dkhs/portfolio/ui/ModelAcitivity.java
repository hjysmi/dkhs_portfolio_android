package com.dkhs.portfolio.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.ui.fragment.BaseFragment;
import com.dkhs.portfolio.ui.widget.TextImageButton;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;


public class ModelAcitivity extends SwipeBackActivity {

    public final int RIGHTBUTTON_ID = R.id.btn_right;
    public final int BACKBUTTON_ID = R.id.btn_back;
    public final int SECONDRIGHTBUTTON_ID = R.id.btn_right_second;
    private TextView btnBack;
    private View mTitleView;
    protected UserEngineImpl engine;
    protected Activity mActivity;

    public boolean hadFragment;

    /**
     * 显示子页面的容器
     */
    private RelativeLayout layoutContent;

    /**
     * 返回按钮
     */

    public void hadFragment(){
        hadFragment=true;
    }

    // private LinearLayout llBack;
    @Override
    protected void onCreate(Bundle arg0) {
        // 模拟堆栈管理activity
        PortfolioApplication.getInstance().addActivity(this);
        mActivity =this;
        onCreate(arg0, R.layout.layout_model_default);



    }

    protected void onResume() {
        super.onResume();
        if(!hadFragment){
            MobclickAgent.onPageStart(this.getClass().getSimpleName());
        }
        MobclickAgent.onResume(this);
         //统计时长
    }
    protected void onPause() {
        super.onPause();
        if(!hadFragment){
            MobclickAgent.onPageEnd(this.getClass().getSimpleName());
        }

        MobclickAgent.onPause(this);
    }

    protected void onCreate(Bundle arg0, int titleLayout) {
        super.onCreate(arg0);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleBundleExtras(extras);
        }
        engine = new UserEngineImpl();

        // setTheme(android.R.style.Theme_Light_NoTitleBar);
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.setContentView(R.layout.layout_model);

        SwipeBackLayout mSwipeBackLayout = getSwipeBackLayout();
//         设置可以滑动的区域，推荐用屏幕像素的一半来指定
        mSwipeBackLayout.setEdgeSize(100);
//         设定滑动关闭的方向，SwipeBackLayout.EDGE_ALL表示向下、左、右滑动均可。EDGE_LEFT，EDGE_RIGHT，EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        // saveTrackingMode(SwipeBackLayout.EDGE_ALL);
        // ViewStub view = (ViewStub) findViewById(R.id.layout_model_right);
        // view.setLayoutResource(titleLayout);
        // view.inflate();
        // setStatusBarColor(findViewById(R.id.statusBarBackground), getResources().getColor(R.color.red));
        stepTitleView();
    }

    public void handleBundleExtras(Bundle extras) {
    }

    @Override
    public void setContentView(int layoutResID) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        layoutContent.addView(View.inflate(this, layoutResID, null), params);
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
                .replace(R.id.layoutContent, fragment).commit();
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
        mTitleView = findViewById(R.id.tool);

        btnBack = (TextView) findViewById(BACKBUTTON_ID);
        btnBack.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_back_selector),
                null, null, null);
        // llBack = (LinearLayout) findViewById(R.id.llHeadBack);

        // 监听返回键 使得子页面不必重复监听
        btnBack.setOnClickListener(clickListener);
        // llBack.setOnClickListener(clickListener);
    }

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
        View rlHead = findViewById(R.id.tool);
        if (rlHead.getVisibility() == View.VISIBLE) {
            rlHead.setVisibility(View.GONE);
        }
    }

    public void showHead() {
        View rlHead = findViewById(R.id.tool);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public TextView getBtnBack() {
        return btnBack;
    }

    public void setBtnBack(TextView btnBack) {
        this.btnBack = btnBack;
    }

    public void setStatusBarColor(View statusBar, int color) {
        // if (Build.VERSION.SDK_INT >= 19) {
        // Window w = getWindow();
        // w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
        // WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // // status bar height
        // int actionBarHeight = getActionBarHeight();
        // int statusBarHeight = getStatusBarHeight();
        // // action bar height
        // statusBar.getLayoutParams().height = actionBarHeight + statusBarHeight;
        // statusBar.setBackgroundColor(color);
        // }
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

    public void startActivityNoAnim(Intent intent) {
        super.startActivity(intent);
    }

    public void updateTitleBackgroud(int resId) {
        getTitleView().setBackgroundResource(resId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(getResources().getColor(resId));
        }


    }

    public void updateTitleBackgroudByValue(float value) {
        if (value < 0) {
            updateTitleBackgroud(R.color.tag_green);
        } else if (value > 0) {
            updateTitleBackgroud(R.color.tag_red);
        } else {
            updateTitleBackgroud(R.color.tag_gray);

        }
    }


}
