package com.dkhs.portfolio.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.dkhs.portfolio.R;

public class ModelAcitivity extends BaseActivity {

    /** 显示子页面的容器 */
    private RelativeLayout layoutContent;

    /** 返回按钮 */
    private Button btnBack;

    // private LinearLayout llBack;

    @Override
    protected void onCreate(Bundle arg0) {
        onCreate(arg0, R.layout.layout_model_default);
    }

    protected void onCreate(Bundle arg0, int titleLayout) {
        super.onCreate(arg0);
        setTheme(android.R.style.Theme_Light_NoTitleBar);
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.setContentView(R.layout.layout_model);
        // ViewStub view = (ViewStub) findViewById(R.id.layout_model_right);
        // view.setLayoutResource(titleLayout);
        // view.inflate();
        initView();
    }

    @Override
    public void setContentView(int layoutResID) {
        // TODO Auto-generated method stub
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

    /**
     * 
     * @Title: findView
     * @Description: 监听以及发现
     * @param
     * @return void
     * @throws
     */
    private void initView() {
        // 取得页面容器 用于子页面的视图添加
        layoutContent = (RelativeLayout) findViewById(R.id.layoutContent);

        btnBack = (Button) findViewById(R.id.btn_back);

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
                case R.id.btn_back:

                    onBackPressed();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 
     * @Title: setBackButtonListener
     * @Description: 重载左边返回键的单击监听
     * @param @param listener
     * @return void
     * @throws
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
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param title
     * @return
     */
    @Override
    public void setTitle(CharSequence title) {
        // TODO Auto-generated method stub
        // super.setTitle(title);
        ((TextView) findViewById(R.id.tv_title)).setText(title);
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param titleId
     * @return
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
     * @param title
     * 标题名称
     */
    protected void setBackTitle(String title) {
        ((TextView) findViewById(R.id.btn_back)).setText(title);
    }

    public void setBackTitle(int stringId) {
        ((TextView) findViewById(R.id.btn_back)).setText(stringId);
    }

    /**
     * 
     * @Title: hideHead
     * @Description: 隐藏标题栏
     * @param
     * @return void
     * @throws
     */
    public void hideHead() {
        RelativeLayout rlHead = (RelativeLayout) findViewById(R.id.includeHead);
        if (rlHead.getVisibility() == View.VISIBLE) {
            rlHead.setVisibility(View.GONE);
        }
    }

    public void showHead() {
        RelativeLayout rlHead = (RelativeLayout) findViewById(R.id.includeHead);
        if (rlHead.getVisibility() == View.GONE) {
            rlHead.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 
     * @Title: setNoContent
     * @Description: 显示一个没有任何消息的提示页面
     * @param @param drawable
     * @return void
     * @throws
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
