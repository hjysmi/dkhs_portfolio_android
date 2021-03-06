/*
 * Offical Website:http://www.mob.com
 * Support QQ: 4006852216
 * Offical Wechat Account:ShareSDK   (We will inform you our updated news at the first time by Wechat, if we release a new version. If you get any problem, you can also contact us with Wechat, we will reply you within 24 hours.)
 *
 * Copyright (c) 2013 mob.com. All rights reserved.
 */

package cn.sharesdk.onekeyshare.theme.classic;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.mob.tools.utils.UIHandler;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.CustomPlatform;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.EditPageFakeActivity;
import cn.sharesdk.onekeyshare.PicViewer;
import cn.sharesdk.onekeyshare.ShareCore;

import static com.mob.tools.utils.BitmapHelper.blur;
import static com.mob.tools.utils.BitmapHelper.captureView;
import static com.mob.tools.utils.BitmapHelper.getBitmap;
import static com.mob.tools.utils.R.dipToPx;
import static com.mob.tools.utils.R.getBitmapRes;
import static com.mob.tools.utils.R.getScreenWidth;
import static com.mob.tools.utils.R.getStringRes;

/**
 * Photo-text Sharing will be handling in this page
 * <p/>
 * note:
 * wechat, yixin, qzone, etc. are shared in their clients, not in this page
 */
public class EditPage extends EditPageFakeActivity implements OnClickListener, TextWatcher {
    private static final int MAX_TEXT_COUNT = 140;
    private static final int DIM_COLOR = 0x7f323232;
    private HashMap<String, Object> reqData;
    private RelativeLayout rlPage;

    private LinearLayout llBody;
    private RelativeLayout rlThumb;
    // 文本编辑框
    private EditText etContent;
    // 字数计算器
    private TextView tvCounter;
    // 别针图片
    private ImageView ivPin;
    // 输入区域的图片
    private ImageView ivImage;
    private Bitmap image;
    private boolean shareImage;
    private LinearLayout llPlat;
    // private LinearLayout llAt;
    // 平台列表
    private Platform[] platformList;
    private View[] views;
    // 设置显示模式为Dialog模式
    private boolean dialogMode;
    private View tmpBgView;
    private Drawable background;
    private ArrayList<String> toFriendList;

    private View llTitle;

    public void setShareData(HashMap<String, Object> data) {
        reqData = data;
    }

    /**
     * 设置显示模式为Dialog模式
     */
    public void setDialogMode() {
        dialogMode = true;
    }

    public void setActivity(Activity activity) {
        super.setActivity(activity);
        Window win = activity.getWindow();
        int orientation = activity.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            win.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                    | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        } else {
            win.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
                    | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
    }

    public void setBackGround(View bgView) {
        tmpBgView = bgView;
    }

    public void onCreate() {
        if (reqData == null) {
            finish();
            return;
        }

        genBackground();
        activity.setContentView(getPageView());
        onTextChanged(etContent.getText(), 0, etContent.length(), 0);
        showThumb();

        // 获取平台列表并过滤微信等使用客户端分享的平台
        new Thread() {
            public void run() {
                platformList = ShareSDK.getPlatformList();
                if (platformList == null) {
                    return;
                }

                ArrayList<Platform> list = new ArrayList<Platform>();
                for (Platform plat : platformList) {
                    String name = plat.getName();
                    if ((plat instanceof CustomPlatform) || ShareCore.isUseClientToShare(name)) {
                        continue;
                    }
                    list.add(plat);
                }
                platformList = new Platform[list.size()];
                for (int i = 0; i < platformList.length; i++) {
                    platformList[i] = list.get(i);
                }

                UIHandler.sendEmptyMessage(1, new Callback() {
                    public boolean handleMessage(Message msg) {
                        afterPlatformListGot();
                        return false;
                    }
                });
            }
        }.start();
    }

    private RelativeLayout getPageView() {
        rlPage = new RelativeLayout(getContext());
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            rlPage.setBackgroundDrawable(background);
        } else {
            rlPage.setBackground(background);
        }


        if (dialogMode) {
            RelativeLayout rlDialog = new RelativeLayout(getContext());
            rlDialog.setBackgroundResource(R.drawable.bg_dialog);
            int dp_8 = dipToPx(getContext(), 8);
            int width = getScreenWidth(getContext()) - dp_8 * 2;
            RelativeLayout.LayoutParams lpDialog = new RelativeLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT);
            lpDialog.topMargin = dp_8;
            lpDialog.bottomMargin = dp_8;
            lpDialog.addRule(RelativeLayout.CENTER_IN_PARENT);
            rlDialog.setLayoutParams(lpDialog);
            rlPage.addView(rlDialog);

            rlDialog.addView(getPageTitle());
            rlDialog.addView(getPageBody());
            rlDialog.addView(getImagePin());
        } else {
            rlPage.addView(getPageTitle());
            rlPage.addView(getPageBody());
            rlPage.addView(getImagePin());
        }
        llTitle.setFocusableInTouchMode(true);
        llTitle.setFocusable(true);

        return rlPage;
    }

    // 标题栏
    private View getPageTitle() {


        llTitle = LayoutInflater.from(getContext()).inflate(R.layout.layout_share_title_bar, null);


        TextView titleTV = (TextView) llTitle.findViewById(R.id.tv_title);
        Button shareBtn = (Button) llTitle.findViewById(R.id.btn_share);

        // int resId = getBitmapRes(activity, "title_back");
        // if (resId > 0) {
        // llTitle.setBackgroundResource(resId);
        // }
        String title = (String) reqData.get("title");
        if (!TextUtils.isEmpty(title)) {
            titleTV.setText(title);
        } else {
            int resId = getStringRes(activity, "multi_share");
            if (resId > 0) {
                titleTV.setText(resId);
            }
        }
        shareBtn.setVisibility(View.VISIBLE);
        int resId = getStringRes(activity, "share");
        if (resId > 0) {
            shareBtn.setText(resId);
        }
        shareBtn.setOnClickListener(this);


        return llTitle;
    }

    // 页面主体
    private LinearLayout getPageBody() {
        llBody = new LinearLayout(getContext());
        llBody.setId(R.id.llBody);
        int resId = getBitmapRes(activity, "edittext_back");
        if (resId > 0) {
            llBody.setBackgroundResource(resId);
        }
        llBody.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams lpBody = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        lpBody.addRule(RelativeLayout.ALIGN_LEFT, llTitle.getId());
        lpBody.addRule(RelativeLayout.BELOW, llTitle.getId());
        lpBody.addRule(RelativeLayout.ALIGN_RIGHT, llTitle.getId());
        if (!dialogMode) {
            lpBody.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }
        int dp_3 = dipToPx(getContext(), 3);
        lpBody.setMargins(dp_3, dp_3, dp_3, dp_3);
        llBody.setLayoutParams(lpBody);

        llBody.addView(getMainBody());
        llBody.addView(getSep());
        llBody.addView(getPlatformList());

        return llBody;
    }

    private LinearLayout getMainBody() {
        LinearLayout llMainBody = new LinearLayout(getContext());
        llMainBody.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lpMain = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        lpMain.weight = 1;
        int dp_4 = dipToPx(getContext(), 4);
        lpMain.setMargins(dp_4, 0, dp_4, dp_4);
        llMainBody.setLayoutParams(lpMain);

        LinearLayout llContent = new LinearLayout(getContext());
        LinearLayout.LayoutParams lpContent = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        lpContent.weight = 1;
        llMainBody.addView(llContent, lpContent);

        // 文字输入区域
        etContent = new EditText(getContext());
        etContent.setGravity(Gravity.LEFT | Gravity.TOP);
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            etContent.setBackgroundDrawable(null);
        } else {
            etContent.setBackground(null);
        }

        etContent.setTextColor(getContext().getResources().getColor(R.color.tag_gray));
        etContent.setText(String.valueOf(reqData.get("text"))+reqData.get("url"));
        etContent.addTextChangedListener(this);
        LinearLayout.LayoutParams lpEt = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        lpEt.weight = 1;
        etContent.setLayoutParams(lpEt);
        llContent.addView(etContent);


        llContent.addView(getThumbView());
        llMainBody.addView(getBodyBottom());

        return llMainBody;
    }

    // 输入区域的图片
    private RelativeLayout getThumbView() {
        rlThumb = new RelativeLayout(getContext());
        rlThumb.setId(R.id.rlThumb);

        int dp_82 = dipToPx(getContext(), 82);
        int dp_98 = dipToPx(getContext(), 98);
        LinearLayout.LayoutParams lpThumb = new LinearLayout.LayoutParams(dp_82, dp_82);
        rlThumb.setLayoutParams(lpThumb);

        ivImage = new ImageView(getContext());
        int resId = getBitmapRes(activity, "btn_back_nor");
        if (resId > 0) {
            ivImage.setBackgroundResource(resId);
        }
        ivImage.setScaleType(ScaleType.FIT_CENTER);
        ivImage.setImageBitmap(image);
        int dp_4 = dipToPx(getContext(), 4);
        ivImage.setPadding(dp_4, dp_4, dp_4, dp_4);
        int dp_74 = dipToPx(getContext(), 74);
        RelativeLayout.LayoutParams lpImage = new RelativeLayout.LayoutParams(dp_74, dp_74);
        int dp_16 = dipToPx(getContext(), 16);
        int dp_8 = dipToPx(getContext(), 8);
        lpImage.setMargins(0, 0, 0, 0);
        ivImage.setLayoutParams(lpImage);
        ivImage.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (image != null && !image.isRecycled()) {
                    PicViewer pv = new PicViewer();
                    pv.setImageBitmap(image);
                    pv.show(activity, null);
                }
            }
        });
        rlThumb.addView(ivImage);

        Button btn = new Button(getContext());
        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // 取消分享图片
                rlThumb.setVisibility(View.GONE);
                ivPin.setVisibility(View.GONE);
                shareImage = false;
            }
        });
        resId = getBitmapRes(activity, "img_cancel");
        if (resId > 0) {
            btn.setBackgroundResource(resId);
        }
        int dp_20 = dipToPx(getContext(), 20);
        RelativeLayout.LayoutParams lpBtn = new RelativeLayout.LayoutParams(dp_20, dp_20);
        lpBtn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lpBtn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        btn.setLayoutParams(lpBtn);
        rlThumb.addView(btn);

        rlThumb.setVisibility(View.GONE);
        return rlThumb;
    }

    private void showThumb() {
        String imagePath = (String) reqData.get("imagePath");
        Bitmap viewToShare = (Bitmap) reqData.get("viewToShare");
        shareImage = false;
        if (!TextUtils.isEmpty(imagePath) && new File(imagePath).exists()) {
            try {
                shareImage = true;
                image = getBitmap(imagePath);
            } catch (Throwable t) {
                System.gc();
                try {
                    image = getBitmap(imagePath, 2);
                } catch (Throwable t1) {
                    t1.printStackTrace();
                    shareImage = false;
                }
            }

            if (shareImage) {
                rlThumb.setVisibility(View.VISIBLE);
                ivPin.setVisibility(View.VISIBLE);
                ivImage.setImageBitmap(image);
            }
        } else if (viewToShare != null && !viewToShare.isRecycled()) {
            shareImage = true;
            image = viewToShare;

            if (shareImage) {
                rlThumb.setVisibility(View.VISIBLE);
                ivPin.setVisibility(View.VISIBLE);
                ivImage.setImageBitmap(image);
            }
        } else if (reqData.containsKey("imageUrl")) {

            imagePath = String.valueOf(reqData.get("imageUrl"));

            ImageLoaderUtils.setImage(imagePath, ivImage, new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    rlThumb.setVisibility(View.GONE);
                    ivPin.setVisibility(View.GONE);
                    shareImage = false;
                }

                @Override
                public void onLoadingComplete(String s, View view, final Bitmap bitmap) {
                    shareImage = true;
                    UIHandler.sendEmptyMessage(1, new Callback() {
                        public boolean handleMessage(Message msg) {
                            rlThumb.setVisibility(View.VISIBLE);
//                            ivPin.setVisibility(View.VISIBLE);
//                            shareImage = true;
                            image = bitmap;
                            return false;
                        }
                    });
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });

        }
    }


    private LinearLayout getBodyBottom() {
        LinearLayout llBottom = new LinearLayout(getContext());
        llBottom.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        String platform = String.valueOf(reqData.get("platform"));
        LinearLayout line = getAtLine(platform);
        if (line != null) {
            llBottom.addView(line);
        }

        // 字数统计
        tvCounter = new TextView(getContext());
        tvCounter.setText(String.valueOf(MAX_TEXT_COUNT));
        tvCounter.setTextColor(0xffcfcfcf);
        tvCounter.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tvCounter.setTypeface(Typeface.DEFAULT_BOLD);
        LinearLayout.LayoutParams lpCounter = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        lpCounter.gravity = Gravity.CENTER_VERTICAL;
        tvCounter.setLayoutParams(lpCounter);
//		llBottom.addView(tvCounter);

        return llBottom;
    }

    // 进新浪微博、腾讯微博、Facebook和Twitter支持At功能
    private LinearLayout getAtLine(String platform) {
        if ("SinaWeibo".equals(platform) || "TencentWeibo".equals(platform) || "Facebook".equals(platform)
                || "Twitter".equals(platform) || "FacebookMessenger".equals(platform)) {
            LinearLayout llAt = new LinearLayout(getContext());
            LinearLayout.LayoutParams lpAt = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            lpAt.rightMargin = dipToPx(getContext(), 4);
            lpAt.gravity = Gravity.LEFT | Gravity.BOTTOM;
            lpAt.weight = 1;
            llAt.setLayoutParams(lpAt);
            llAt.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    FollowListPage subPage = new FollowListPage();
                    subPage.setPlatform(platforms.get(0));
                    subPage.showForResult(activity, null, EditPage.this);
                }
            });
            TextView tvAt = new TextView(getContext());
            int resId = getBitmapRes(activity, "btn_back_nor");
            if (resId > 0) {
                tvAt.setBackgroundResource(resId);
            }
            int dp_32 = dipToPx(getContext(), 32);
            tvAt.setLayoutParams(new LinearLayout.LayoutParams(dp_32, dp_32));
            tvAt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            tvAt.setText("FacebookMessenger".equals(platform) ? "To" : "@");
            int dp_2 = dipToPx(getContext(), 2);
            tvAt.setPadding(0, 0, 0, dp_2);
            tvAt.setTypeface(Typeface.DEFAULT_BOLD);
            tvAt.setTextColor(0xff000000);
            tvAt.setGravity(Gravity.CENTER);
            llAt.addView(tvAt);

            TextView tvName = new TextView(getContext());
            tvName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            tvName.setTextColor(0xff000000);
            resId = getStringRes(activity, "list_friends");
            String text = getContext().getString(resId, getName(platform));
            tvName.setText(text);
            LinearLayout.LayoutParams lpName = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            lpName.gravity = Gravity.CENTER_VERTICAL;
            tvName.setLayoutParams(lpName);
            llAt.addView(tvName);

            return llAt;
        }

        return null;
    }

    private View getSep() {
        View vSep = new View(getContext());
        vSep.setBackgroundColor(getContext().getResources().getColor(R.color.drivi_line));
        int dp_1 = dipToPx(getContext(), 1);
        LinearLayout.LayoutParams lpSep = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dp_1);
        int top = dipToPx(getContext(), 10);
        lpSep.setMargins(0, top, 0, 0);
        vSep.setLayoutParams(lpSep);
        return vSep;
    }

    // 平台Logo列表
    private LinearLayout getPlatformList() {
        LinearLayout llToolBar = new LinearLayout(getContext());
        LinearLayout.LayoutParams lpTb = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        llToolBar.setLayoutParams(lpTb);

        TextView tvShareTo = new TextView(getContext());
        int resId = getStringRes(activity, "share_to");
        if (resId > 0) {
            tvShareTo.setText(resId);
        }
        tvShareTo.setTextColor(0xffcfcfcf);
        tvShareTo.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        int dp_9 = dipToPx(getContext(), 9);
        LinearLayout.LayoutParams lpShareTo = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        lpShareTo.gravity = Gravity.CENTER_VERTICAL;
        lpShareTo.setMargins(dp_9, 0, 0, 0);
        tvShareTo.setLayoutParams(lpShareTo);
        llToolBar.addView(tvShareTo);

        HorizontalScrollView sv = new HorizontalScrollView(getContext());
        sv.setHorizontalScrollBarEnabled(false);
        sv.setHorizontalFadingEdgeEnabled(false);
        LinearLayout.LayoutParams lpSv = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        lpSv.setMargins(dp_9, dp_9, dp_9, dp_9);
        sv.setLayoutParams(lpSv);
        llToolBar.addView(sv);

        llPlat = new LinearLayout(getContext());
        llPlat.setLayoutParams(new HorizontalScrollView.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT));
        sv.addView(llPlat);

        return llToolBar;
    }

    // 别针图片
    private ImageView getImagePin() {
        ivPin = new ImageView(getContext());
        int resId = getBitmapRes(activity, "pin");
        if (resId > 0) {
            ivPin.setImageResource(resId);
        }
        int dp_80 = dipToPx(getContext(), 80);
        int dp_36 = dipToPx(getContext(), 36);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(dp_80, dp_36);
        lp.topMargin = dipToPx(getContext(), 6);
        lp.addRule(RelativeLayout.ALIGN_TOP, llBody.getId());
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ivPin.setLayoutParams(lp);
        ivPin.setVisibility(View.GONE);

        return ivPin;
    }

    private void genBackground() {
        background = new ColorDrawable(DIM_COLOR);
        if (tmpBgView != null) {
            try {
                Bitmap bgBm = captureView(tmpBgView, tmpBgView.getWidth(), tmpBgView.getHeight());
                bgBm = blur(bgBm, 20, 8);
                BitmapDrawable blurBm = new BitmapDrawable(activity.getResources(), bgBm);
                background = new LayerDrawable(new Drawable[]{blurBm, background});
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private String getName(String platform) {
        if (platform == null) {
            return "";
        }

        int resId = getStringRes(getContext(), platform);
        return getContext().getString(resId);
    }

    public void onClick(View v) {

        if (v.getId() == (R.id.btn_share)) {
            String text = etContent.getText().toString();
            reqData.put("text", text);
            if (!shareImage) {
                if (reqData.get("imagePath") == null) {
                    reqData.put("viewToShare", null);
                    reqData.put("imageUrl", null);
                } else if (reqData.get("imageUrl") == null) {
                    reqData.put("imagePath", null);
                    reqData.put("viewToShare", null);
                } else {
                    reqData.put("imageUrl", null);
                    reqData.put("imagePath", null);
                }
            }
            String platform = String.valueOf(reqData.get("platform"));
            if ("FacebookMessenger".equals(platform)) {
                if (toFriendList != null && toFriendList.size() > 0) {
                    reqData.put("address", toFriendList.get(toFriendList.size() - 1));
                }
                if (reqData.get("address") == null) {
                    int resId = getStringRes(activity, "select_a_friend");
                    if (resId > 0) {
                        Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
            }

            HashMap<Platform, HashMap<String, Object>> editRes = new HashMap<Platform, HashMap<String, Object>>();
            boolean selected = false;
            for (int i = 0; i < views.length; i++) {
                if (views[i].getVisibility() != View.VISIBLE) {
                    editRes.put(platformList[i], reqData);
                    selected = true;
                }
            }

            if (selected) {
                HashMap<String, Object> res = new HashMap<String, Object>();
                res.put("editRes", editRes);
                setResult(res);
                finish();
            } else {
                int resId = getStringRes(activity, "select_one_plat_at_least");
                if (resId > 0) {
                    Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
                }
            }
            return;
        }

        if (v instanceof FrameLayout) {
            ((FrameLayout) v).getChildAt(1).performClick();
            return;
        }

        if (v.getVisibility() == View.INVISIBLE) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 显示平台列表
     */
    public void afterPlatformListGot() {
        String name = String.valueOf(reqData.get("platform"));
        int size = platformList == null ? 0 : platformList.length;
        views = new View[size];

        final int dp_24 = dipToPx(getContext(), 24);
        LinearLayout.LayoutParams lpItem = new LinearLayout.LayoutParams(dp_24, dp_24);
        final int dp_9 = dipToPx(getContext(), 9);
        lpItem.setMargins(0, 0, dp_9, 0);
        FrameLayout.LayoutParams lpMask = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        lpMask.gravity = Gravity.LEFT | Gravity.TOP;
        int selection = 0;
        for (int i = 0; i < size; i++) {
            FrameLayout fl = new FrameLayout(getContext());
            fl.setLayoutParams(lpItem);
            if (i >= size - 1) {
                fl.setLayoutParams(new LinearLayout.LayoutParams(dp_24, dp_24));
            }
            llPlat.addView(fl);
            fl.setOnClickListener(this);

            ImageView iv = new ImageView(getContext());
            iv.setScaleType(ScaleType.CENTER_INSIDE);
            iv.setImageBitmap(getPlatLogo(platformList[i]));
            iv.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            fl.addView(iv);

            views[i] = new View(getContext());
            views[i].setBackgroundColor(0xcfffffff);
            views[i].setOnClickListener(this);
            if (name != null && name.equals(platformList[i].getName())) {
                views[i].setVisibility(View.INVISIBLE);
                selection = i;
                // 编辑分享内容的统计
                ShareSDK.logDemoEvent(3, platformList[i]);
            }
            views[i].setLayoutParams(lpMask);
            fl.addView(views[i]);
            views[i].performClick();
        }

        final int postSel = selection;
        UIHandler.sendEmptyMessageDelayed(0, 333, new Callback() {
            public boolean handleMessage(Message msg) {
                HorizontalScrollView hsv = (HorizontalScrollView) llPlat.getParent();
                hsv.scrollTo(postSel * (dp_24 + dp_9), 0);
                return false;
            }
        });
    }

    private Bitmap getPlatLogo(Platform plat) {
        if (plat == null) {
            return null;
        }

        String name = plat.getName();
        if (name == null) {
            return null;
        }

        String resName = "logo_" + plat.getName();
        int resId = getBitmapRes(activity, resName);
        if (resId > 0) {
            return BitmapFactory.decodeResource(activity.getResources(), resId);
        }
        return null;
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int remain = MAX_TEXT_COUNT - etContent.length();
        tvCounter.setText(String.valueOf(remain));
        tvCounter.setTextColor(remain > 0 ? 0xffcfcfcf : 0xffff0000);
    }

    public void afterTextChanged(Editable s) {

    }

    public void onResult(HashMap<String, Object> data) {
        if (data != null && data.containsKey("selected")) {
            @SuppressWarnings("unchecked")
            ArrayList<String> selected = (ArrayList<String>) data.get("selected");
            String platform = String.valueOf(reqData.get("platform"));
            if ("FacebookMessenger".equals(platform)) {
                toFriendList = selected;
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (String sel : selected) {
                sb.append('@').append(sel).append(' ');
            }
            etContent.append(sb.toString());
        }
    }

    private void hideSoftInput() {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etContent.getWindowToken(), 0);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public boolean onFinish() {
        hideSoftInput();
        return super.onFinish();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        int orientation = activity.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSoftInput();
            Window win = activity.getWindow();
            win.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                    | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            rlPage.setBackgroundColor(DIM_COLOR);
            rlPage.postDelayed(new Runnable() {
                public void run() {
                    genBackground();


                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        rlPage.setBackgroundDrawable(background);
                    } else {
                        rlPage.setBackground(background);
                    }


                }
            }, 1000);
        } else {
            hideSoftInput();
            Window win = activity.getWindow();
            win.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
                    | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            rlPage.setBackgroundColor(DIM_COLOR);
            rlPage.postDelayed(new Runnable() {
                public void run() {
                    genBackground();

                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        rlPage.setBackgroundDrawable(background);
                    } else {
                        rlPage.setBackground(background);
                    }


                }
            }, 1000);
        }
    }

}
