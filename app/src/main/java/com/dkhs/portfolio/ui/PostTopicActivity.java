package com.dkhs.portfolio.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.ImageView;
import com.dkhs.portfolio.base.widget.TextView;
import com.dkhs.portfolio.bean.DraftBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.DraftEngine;
import com.dkhs.portfolio.service.PostTopicService;
import com.dkhs.portfolio.ui.adapter.DKHSEmojisPagerAdapter;
import com.dkhs.portfolio.ui.adapter.EmojiData;
import com.dkhs.portfolio.ui.fragment.DKHSEmojiFragment;
import com.dkhs.portfolio.ui.fragment.FragmentSearchStockFund;
import com.dkhs.portfolio.ui.widget.DKHSEditText;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.ui.widget.MyActionSheetDialog;
import com.dkhs.portfolio.ui.widget.MyActionSheetDialog.SheetItem;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.rockerhieu.emojicon.emoji.Emojicon;

import org.parceler.Parcels;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangcm on 2015/7/16.
 */
public class PostTopicActivity extends ModelAcitivity implements DKHSEmojiFragment.OnEmojiconBackspaceClickedListener, DKHSEmojiFragment.OnEmojiconClickedListener, View.OnClickListener, ViewPager.OnPageChangeListener {

    public static final String MY_CAMERA = "/my_camera";
    //    public static final String UPLOAD_JPG = "/upload.jpg";
    private Uri photoUri;
    private String jpg_path;
    private InputMethodManager imm;
    private boolean isShowingEmotionView;
    private ImageButton ibEmoji;
    private ImageButton ibStock;

    /**
     * 发表
     */
    public static final int TYPE_POST = 1;
    /**
     * 回复
     */
    public static final int TYPE_RETWEET = 2;
    public static final String REPLIED_STATUS = "replied_status";
    public static final String USER_NAME = "user_name";
    private String repliedStatus;
    private String userName;
    public static final String ARGUMENT_DRAFT = "argument_draft";

    private static final String TYPE = "type";
    private static final String TAG = "PostTopicActivity";
    private int curType;
    private DraftBean mDraftBean;

    /**
     * @param context
     * @param type          TYPE_POST:发表话题，TYPE_RETWEET:评论话题
     * @param repliedStatus
     * @return
     */
    public static Intent getIntent(Context context, int type, String repliedStatus, String userName) {

        Intent intent = new Intent(context, PostTopicActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(REPLIED_STATUS, repliedStatus);
        intent.putExtra(USER_NAME, userName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_post_topic);
        getSwipeBackLayout().setEnableGesture(false);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        initViews();
        initEmoji();
        setupViewData();

    }

    private void handleExtras(Bundle extras) {
        curType = extras.getInt(TYPE);
        userName = extras.getString(USER_NAME);
        mDraftBean = Parcels.unwrap(extras.getParcelable(ARGUMENT_DRAFT));
        repliedStatus = extras.getString(REPLIED_STATUS);

    }

    private void initEmoji() {
        ViewPager vpEmoji = (ViewPager) findViewById(R.id.vp_emoji);
        views = new ArrayList<DKHSEmojiFragment>();
        views.add(DKHSEmojiFragment.newInstance(EmojiData.getData(0)));
        views.add(DKHSEmojiFragment.newInstance(EmojiData.getData(1)));
        views.add(DKHSEmojiFragment.newInstance(EmojiData.getData(2)));
        views.add(DKHSEmojiFragment.newInstance(EmojiData.getData(3)));
        vpEmoji.setAdapter(new DKHSEmojisPagerAdapter(getSupportFragmentManager(), views));
        vpEmoji.setOnPageChangeListener(this);
        initDots();
    }

    private DKHSEditText etContent;
    private DKHSEditText etTitle;
    private DKHSEditText curEt;
    private ImageView ivPhoto;
    private View ibImg;
    private TextView btnSend;

    private void initViews() {
        etTitle = (DKHSEditText) findViewById(R.id.et_title);
        etContent = (DKHSEditText) findViewById(R.id.et_content);
        ibEmoji = (ImageButton) findViewById(R.id.ib_emoji);
        ibStock = (ImageButton) findViewById(R.id.ib_dollar);
        ivPhoto = (ImageView) findViewById(R.id.iv_photo);
        ibImg = findViewById(R.id.ib_img);
        TextView backBtn = (TextView) getBtnBack();
        backBtn.setCompoundDrawables(null, null, null, null);
        backBtn.setText(R.string.cancel);
        btnSend = (TextView) getRightButton();
        btnSend.setCompoundDrawables(null, null, null, null);
        btnSend.setText(R.string.send);
        btnSend.setEnabled(false);
        btnSend.setOnClickListener(this);
        setBackButtonListener(this);
        ibStock.setOnClickListener(this);
        ibEmoji.setOnClickListener(this);
        ibImg.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);
        etContent.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //隐藏表情
                if (isShowingEmotionView) {
                    hideEmotionView();
                    isShowingEmotionView = !isShowingEmotionView;
                }
                if (curEt != etContent)
                    curEt = etContent;
                return onTouchEvent(event);
            }
        });
        etTitle.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //隐藏表情
                if (isShowingEmotionView) {
                    isShowingEmotionView = !isShowingEmotionView;
                }
                if (curEt != etTitle)
                    curEt = etTitle;
                return onTouchEvent(event);
            }
        });
        etTitle.clearFocus();
        curEt = etContent;
        etTitle.setFocusable(false);
        etTitle.setFocusableInTouchMode(false);
        etContent.requestFocus();
        etTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etTitle.isFocusable()) {
                    etTitle.setFocusable(true);
                    etTitle.setFocusableInTouchMode(true);
                    etTitle.requestFocus();
                }
            }
        });
        MyTextWatcher watcher = new MyTextWatcher();
        etTitle.addTextChangedListener(watcher);
        etContent.addTextChangedListener(watcher);
        //初始化软键盘
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private List<DKHSEmojiFragment> views;
    private int etCount;

    // 底部小点图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_image);
        dots = new ImageView[views.size()];
        // 循环取得小点图片
        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);// 都设为灰色
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
    }

    private void setCurrentDot(int position) {
        if (position < 0 || position > views.size() - 1
                || currentIndex == position) {
            return;
        }
        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = position;
        View ll = findViewById(R.id.ll_image);
        if (ll.getVisibility() == View.GONE)
            ll.setVisibility(View.VISIBLE);

    }


    private void setupViewData() {
        if (curType == TYPE_RETWEET) {

            setTitle(String.format(getResources().getString(R.string.blank_reply), userName));
            ibImg.setVisibility(View.GONE);
            etTitle.setVisibility(View.GONE);
        } else if (curType == TYPE_POST) {
            setTitle(R.string.post_topic);
            ibImg.setVisibility(View.VISIBLE);
            etTitle.setVisibility(View.VISIBLE);
        }
        if (null != mDraftBean) {
            etContent.setText(mDraftBean.getContent());
            etTitle.setText(mDraftBean.getTitle());
            if (null != mDraftBean.getImageUri()) {
//                jpg_path = mDraftBean.getImageUri();
                ImageLoaderUtils.setImage(mDraftBean.getImageUri(), ivPhoto);
                ivPhoto.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        setCurrentDot(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private class MyTextWatcher implements TextWatcher {
        private boolean isBeforeNull;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            isBeforeNull = TextUtils.isEmpty(charSequence);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!TextUtils.isEmpty(editable)) {
                btnSend.setEnabled(true);
                btnSend.setClickable(true);
            } else {
                btnSend.setEnabled(false);
                btnSend.setClickable(false);
            }
        }
    }


    /**
     * 隐藏表情
     */
    private void hideEmotionView() {
        curEt.requestFocus();
        findViewById(R.id.ll_emotion).setVisibility(View.GONE);
        ibEmoji.setImageResource(R.drawable.kb_icon_emoji);
        if (curEt.getTag() == null) {
            imm.showSoftInput(curEt, 0);
        } else {
            curEt.setTag(null);
            isShowingEmotionView = false;
        }


    }

    /**
     * 展示表情
     */
    private void showEmotionView() {
        curEt.requestFocus();
        findViewById(R.id.ll_emotion).setVisibility(View.VISIBLE);
        ibEmoji.setImageResource(R.drawable.kb_icon_keyboard);
        imm.hideSoftInputFromWindow(curEt.getWindowToken(), 0);
    }

//    private String filePath = "";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case RIGHTBUTTON_ID:

                PostTopicService.startPost(this, buildDrafteBean());
                finish();

//                if (curType == TYPE_RETWEET) {
//
//                    StatusEngineImpl.postStatus(null, etContent.getText().toString(), repliedStatus, null, 0, 0, null, statusListener.setLoadingDialog(this, false));
//                } else if (curType == TYPE_POST) {
//                    if (TextUtils.isEmpty(jpg_path)) {
//                        //直接发表帖子或评论
//                        StatusEngineImpl.postStatus(etTitle.getText().toString(), etContent.getText().toString(), null, null, 0, 0, null, statusListener.setLoadingDialog(this, false));
//                    } else {
//                        String file_str = Environment.getExternalStorageDirectory().getPath();
//                        StatusEngineImpl.uploadImage(new File(filePath), uploadListener.setLoadingDialog(this, false));
//                    }
//                }
                break;
            case BACKBUTTON_ID:
                showAlertDialog();
                break;
            case R.id.ib_emoji:
                if (isShowingEmotionView) {
                    hideEmotionView();
                } else {
                    showEmotionView();
                }
                isShowingEmotionView = !isShowingEmotionView;
                break;
            case R.id.iv_photo:
                isShowDeletePic = true;
                items.clear();
                items.add(new SheetItem(getString(R.string.retake_picture), MyActionSheetDialog.SheetItemColor.Black));
                items.add(new SheetItem(getString(R.string.delete_picture), MyActionSheetDialog.SheetItemColor.Black));
                showPicDialog();
                break;
            case R.id.ib_img:
                isShowDeletePic = false;
                items.clear();
                items.add(new SheetItem(getString(R.string.take_picture), MyActionSheetDialog.SheetItemColor.Black));
                items.add(new SheetItem(getString(R.string.local_image), MyActionSheetDialog.SheetItemColor.Black));
                showPicDialog();
                break;
            case R.id.ib_dollar:
                pickStock();
                break;
        }
    }


    private void pickStock() {
        Intent intent = new Intent(this,
                SelectStatusStockActivity.class);
        startActivityForResult(intent, 0x7);
    }

    private boolean isShowDeletePic = false;
    private List<MyActionSheetDialog.SheetItem> items = new ArrayList<MyActionSheetDialog.SheetItem>();

    //选择图片
    private void showPicDialog() {
        MyActionSheetDialog dialog = new MyActionSheetDialog(this).builder().setCancelable(true)
                .setCanceledOnTouchOutside(true);
        for (int i = 0; i < items.size(); i++) {
            dialog.addSheetItem(items.get(i));
        }
//        dialog.setTitle(getString(R.string.save_draft));
        dialog.setSheetItemClickListener(new MyActionSheetDialog.SheetItemClickListener() {

            @Override
            public void onSheetItemClick(int position) {
                switch (position) {
                    case 0:
                        if (isShowDeletePic) {
                            //重现选择图片
                            onClick(ibImg);
                        } else {
                            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                                String file_str = Environment.getExternalStorageDirectory().getPath();
                                File mars_file = new File(file_str + MY_CAMERA);
                                // file_go = new File(file_str + jpg_path);
                                // 先创建父目录，如果新创建一个文件的时候，父目录没有存在，那么必须先创建父目录，再新建文件。
                                if (!mars_file.exists()) {
                                    mars_file.mkdirs();
                                }

                                // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                // 并设置拍照的存在方式为外部存储和存储的路径；

                                // intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file_go));
                                // 跳转到拍照界面;
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                                String filename = timeStampFormat.format(new Date());
                                ContentValues values = new ContentValues();
                                values.put(MediaStore.Audio.Media.TITLE, filename);

                                photoUri = getContentResolver()
                                        .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                startActivityForResult(intent, 0x1);
                            } else {
                                PromptManager.showToast(R.string.pls_instore_sdcard);
                            }
                        }
                        break;

                    case 1:
                        if (isShowDeletePic) {
                            ivPhoto.setVisibility(View.GONE);
                            jpg_path = "";
                            imageUri = "";
                        } else {
                        /* 取得相片后返回本画面 */
                            Intent intent = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 0x5);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0x5 && resultCode == RESULT_OK) {
            // 相册选择
            try {
                final Uri uri = data.getData();
                imageUri = uri.toString();
                ivPhoto.setVisibility(View.VISIBLE);
                final String file_str = Environment.getExternalStorageDirectory().getPath();
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(uri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                final String path = cursor.getString(column_index);
                final Bitmap imageBitmap = UIUtils.getLocaleimage(path);
                ivPhoto.setImageBitmap(imageBitmap);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            jpg_path = MY_CAMERA + getTimestampFileName();
                            File f = new File(file_str + jpg_path);
                            if (f.exists()) {
                                f.delete();
                            }
                            FileOutputStream out = new FileOutputStream(f);
                            Bitmap bitmap = UIUtils.loadBitmap(imageBitmap, path);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 80, out);
                            // setImageView(imageBitmap);
                            //                setImageView(UIUtils.cropBitmap(imageBitmap));
                            saveBitmap(f.getAbsolutePath(), bitmap);
                        } catch (Exception e) {
                            Log.e("Exception", e.getMessage(), e);
                        }
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 0x1 && resultCode == RESULT_OK) {
            // 拍照
            final String file_str = Environment.getExternalStorageDirectory().getPath();
            Uri uri = null;
            if (null != data) {
                uri = data.getData();
            }
            if (null == uri && photoUri != null) {
                uri = photoUri;
            }
            if (uri == null) {
                return;
            }
            final Uri finalUri = uri;
            imageUri = uri.toString();
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(finalUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            jpg_path = MY_CAMERA + getTimestampFileName();
            final String path = cursor.getString(column_index);
            final Bitmap imageBitmap = UIUtils.getLocaleimage(path);
            ivPhoto.setVisibility(View.VISIBLE);
            ivPhoto.setImageBitmap(imageBitmap);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        File f = new File(file_str + jpg_path);
                        if (f.exists()) {
                            f.delete();
                        }
                        FileOutputStream out = new FileOutputStream(f);
                        Bitmap bitmap = UIUtils.loadBitmap(imageBitmap, path);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
//                        setImageView(UIUtils.cropBitmap(imageBitmap));
//                        ivPhoto.setImageBitmap(imageBitmap);
                        saveBitmap(f.getAbsolutePath(), bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

        if (requestCode == 0x7 && resultCode == RESULT_OK) {
            SelectStockBean stockBean = Parcels.unwrap(data.getExtras().getParcelable(FragmentSearchStockFund.EXTRA_STOCK));
            if (null != stockBean) {
                selectStockBack(stockBean);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private String getTimestampFileName() {
//        return "/upload.jpg";
        return "/" + System.currentTimeMillis() / 1000 + ".jpg";
    }

    private void selectStockBack(SelectStockBean stockBean) {
        curEt.insesrStockText(String.format("%s(%s)", stockBean.getName(), stockBean.getSymbol()));
    }

    private void saveBitmap(final String path, final Bitmap bitmap) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                File f = new File(path);
                if (f.exists() && !f.toString().equals(path)) {
                    f.delete();
                }
                try {
                    FileOutputStream out = new FileOutputStream(f);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                    // 回收内存中的bitmap
//                    bitmap.recycle();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onEmojiconBackSpaceClicked(Emojicon emojicon) {
        DKHSEmojiFragment.backspace(curEt);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        DKHSEmojiFragment.input(curEt, emojicon);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            onUserInteraction();
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }


    @Override
    public void onBackPressed() {


        showAlertDialog();

    }

    private void showAlertDialog() {

        if (TextUtils.isEmpty(etTitle.getText()) && TextUtils.isEmpty(etContent.getText()) && TextUtils.isEmpty(jpg_path)) {
            finish();
            return;
        }
        MAlertDialog builder = PromptManager.getAlertDialog(this);

        builder.setMessage(R.string.dialog_msg_save_draft)
                .setNegativeButton(R.string.notsave, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();

                    }
                }).setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                saveDraft();
                dialog.dismiss();
                finish();

            }
        });


        builder.show();

    }

    private String imageUri;

    private DraftBean buildDrafteBean() {
        if (null == mDraftBean) {
            mDraftBean = new DraftBean();
            mDraftBean.setLabel(curType);
            mDraftBean.setReplyUserName(userName);
            if (!TextUtils.isEmpty(repliedStatus)) {
                mDraftBean.setStatusId(repliedStatus);
            }
        }

        if (ivPhoto.getVisibility() == View.VISIBLE) {

            if (!TextUtils.isEmpty(jpg_path)) {

                String file_str = Environment.getExternalStorageDirectory().getPath();
                file_str = file_str + jpg_path;
                mDraftBean.setImageFilepath(file_str);
            }
            if (!TextUtils.isEmpty(jpg_path)) {
                mDraftBean.setImageUri(imageUri);
            }
        } else {
            mDraftBean.setImageUri("");
            mDraftBean.setImageFilepath("");
        }

        mDraftBean.setFailReason("");
        mDraftBean.setTitle(etTitle.getText().toString());
        mDraftBean.setContent(etContent.getText().toString());
        return mDraftBean;

    }

    private void saveDraft() {
        new DraftEngine(null).saveDraft(buildDrafteBean());

    }


}
