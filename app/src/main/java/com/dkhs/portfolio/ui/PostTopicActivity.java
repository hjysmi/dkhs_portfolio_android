package com.dkhs.portfolio.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.ImageView;
import com.dkhs.portfolio.base.widget.TextView;
import com.dkhs.portfolio.ui.adapter.DKHSEmojisPagerAdapter;
import com.dkhs.portfolio.ui.adapter.EmojiData;
import com.dkhs.portfolio.ui.fragment.DKHSEmojiFragment;
import com.dkhs.portfolio.ui.widget.DKHSEditText;
import com.dkhs.portfolio.ui.widget.MyActionSheetDialog;
import com.dkhs.portfolio.ui.widget.MyActionSheetDialog.SheetItem;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangcm on 2015/7/16.
 */
public class PostTopicActivity extends ModelAcitivity implements EmojiconsFragment.OnEmojiconBackspaceClickedListener, EmojiconGridFragment.OnEmojiconClickedListener, View.OnClickListener {

    public static final String MY_CAMERA = "/my_camera";
    public static final String UPLOAD_JPG = "/upload.jpg";
    private Uri photoUri;
    private String jpg_path;
    private InputMethodManager imm;
    private boolean isShowingEmotionView;
    private ImageButton ibEmoji;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_post_topic);
        initViews();
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.emojicons, EmojiconsFragment.newInstance(false))
//                .commit();
        initEmoji();
    }

    private void initEmoji() {
        ViewPager vpEmoji = (ViewPager) findViewById(R.id.vp_emoji);
        List<DKHSEmojiFragment> fragments =new ArrayList<DKHSEmojiFragment>();
        fragments.add(DKHSEmojiFragment.newInstance(EmojiData.getData(0)));
        fragments.add(DKHSEmojiFragment.newInstance(EmojiData.getData(1)));
        fragments.add(DKHSEmojiFragment.newInstance(EmojiData.getData(2)));
        fragments.add(DKHSEmojiFragment.newInstance(EmojiData.getData(3)));
        vpEmoji.setAdapter(new DKHSEmojisPagerAdapter( getSupportFragmentManager(), fragments));
    }

    private DKHSEditText etContent;
    private DKHSEditText etTitle;
    private EditText curEt;
    private ImageView ivPhoto;
    private View ibImg;

    private void initViews() {
        setTitle(R.string.post_topic);
        etTitle = (DKHSEditText) findViewById(R.id.et_title);
        etContent = (DKHSEditText) findViewById(R.id.et_content);
        ibEmoji = (ImageButton) findViewById(R.id.ib_emoji);
        ivPhoto = (ImageView) findViewById(R.id.iv_photo);
        ibImg = findViewById(R.id.ib_img);
        TextView backBtn = (TextView) getBtnBack();
        backBtn.setCompoundDrawables(null, null, null, null);
        backBtn.setText(R.string.cancel);
        TextView rightBtn = (TextView) getRightButton();
        rightBtn.setCompoundDrawables(null, null, null, null);
        rightBtn.setText(R.string.send);
        rightBtn.setEnabled(false);
        rightBtn.setOnClickListener(this);
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
                    hideEmotionView();
                    isShowingEmotionView = !isShowingEmotionView;
                }
                if (curEt != etTitle)
                    curEt = etTitle;
                return onTouchEvent(event);
            }
        });
        curEt = etTitle;
        //初始化软键盘
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(curEt);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(curEt, emojicon);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case RIGHTBUTTON_ID:
                // TODO 发表帖子
                break;
            case BACKBUTTON_ID:
                // TODO 返回的时候判断是否有草稿
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
        }
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
                            jpg_path = null;
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
                String file_str = Environment.getExternalStorageDirectory().getPath();
                Uri uri = data.getData();

                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(uri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();

                String path = cursor.getString(column_index);
                Bitmap imageBitmap = UIUtils.getLocaleimage(path);
                jpg_path = MY_CAMERA + UPLOAD_JPG;
                File f = new File(file_str + jpg_path);
                if (f.exists()) {
                    f.delete();
                }
                FileOutputStream out = new FileOutputStream(f);
                imageBitmap = UIUtils.loadBitmap(imageBitmap, path);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                // setImageView(imageBitmap);
                ivPhoto.setVisibility(View.VISIBLE);
                ivPhoto.setImageURI(uri);
//                setImageView(UIUtils.cropBitmap(imageBitmap));
                saveBitmap(f.getAbsolutePath(), imageBitmap);
            } catch (Exception e) {
                Log.e("Exception", e.getMessage(), e);
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
            ivPhoto.setVisibility(View.VISIBLE);
            ivPhoto.setImageURI(uri);
            final Uri finalUri = uri;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = managedQuery(finalUri, proj, null, null, null);
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        jpg_path = MY_CAMERA + UPLOAD_JPG;
                        String path = cursor.getString(column_index);
                        Bitmap imageBitmap = UIUtils.getLocaleimage(path);
                        File f = new File(file_str + jpg_path);
                        if (f.exists()) {
                            f.delete();
                        }
                        FileOutputStream out = new FileOutputStream(f);
                        imageBitmap = UIUtils.loadBitmap(imageBitmap, path);
                        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
//                        setImageView(UIUtils.cropBitmap(imageBitmap));
//                        ivPhoto.setImageBitmap(imageBitmap);
                        saveBitmap(f.getAbsolutePath(), imageBitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
        super.onActivityResult(requestCode, resultCode, data);
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
                    bitmap.recycle();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
