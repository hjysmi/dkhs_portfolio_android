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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import cn.sharesdk.onekeyshare.OnekeyShare;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.ui.fragment.FragmentCompare;
import com.dkhs.portfolio.ui.fragment.FragmentNetValueTrend;
import com.dkhs.portfolio.ui.fragment.FragmentNews;
import com.dkhs.portfolio.ui.fragment.FragmentPositionDetail;
import com.dkhs.portfolio.ui.fragment.TestFragment;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;

/**
 * @ClassName CombinationDetailActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-1 下午1:10:29
 * @version 1.0
 */
public class CombinationDetailActivity extends ModelAcitivity implements OnClickListener {

    private static final float DOWN_SCALE = 1.0f;
    private Button btnMore;
    private View btnPreBottom;
    private Fragment mPreFragment;
    private TextView tvTitleView;

    private CombinationBean mCombinationBean;

    MyPagerFragmentAdapter mPagerAdapter;

    // private FragmentNetValueTrend mFragmentTrend;
    // private FragmentCompare mFragmentCompare;
    // private FragmentPositionDetail mFragmentDetail;
    // private FragmentNews mFragmentNews;

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
        setTitle(R.string.netvalue_trend);

        // handle intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

        initView();
        // showFragmentByButtonId(R.id.btn_trend);
    }

    private void handleExtras(Bundle extras) {
        mCombinationBean = (CombinationBean) extras.getSerializable(EXTRA_COMBINATION);

    }

    private void showShareButton() {
        btnMore.setBackgroundResource(R.drawable.ic_share);
        btnMore.setVisibility(View.VISIBLE);

    }

    private void hideMoreButton() {
        btnMore.setVisibility(View.GONE);
    }

    private void initView() {
        btnMore = getRightButton();
        btnMore.setOnClickListener(this);
        btnMore.setBackgroundResource(R.drawable.ic_share);
        // btnMore.setBackgroundResource(R.drawable.nav_more_selector);

        btnPreBottom = findViewById(R.id.btn_trend);
        btnPreBottom.setEnabled(false);
        btnPreBottom.setOnClickListener(bottomClickListner);
        findViewById(R.id.btn_comparison).setOnClickListener(bottomClickListner);
        findViewById(R.id.btn_detail).setOnClickListener(bottomClickListner);
        findViewById(R.id.btn_news).setOnClickListener(bottomClickListner);

        // Button btnShare = getSecondRightButton();
        // btnShare.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // saveBitmapToGallery();
        // Toast.makeText(CombinationDetailActivity.this, "保存截图到相册", Toast.LENGTH_SHORT).show();
        // }
        // });

        // initTabPage();
        replaceTrendView();

    }

    private void replaceTrendView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.rl_content, FragmentNetValueTrend.newInstance(false));
        ft.commit();
    }

    private void replaceCompareView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.rl_content, new FragmentCompare());
        ft.commit();
    }

    private void replaceDetailView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.rl_content, FragmentPositionDetail.newInstance(mCombinationBean.getId()));
        ft.commit();
    }

    private void replaceNewsView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment f = new FragmentNews();
        // Fragment f = TestFragment.getInstance();
        Bundle b = new Bundle();
        b.putSerializable(FragmentNews.DATA, mCombinationBean);
        f.setArguments(b);
        ft.replace(R.id.rl_content, f);
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
                // 直接分享
                showShare(true, null, false);

                break;

            default:
                break;
        }

    }

    private void showShare(boolean silent, String platform, boolean captureView) {
        Context context = this;
        final OnekeyShare oks = new OnekeyShare();

        oks.setNotification(R.drawable.ic_launcher, context.getString(R.string.app_name));
        // oks.setAddress("12345678901");
        // oks.setTitle(CustomShareFieldsPage.getString("title", context.getString(R.string.evenote_title)));
        // oks.setTitleUrl(CustomShareFieldsPage.getString("titleUrl", "http://mob.com"));
        // String customText = CustomShareFieldsPage.getString( "text", null);
        oks.setTitle("谁牛");
        oks.setTitleUrl("http://dev.dkhs.com");
        String customText = "分享内容定制";
        oks.setText(customText);
        // if (customText != null) {
        // oks.setText(customText);
        // } {
        // oks.setText(context.getString(R.string.share_content));
        // }

        // if (captureView) {
        // // oks.setViewToShare(getPage());
        // } else {
        // oks.setImagePath(CustomShareFieldsPage.getString("imagePath", MainActivity.TEST_IMAGE));
        // oks.setImageUrl(CustomShareFieldsPage.getString("imageUrl", MainActivity.TEST_IMAGE_URL));
        // oks.setImageArray(new String[] { MainActivity.TEST_IMAGE, MainActivity.TEST_IMAGE_URL });
        // }
        // oks.setUrl("http://dev.dkhs.com");
        // oks.setFilePath(CustomShareFieldsPage.getString("filePath", MainActivity.TEST_IMAGE));
        // oks.setComment("share");
        // oks.setSite(CustomShareFieldsPage.getString("site", context.getString(R.string.app_name)));
        // oks.setSiteUrl(CustomShareFieldsPage.getString("siteUrl", "http://mob.com"));
        // oks.setVenueName(CustomShareFieldsPage.getString("venueName", "ShareSDK"));
        // oks.setVenueDescription(CustomShareFieldsPage.getString("venueDescription", "This is a beautiful place!"));
        // oks.setLatitude(23.056081f);
        // oks.setLongitude(113.385708f);
        oks.setSilent(silent);
        oks.setShareFromQQAuthSupport(true);
        if (platform != null) {
            oks.setPlatform(platform);
        }

        // 令编辑页面显示为Dialog模式
        oks.setDialogMode();

        // 在自动授权时可以禁用SSO方式
        // if (!CustomShareFieldsPage.getBoolean("enableSSO", true))
        // oks.disableSSOWhenAuthorize();

        // 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
        // oks.setCallback(new OneKeyShareCallback());

        // 去自定义不同平台的字段内容
        // oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());

        // 去除注释，演示在九宫格设置自定义的图标
        // Bitmap logo = BitmapFactory.decodeResource(menu.getResources(), R.drawable.ic_launcher);
        // String label = menu.getResources().getString(R.string.app_name);
        // OnClickListener listener = new OnClickListener() {
        // public void onClick(View v) {
        // String text = "Customer Logo -- ShareSDK " + ShareSDK.getSDKVersionName();
        // Toast.makeText(menu.getContext(), text, Toast.LENGTH_SHORT).show();
        // oks.finish();
        // }
        // };
        // oks.setCustomerLogo(logo, label, listener);

        // 去除注释，则快捷分享九宫格中将隐藏新浪微博和腾讯微博
        // oks.addHiddenPlatform(SinaWeibo.NAME);
        // oks.addHiddenPlatform(TencentWeibo.NAME);

        // 为EditPage设置一个背景的View
        // oks.setEditPageBackground(getPage());

        // 设置kakaoTalk分享链接时，点击分享信息时，如果应用不存在，跳转到应用的下载地址
        // oks.setInstallUrl("http://www.mob.com");
        // 设置kakaoTalk分享链接时，点击分享信息时，如果应用存在，打开相应的app
        // oks.setExecuteUrl("kakaoTalkTest://starActivity");

        oks.show(context);
    }

    protected void showFragmentByButtonId(int id) {
        int mSelectedTabIndex = 0;
        switch (id) {
            case R.id.btn_trend: {

                mSelectedTabIndex = 0;
                setTitle(R.string.netvalue_trend);
                replaceTrendView();
                // if (null == mFragmentTrend) {
                // mFragmentTrend = new FragmentNetValueTrend();
                // }
                // mFragment = mFragmentTrend;
            }

                break;
            case R.id.btn_comparison: {

                mSelectedTabIndex = 1;

                setTitle(R.string.performance_comparison);
                replaceCompareView();
            }

                break;
            case R.id.btn_detail: {

                mSelectedTabIndex = 2;

                setTitle(R.string.position_detail);
                replaceDetailView();
            }

                break;
            case R.id.btn_news: {

                mSelectedTabIndex = 3;
                setTitle(R.string.related_news);
                replaceNewsView();

            }

                break;

            default:
                break;
        }
        
        if (mSelectedTabIndex == 0) {
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
}
