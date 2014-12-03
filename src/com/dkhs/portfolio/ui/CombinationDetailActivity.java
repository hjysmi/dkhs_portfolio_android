/**
 * @Title CombinationDetailActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-1 下午1:10:29
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.fragment.FragmentCompare;
import com.dkhs.portfolio.ui.fragment.FragmentNetValueTrend;
import com.dkhs.portfolio.ui.fragment.FragmentNews;
import com.dkhs.portfolio.ui.fragment.FragmentPositionDetail;
import com.dkhs.portfolio.ui.fragment.TestFragment;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.dkhs.portfolio.utils.TimeUtils;

/**
 * @ClassName CombinationDetailActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-1 下午1:10:29
 * @version 1.0
 */
public class CombinationDetailActivity extends ModelAcitivity implements OnClickListener {

    private static final float DOWN_SCALE = 1.0f;
    private Button btnShare;
    private Button btnMore;
    private View btnPreBottom;
    private Fragment mPreFragment;
    private TextView tvTitleView;

    private CombinationBean mCombinationBean;

    MyPagerFragmentAdapter mPagerAdapter;

    private FragmentNetValueTrend mFragmentTrend;
    private FragmentCompare mFragmentCompare;
    private FragmentPositionDetail mFragmentDetail;
    private FragmentNews mFragmentNews;
    public static final String EXTRA_COMBINATION = "extra_combination";

    public static Intent newIntent(Context context, CombinationBean combinationBean) {
        Intent intent = new Intent(context, CombinationDetailActivity.class);

        intent.putExtra(EXTRA_COMBINATION, combinationBean);

        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_combination_detail);

        // handle intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

        updataTitle();
        initView();
        // showFragmentByButtonId(R.id.btn_trend);
    }

    private void updataTitle() {
        if (null != mCombinationBean) {
            setTitle(mCombinationBean.getName());
            setTitleTipString("创建于" + TimeUtils.getSimpleDay(mCombinationBean.getCreateTime()));
        }
    }

    private void handleExtras(Bundle extras) {
        mCombinationBean = (CombinationBean) extras.getSerializable(EXTRA_COMBINATION);

    }

    private void showShareButton() {
        btnShare.setBackgroundResource(R.drawable.ic_share);
        btnShare.setVisibility(View.VISIBLE);
        btnMore.setVisibility(View.VISIBLE);

    }

    private void hideMoreButton() {
        btnShare.setVisibility(View.GONE);
        btnMore.setVisibility(View.GONE);
    }

    private void initView() {

        btnShare = getSecondRightButton();
        btnShare.setOnClickListener(this);
        btnShare.setBackgroundResource(R.drawable.ic_share);
        btnMore = getRightButton();
        btnMore.setOnClickListener(this);
        btnMore.setBackgroundResource(R.drawable.nav_more);
        btnPreBottom = findViewById(R.id.btn_trend);
        btnPreBottom.setEnabled(false);
        btnPreBottom.setOnClickListener(bottomClickListner);
        head = findViewById(R.id.includeHead);
        findViewById(R.id.btn_comparison).setOnClickListener(bottomClickListner);
        findViewById(R.id.btn_detail).setOnClickListener(bottomClickListner);
        findViewById(R.id.btn_news).setOnClickListener(bottomClickListner);
        btn_more_categorys = getResources().getStringArray(R.array.fund_detail_more);

        // initTabPage();
        replaceTrendView();

    }

    private void replaceTrendView() {
        // if (null == mFragmentTrend) {
        mFragmentTrend = FragmentNetValueTrend.newInstance(false, null);
        // }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // ft.replace(R.id.rl_content, FragmentNetValueTrend.newInstance(false));
        ft.replace(R.id.fl_content, mFragmentTrend);
        ft.commit();
    }

    private void replaceCompareView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mFragmentCompare = new FragmentCompare();
        ft.replace(R.id.fl_content, mFragmentCompare);
        ft.commit();
    }

    private void replaceDetailView() {
        mFragmentDetail = FragmentPositionDetail.newInstance(mCombinationBean.getId());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, mFragmentDetail);
        ft.commit();
    }

    private void replaceNewsView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment f = new FragmentNews();
        // Fragment f = TestFragment.getInstance();
        Bundle b = new Bundle();
        b.putSerializable(FragmentNews.DATA, mCombinationBean);
        f.setArguments(b);
        ft.replace(R.id.fl_content, f);
        ft.commit();
    }

    ScrollViewPager mViewPager;

    private void initTabPage() {

        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();// ViewPager中显示的数据
        // fragmentList.add(FragmentNetValueTrend.newInstance(false));
        fragmentList.add(TestFragment.getInstance());
        fragmentList.add(new FragmentCompare());
        // fragmentList.add(TestFragment.getInstance());
        fragmentList.add(FragmentPositionDetail.newInstance(mCombinationBean.getId()));
        fragmentList.add(TestFragment.getInstance());
        // Fragment f = new FragmentNews();
        // fragmentList.add(FragmentPositionDetail.newInstance(mCombinationBean.getId()));
        // Fragment f = new FragmentNews();
        // Bundle b = new Bundle();
        // b.putSerializable(FragmentNews.DATA, mCombinationBean);
        // f.setArguments(b);
        // fragmentList.add(f);

        mViewPager = (ScrollViewPager) findViewById(R.id.pager);
        mViewPager.setCanScroll(false);
        mPagerAdapter = new MyPagerFragmentAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOnPageChangeListener(pageChangeListener);
    }

    private class MyPagerFragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;

        // private String[] titleList;

        public MyPagerFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList2) {
            super(fm);
            this.fragmentList = fragmentList2;
            // this.titleList = titleList;
        }

        @Override
        public Fragment getItem(int arg0) {

            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

    }

    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        int currentPosition = 0;

        @Override
        public void onPageSelected(int newPosition) {

            // FragmentLifecycle fragmentToShow = (FragmentLifecycle) mPagerAdapter.getItem(newPosition);
            // fragmentToShow.onResumeFragment();
            //
            // FragmentLifecycle fragmentToHide = (FragmentLifecycle) mPagerAdapter.getItem(currentPosition);
            // fragmentToHide.onPauseFragment();
            if (newPosition == 0) {
                showShareButton();
            } else {
                hideMoreButton();
            }

            currentPosition = newPosition;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageScrollStateChanged(int arg0) {
        }
    };

    // private void replaceContentView(Fragment fragment, String tag) {
    //
    // getSupportFragmentManager().beginTransaction().replace(R.id.combination_contentview, fragment, tag);
    // }

    OnClickListener bottomClickListner = new OnClickListener() {

        @Override
        public void onClick(View v) {

            btnPreBottom.setEnabled(true);
            v.setEnabled(false);
            btnPreBottom = v;
            showFragmentByButtonId(v.getId());

        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_right:
                // 下拉列表
                showMoreDialog(btn_more_categorys);
                break;
            case R.id.btn_right_second:
                if (0 == mSelectedTabIndex && mFragmentTrend != null) {
                    // 直接分享
                    // mFragmentTrend.showShare(false, null, false);
                    mFragmentTrend.showShareImage();
                } else if (1 == mSelectedTabIndex && mFragmentCompare != null) {
                    mFragmentCompare.showShareImage();
                } else if (2 == mSelectedTabIndex && mFragmentDetail != null) {
                    mFragmentDetail.showShareImage();
                }

                break;
            default:
                break;
        }

    }

    private PopupWindow pw;
    private String[] btn_more_categorys;
    private View head;

    protected void showMoreDialog(String[] category) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.layout_btn_more, null);
        ListView lv_profit_loss = (ListView) view.findViewById(R.id.lv_more);
        lv_profit_loss.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	switch (position) {
				case 0:
					// 修改基金名称
					startActivityForResult(
                            ChangeCombinationNameActivity.newIntent(CombinationDetailActivity.this, mCombinationBean),
                            REQUESTCODE_MODIFY_COMBINATION);
					break;
				case 1:
					// 调整仓位
					Intent intent = new Intent(CombinationDetailActivity.this, PositionAdjustActivity.class);
                    intent.putExtra(PositionAdjustActivity.EXTRA_COMBINATION_ID, mCombinationBean.getId());
                    intent.putExtra(PositionAdjustActivity.EXTRA_ISADJUSTCOMBINATION, true);
                    startActivity(intent);
					break;
				case 2:
					// 隐私设置
					startActivity(PrivacySettingActivity.newIntent(CombinationDetailActivity.this, mCombinationBean));
					break;
				case 3:
					//每日收益记录
					startActivity(EveryDayValueActivity.newIntent(CombinationDetailActivity.this, mCombinationBean));
					break;
				case 4:
					//历史调仓记录
					startActivity(HistoryPositionDetailActivity.newIntent(CombinationDetailActivity.this, mCombinationBean));
					break;
				default:
					break;
				}
                pw.dismiss();
            }
        });
        lv_profit_loss.setAdapter(new ArrayAdapter<String>(this, R.layout.item_btn_more, category));
        pw = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(true);
        pw.setFocusable(true);
        pw.getContentView().measure(0, 0);
        int width = pw.getContentView().getMeasuredWidth();
        // pw.setAnimationStyle(R.style.profit_loss_style);
        // 保存anchor在屏幕中的位置
        int[] location = new int[2];
        // 读取位置anchor座标
        head.getLocationOnScreen(location);
        int desX = head.getWidth() - width - getResources().getDimensionPixelSize(R.dimen.line_weight_stroke_width);
        pw.showAtLocation(head, Gravity.NO_GRAVITY, location[0] + desX, location[1] + head.getHeight());
    }

    private int mSelectedTabIndex = 0;

    protected void showFragmentByButtonId(int id) {
        // int mSelectedTabIndex = 0;
        switch (id) {
            case R.id.btn_trend: {

                mSelectedTabIndex = 0;
                // setTitle(R.string.netvalue_trend);
                replaceTrendView();
                // if (null == mFragmentTrend) {
                // mFragmentTrend = new FragmentNetValueTrend();
                // }
                // mFragment = mFragmentTrend;
            }

                break;
            case R.id.btn_comparison: {

                mSelectedTabIndex = 1;

                // setTitle(R.string.performance_comparison);
                replaceCompareView();
            }

                break;
            case R.id.btn_detail: {

                mSelectedTabIndex = 2;

                // setTitle(R.string.position_detail);
                replaceDetailView();
            }

                break;
            case R.id.btn_news: {

                mSelectedTabIndex = 3;
                // setTitle(R.string.related_news);
                replaceNewsView();

            }

                break;

            default:
                break;
        }

        if (mSelectedTabIndex == 0 || mSelectedTabIndex == 1 || mSelectedTabIndex == 2) {
            showShareButton();
        } else {
            hideMoreButton();
        }
        // PagerAdapter adapter = mViewPager.getAdapter();
        // final int count = adapter.getCount();
        //
        // if (mSelectedTabIndex > count) {
        // mSelectedTabIndex = count - 1;
        // }
        //
        // mViewPager.setCurrentItem(mSelectedTabIndex, true);

    }

    // 屏幕截取
    private Bitmap screenShot() {
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        final int statusBarHeight = frame.top;
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        final View view = getWindow().getDecorView();
        int width = (int) (point.x * DOWN_SCALE);
        int height = (int) (point.y * DOWN_SCALE);
        int offset = width % 4;
        width -= offset;
        height = height - (height % 4);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(0f, -statusBarHeight * DOWN_SCALE);
        canvas.scale(DOWN_SCALE, DOWN_SCALE);
        view.draw(canvas);
        // Timber.d("###offset:%d", offset);
        return bitmap;
    }

    private void saveBitmapToGallery() {
        String timeStamp = System.currentTimeMillis() + "";
        Bitmap combination = screenShot();
        // save in gallery
        MediaStore.Images.Media.insertImage(getContentResolver(), combination, "test_" + timeStamp + ".jpg",
                timeStamp.toString());
    }

    private final int REQUESTCODE_MODIFY_COMBINATION = 902;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            Bundle b = data.getExtras(); // data为B中回传的Intent
            switch (requestCode) {
                case REQUESTCODE_MODIFY_COMBINATION:
                    // ArrayList<SelectStockBean> listStock = (ArrayList<SelectStockBean>) data
                    // .getSerializableExtra(BaseSelectActivity.ARGUMENT_SELECT_LIST);
                    CombinationBean cBean = (CombinationBean) data
                            .getSerializableExtra(ChangeCombinationNameActivity.ARGUMENT_COMBINATION_BEAN);
                    if (null != cBean) {
                        mCombinationBean = cBean;
                        // update combination
                        updataTitle();

                    }
                    break;
            }

        }
    }

}
