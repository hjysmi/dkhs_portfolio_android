package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.ImageButton;
import com.dkhs.portfolio.base.widget.ImageView;
import com.dkhs.portfolio.base.widget.TextView;
import com.dkhs.portfolio.bean.AccountInfoBean;
import com.dkhs.portfolio.bean.DraftBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.DraftEngine;
import com.dkhs.portfolio.engine.LocalDataEngine.WalletEngineImpl;
import com.dkhs.portfolio.engine.UploadImageEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.service.PostTopicService;
import com.dkhs.portfolio.ui.adapter.DKHSEmojisPagerAdapter;
import com.dkhs.portfolio.ui.adapter.EmojiData;
import com.dkhs.portfolio.ui.adapter.SelectPicAdapter;
import com.dkhs.portfolio.ui.fragment.DKHSEmojiFragment;
import com.dkhs.portfolio.ui.fragment.FragmentSearchStockFund;
import com.dkhs.portfolio.ui.pickphoto.PhotoPickerActivity;
import com.dkhs.portfolio.ui.widget.DKHSEditText;
import com.dkhs.portfolio.ui.widget.GridViewEx;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.ui.widget.MyActionSheetDialog;
import com.dkhs.portfolio.ui.widget.MyActionSheetDialog.SheetItem;
import com.dkhs.portfolio.utils.DKHtml;
import com.dkhs.portfolio.utils.PromptManager;
import com.google.gson.Gson;
import com.rockerhieu.emojicon.emoji.Emojicon;

import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangcm on 2015/7/16.
 */
public class PostRewardActivity extends ModelAcitivity implements DKHSEmojiFragment.OnEmojiconBackspaceClickedListener, DKHSEmojiFragment.OnEmojiconClickedListener, View.OnClickListener, ViewPager.OnPageChangeListener, SelectPicAdapter.IDeletePicListenr {

    //    public static final String MY_CAMERA = "/my_camera";
    //    public static final String UPLOAD_JPG = "/upload.jpg";

    public static final String ADD_PICTURE = "add_picture";
    public static int MAX_TOPIC_PICSIZE = 9;

    private InputMethodManager imm;
    private boolean isShowingEmotionView;
    private ImageButton ibEmoji;
    private ImageButton ibStock;

    private GridViewEx gvSelectPic;
    private SelectPicAdapter mPicAdapter;
    private ArrayList<String> mSelectPohotos = new ArrayList<>();

    /**
     * 发表
     */
    public static final int TYPE_POST = 1;
    /**
     * 评论
     */
    public static final int TYPE_COMMENT = 2;
    /**
     * 回复
     */
    public static final int TYPE_REPLY = 3;
    public static final String REPLIED_STATUS = "replied_status";
    public static final String USER_NAME = "user_name";
    private String repliedStatus;
    private String userName;
    public static final String ARGUMENT_DRAFT = "argument_draft";

    private static final String TYPE = "type";
    private static final String TAG = "PostTopicActivity";
    private int curType;
    private DraftBean mDraftBean;
    private String available = "0.00";


    /**
     * @param context
     * @param type          TYPE_POST:发表悬赏，TYPE_COMMENT:回复悬赏
     * @param repliedStatus
     * @return
     */
    public static Intent getIntent(Context context, int type, String repliedStatus, String userName) {

        Intent intent = new Intent(context, PostRewardActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(REPLIED_STATUS, repliedStatus);
        intent.putExtra(USER_NAME, userName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_post_reward);
        getSwipeBackLayout().setEnableGesture(false);
        AndroidBugForSpecialPhone.assistActivity(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        initViews();
        initEmoji();
        setupViewData();
        getAccountInfo();
    }

    private void getAccountInfo(){
        WalletEngineImpl.getWalletBalance(new ParseHttpListener<AccountInfoBean>() {

            @Override
            protected AccountInfoBean parseDateTask(String jsonData) {
                if (TextUtils.isEmpty(jsonData)) {
                    return null;
                }
                try {
                    AccountInfoBean entity = DataParse.parseObjectJson(AccountInfoBean.class, jsonData);
                    return entity;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void afterParseData(AccountInfoBean object) {
                available = object.getAvailable();
                balanceTv.setText(String.format(getString(R.string.balance),available));
                amountEt.setHint(String.format(getString(R.string.reward_lower_limit), String.valueOf(minAmount)));
                btnSend.setEnabled(true);
                btnSend.setClickable(true);
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                super.onFailure(errCode, errMsg);
            }
        });
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
    private DKHSEditText curEt;
    //    private ImageView ivPhoto;
    private View ibImg;
    private TextView btnSend;
    private LinearLayout llRewardInfo;
    private DKHSEditText amountEt;
    private TextView balanceTv;

    private void initViews() {
        mPicAdapter = new SelectPicAdapter(this, mSelectPohotos);
        gvSelectPic = (GridViewEx) findViewById(R.id.gv_pic);
        gvSelectPic.isExpanded();
        gvSelectPic.setAdapter(mPicAdapter);
        mPicAdapter.setDeletePicListenr(this);
        etContent = (DKHSEditText) findViewById(R.id.et_content);
        llRewardInfo = (LinearLayout)findViewById(R.id.ll_reward_info);
        amountEt = (DKHSEditText)findViewById(R.id.et_reward);
        balanceTv = (TextView)findViewById(R.id.tv_balance);
        ibEmoji = (ImageButton) findViewById(R.id.ib_emoji);
        ibStock = (ImageButton) findViewById(R.id.ib_dollar);
//        ivPhoto = (ImageView) findViewById(R.id.iv_photo);
        ibImg = findViewById(R.id.ib_img);
//        TextView backBtn = (TextView) getBtnBack();
//        backBtn.setCompoundDrawables(null, null, null, null);
//        backBtn.setText(R.string.cancel);
        btnSend = (TextView) getRightButton();
        btnSend.setCompoundDrawables(null, null, null, null);
        btnSend.setText(R.string.send);
        btnSend.setEnabled(false);
        btnSend.setOnClickListener(this);
        setBackButtonListener(this);
        ibStock.setOnClickListener(this);
        ibEmoji.setOnClickListener(this);
        findViewById(R.id.ib_friend).setOnClickListener(this);
        ibImg.setOnClickListener(this);
//        ivPhoto.setOnClickListener(this);
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
        curEt = etContent;
        etContent.requestFocus();
        MyTextWatcher watcher = new MyTextWatcher();
//        etTitle.addTextChangedListener(watcher);
        etContent.addTextChangedListener(watcher);
        amountEt.setFilters(new InputFilter[]{lengthfilter});
//        amountEt.addTextChangedListener(new RewardTextWatcher());
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

    @Override
    protected void onPause() {
        super.onPause();
        hideEmotionView();
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
        if (curType == TYPE_REPLY) {

            setTitle(String.format(getResources().getString(R.string.blank_reply), userName));
            ibImg.setVisibility(View.VISIBLE);
            etContent.setHint(R.string.reward_reply_hint);
            llRewardInfo.setVisibility(View.GONE);
        } else if (curType == TYPE_COMMENT) {
            setTitle(String.format(getResources().getString(R.string.reward_comment), userName));
            ibImg.setVisibility(View.VISIBLE);
            etContent.setHint(R.string.reward_reply_hint);
            llRewardInfo.setVisibility(View.GONE);
        } else if (curType == TYPE_POST) {
            setTitle(R.string.post_reward);
            ibImg.setVisibility(View.VISIBLE);
            etContent.setHint(R.string.reward_hint);
            llRewardInfo.setVisibility(View.VISIBLE);
            amountEt.requestFocus();
        }
        if (null != mDraftBean) {

            amountEt.insertHtmlText(mDraftBean.getRewardAmount());
            etContent.insertHtmlText((mDraftBean.getContent()));
            etContent.setSelection(etContent.getText().length());

            mSelectPohotos.addAll(mDraftBean.getPhotoList());
            if (isTopicType() && mDraftBean.getPhotoList().size() > 0 && mDraftBean.getPhotoList().size() < MAX_TOPIC_PICSIZE) {
                mSelectPohotos.add(ADD_PICTURE);
            }

//            checkSendButtonEnable();
            mPicAdapter.notifyDataSetChanged();

            uploadImageEngine = new UploadImageEngine(mDraftBean.getUploadMap());
            uploadImageEngine.setPhotoList(mDraftBean.getPhotoList());

        } else {
            uploadImageEngine = new UploadImageEngine();
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

    @Override
    public void delFinish() {
//        checkSendButtonEnable();
    }

/*    private boolean checkSendButtonEnable() {

        boolean enAble =  !TextUtils.isEmpty(etContent.getText()) || mSelectPohotos.size() > 0;
        btnSend.setEnabled(enAble);
        btnSend.setClickable(enAble);

        return enAble;


    }*/


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
//            checkSendButtonEnable();
        }
    }

    private class RewardTextWatcher implements TextWatcher {
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
            if(TextUtils.isEmpty(editable.toString())){
                return;
            };
            float amount = Float.valueOf(editable.toString());
            if(amount > MAX_AMOUNT){
                amountEt.setText(String.valueOf(MAX_AMOUNT));
            }
        }

    }


    /**
     * 隐藏表情
     */
    private void hideEmotionView() {
        curEt.requestFocus();

        ibEmoji.setImageResource(R.drawable.kb_icon_emoji);
        if (curEt.getTag() == null) {
            imm.showSoftInput(curEt, 0);
        } else {
            curEt.setTag(null);
            isShowingEmotionView = false;
        }

        findViewById(R.id.ll_emotion).setVisibility(View.GONE);


    }

    /**
     * 展示表情
     */
    private void showEmotionView() {
        curEt.requestFocus();
        ibEmoji.setImageResource(R.drawable.kb_icon_keyboard);
        imm.hideSoftInputFromWindow(curEt.getWindowToken(), 0);


        new Handler().postDelayed(new Runnable() {

            public void run() {
                findViewById(R.id.ll_emotion).setVisibility(View.VISIBLE);

            }

        }, 120);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case RIGHTBUTTON_ID:
                String text = amountEt.getText().toString();
                float amount = !TextUtils.isEmpty(text)?Float.valueOf(text):0;
                if(checkRewardValid(etContent.getText().toString(),amount,Float.valueOf(available))){
                    PostTopicService.startPost(this, buildDrafteBean());
                    finish();
                }

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

            case R.id.ib_img:
                items.clear();
                items.add(new SheetItem(getString(R.string.take_picture), MyActionSheetDialog.SheetItemColor.Black));
                items.add(new SheetItem(getString(R.string.local_image), MyActionSheetDialog.SheetItemColor.Black));
                showPicDialog();
                break;
            case R.id.ib_dollar:
                pickStock();
                break;
            case R.id.ib_friend:
                pickupFriend();
                break;
        }
    }


    public final static int RCODE_PICK_PICTURE = 700;
    public final static int RCODE_TAKE_PHOTO = 800;
    public final static int RCODE_PICK_FRIEND = 600;
    public final static int RCODE_PICK_STOCK = 500;

    public void pickMultiPicture() {
        ArrayList<String> sPhotos = new ArrayList();
        sPhotos.addAll(mSelectPohotos);
        sPhotos.remove(ADD_PICTURE);
        Intent intent = PhotoPickerActivity.getIntent(this, sPhotos);
        startActivityForResult(intent, RCODE_PICK_PICTURE);
    }

    public void pickSinglePicture() {

        Intent intent = PhotoPickerActivity.getSinglePicIntent(this);
        startActivityForResult(intent, RCODE_PICK_PICTURE);
    }


    private void pickStock() {
        Intent intent = new Intent(this,
                SelectStatusStockActivity.class);
        startActivityForResult(intent, RCODE_PICK_STOCK);
    }

    private void pickupFriend() {
        Intent intent = new Intent(this,
                SelectFriendActivity.class);
        startActivityForResult(intent, RCODE_PICK_FRIEND);
    }

    //    private boolean isShowDeletePic = false;
    private List<SheetItem> items = new ArrayList<SheetItem>();

    //选择图片
    private void showPicDialog() {
        MyActionSheetDialog dialog = new MyActionSheetDialog(this).builder().setCancelable(true)
                .setCanceledOnTouchOutside(true);
        for (int i = 0; i < items.size(); i++) {
            dialog.addSheetItem(items.get(i));
        }
        dialog.setSheetItemClickListener(new MyActionSheetDialog.SheetItemClickListener() {

            @Override
            public void onSheetItemClick(int position) {
                switch (position) {
                    case 0: {
                        if (isTopicType() && mSelectPohotos.size() > 0 && !mSelectPohotos.contains(ADD_PICTURE)) {
//                            mSelectPohotos.contains(ADD_PICTURE);
                            PromptManager.showToast(getString(R.string.max_photo_msg, MAX_TOPIC_PICSIZE));
                            return;
                        }
                        dispatchTakePictureIntent();
                    }
                    break;

                    case 1: {

                        if (isTopicType()) {
                            pickMultiPicture();
                        } else {
                            pickSinglePicture();
                        }

                    }
                    break;
                    default:
                        break;
                }
            }
        });
        dialog.show();
    }

    private boolean isTopicType() {
        return curType == TYPE_POST;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == RCODE_PICK_PICTURE) {
                // 相册选择
                selectPickBack(data.getExtras());
            }
            if (requestCode == RCODE_TAKE_PHOTO) {
                takePhotoBack();

            }

            if (requestCode == RCODE_PICK_STOCK) {
                SelectStockBean stockBean = Parcels.unwrap(data.getExtras().getParcelable(FragmentSearchStockFund.EXTRA_STOCK));
                if (null != stockBean) {
                    selectStockBack(stockBean);
                }
            }
            if (requestCode == RCODE_PICK_FRIEND) {
                selectFriendBack(data.getExtras());

            }

//            checkSendButtonEnable();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    String mCurrentPhotoPath;

    //create photo file to take photo
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, RCODE_TAKE_PHOTO);
            }
        }
    }


    private void selectPickBack(Bundle bundle) {

        if (bundle != null) {
            List<String> photos = bundle.getStringArrayList(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            mSelectPohotos.clear();
            mSelectPohotos.addAll(photos);
            if (isTopicType()) {


                if (photos.size() < 9) {
                    mSelectPohotos.add(ADD_PICTURE);
                }

            }

            mPicAdapter.notifyDataSetChanged();
            uploadImage();
        }
    }

    private void takePhotoBack() {
        //have add picture image
        if (isTopicType()) {
            mSelectPohotos.remove(ADD_PICTURE);
            mSelectPohotos.add(mCurrentPhotoPath);
            if (mSelectPohotos.size() < MAX_TOPIC_PICSIZE) {
                mSelectPohotos.add(ADD_PICTURE);
            }
        } else {
            mSelectPohotos.clear();
            mSelectPohotos.add(mCurrentPhotoPath);
        }

        mPicAdapter.notifyDataSetChanged();
        uploadImage();
    }


    UploadImageEngine uploadImageEngine;

    private void uploadImage() {
        uploadImageEngine.setPhotoList(mSelectPohotos);

    }

    private void selectStockBack(SelectStockBean stockBean) {
        curEt.insesrStockText(String.format("%s(%s)", stockBean.getName(), stockBean.getSymbol()));

    }

    private void selectFriendBack(Bundle bundle) {
        String username = bundle.getString("select_friend");
        if (!TextUtils.isEmpty(username)) {
            curEt.inserUserText(username);
        }

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


    private boolean isDraftModify() {
        boolean isPhotoNoChange = true;
        String inputContent = etContent.getText().toString();
        ArrayList<String> tempList = new ArrayList<>(mSelectPohotos.size());
        tempList.addAll(mSelectPohotos);
        tempList.remove(ADD_PICTURE);
        if (mDraftBean.getPhotoList() == null && tempList.size() > 0) {
            isPhotoNoChange = false;
        } else if (mDraftBean.getPhotoList() != null) {
            if (mDraftBean.getPhotoList().size() != tempList.size()) {
                isPhotoNoChange = false;
            } else {
                String selectPath = new Gson().toJson(tempList);
                String draftPaths = mDraftBean.getPhotoPaths();
                if (!TextUtils.isEmpty(selectPath) && !TextUtils.isEmpty(draftPaths)) {
                    isPhotoNoChange = selectPath.equals(draftPaths);
                }
            }
        }


        return isPhotoNoChange && inputContent.equals(mDraftBean.getSimpleContent());


    }

    private void showAlertDialog() {


        if (null != mDraftBean) {
            if (isDraftModify()) {
                finish();
                return;
            }
        }

//        if (!checkSendButtonEnable()) {
//            finish();
//            return;
//        }
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

    private DraftBean buildDrafteBean() {
        if (null == mDraftBean) {
            mDraftBean = new DraftBean();
            mDraftBean.setLabel(curType);
            mDraftBean.setReplyUserName(userName);
            if (!TextUtils.isEmpty(repliedStatus)) {
                mDraftBean.setStatusId(repliedStatus);
            }
        }

        String strContent = DKHtml.toHtml(etContent.getText());
        mDraftBean.setFailReason("");
        mDraftBean.setTitle(getString(R.string.post_reward));
        mDraftBean.setSimpleTitle(getString(R.string.post_reward));
        mDraftBean.setContent(strContent);
        mDraftBean.setSimleContent(etContent.getText().toString());
        mSelectPohotos.remove(ADD_PICTURE);
        mDraftBean.setPhotoList(mSelectPohotos);
        mDraftBean.setUploadMap(uploadImageEngine.getUploadMap());
        mDraftBean.setContentType(40);
        mDraftBean.setRewardAmount(amountEt.getText().toString());
        uploadImageEngine.cancelUpload();
        return mDraftBean;

    }

    private void saveDraft() {
        new DraftEngine(null).saveDraft(buildDrafteBean());

    }


    private static final float MAX_AMOUNT = 99999.99f;
    private float minAmount = 10.00f;
    /**
     * 限制悬赏金额  大于指定最小金额，小于99999.99
     */

    private boolean checkAmountLegal(float amount){
        if(amount < minAmount || amount > MAX_AMOUNT){
            return false;
        }
        return true;
    }

    private static final int DECIMAL_DIGITS = 2;
    /**
     * 限制小数位数
     */
    InputFilter lengthfilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            // 删除等特殊字符，直接返回
            if ("".equals(source.toString())) {
                return null;
            }
            String dValue = dest.toString();
            String[] splitArray = dValue.split("\\.");
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                int diff = dotValue.length() + 1 - DECIMAL_DIGITS;
                if (diff > 0) {
                    return source.subSequence(start, end - diff);
                }
            }
            return null;
        }
    };

    /**
     *限制最大值
     */
    InputFilter limitfilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            if ("".equals(source.toString()) || TextUtils.isEmpty(dest.toString())) {
                return null;
            }
            String dValue = dest.toString();
            float amount = Float.valueOf(dValue);
            if(amount > MAX_AMOUNT){
                return String.valueOf(MAX_AMOUNT);
            }
            return null;
        }
    };
    /**
     * 发布悬赏前进行检查
     */
    private boolean checkRewardValid(String content,float rewardAmount,float available){
        if(rewardAmount < minAmount){
            PromptManager.showToast("没有达到最小金额");
            return false;
        }
/*        if(rewardAmount > MAX_AMOUNT){
            PromptManager.showToast("不能超过"+MAX_AMOUNT);
            return false;
        }*/
        if(rewardAmount > available){
            showChargeDialog();
            return false;
        }
        if(TextUtils.isEmpty(content)){
            PromptManager.showToast("请输入悬赏内容");
            return false;
        }
        return true;
    }

    private void showChargeDialog(){
        MAlertDialog builder = PromptManager.getAlertDialog(this);
        builder.setMessage("余额不足，是否充值").setPositiveButton("充值",null).setNegativeButton("取消",null);
        builder.show();
    }
}
