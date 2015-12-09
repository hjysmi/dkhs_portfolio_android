package com.dkhs.portfolio.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.percent.PercentFrameLayout;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.AgreementTextActivity;
import com.dkhs.portfolio.ui.PersonalIntroduceActivity;
import com.dkhs.portfolio.ui.city.SelectProviceActivity;
import com.dkhs.portfolio.ui.eventbus.BackCityEvent;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.pickphoto.PhotoPickerActivity;
import com.dkhs.portfolio.ui.widget.MyActionSheetDialog;
import com.dkhs.portfolio.utils.UIUtils;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 牛人招募之个人资料
 * Created by xuetong on 2015/12/7.
 */
public class PersonalFragment extends BaseFragment implements View.OnClickListener {
    private TextView rlt_agreement;
    private PercentFrameLayout fm_city;
    private PercentFrameLayout fm_introduce;
    public static final int RESULT_INTRODUCE_BACK = 1;
    private TextView tv_introduce;
    private TextView tv_city;
    private TextView et_name;
    private Button but_update;
    private ImageView iv_upimg;
    private ImageView iv_upbg;
    private TextView tv_upimgintroduce;
    private EditText et_id;
    private List<MyActionSheetDialog.SheetItem> items = new ArrayList<MyActionSheetDialog.SheetItem>();

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_personal;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvents();
    }


    private void initView(View view) {
        rlt_agreement = (TextView) view.findViewById(R.id.rlt_agreement);
        fm_introduce = (PercentFrameLayout) view.findViewById(R.id.fm_introduce);
        tv_introduce = (TextView) view.findViewById(R.id.tv_introduce);
        fm_city = (PercentFrameLayout) view.findViewById(R.id.fm_city);
        tv_city = (TextView) view.findViewById(R.id.tv_city);
        but_update = (Button) view.findViewById(R.id.but_update);
        et_name = (TextView) view.findViewById(R.id.et_name);
        iv_upimg = (ImageView) view.findViewById(R.id.iv_upimg);
        iv_upbg = (ImageView) view.findViewById(R.id.iv_upbg);
        tv_upimgintroduce = (TextView) view.findViewById(R.id.tv_upimgintroduce);
        et_id = (EditText) view.findViewById(R.id.et_id);
       // et_id.addTextChangedListener(et_id_textwatcher);
        et_name.addTextChangedListener(et_name_textwatcher);
    }

    private void initEvents() {
        rlt_agreement.setOnClickListener(this);
        fm_introduce.setOnClickListener(this);
        fm_city.setOnClickListener(this);
        but_update.setOnClickListener(this);
    }

    TextWatcher et_name_textwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                String temp = s.toString();
                String tem = temp.substring(temp.length() - 1, temp.length());
                char[] temC = tem.toCharArray();
                // int mid = temC[0];
                if (!UIUtils.isChinese(temC[0])) {
                    s.delete(temp.length() - 1, temp.length());
                }
            } catch (Exception e) {
            }
        }
    };

    @Subscribe
    public void getCity(BackCityEvent event) {
        tv_city.setText(event.city);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlt_agreement:
                Intent intent = new Intent(getActivity(), AgreementTextActivity.class);
                startActivity(intent);
                break;
            case R.id.fm_introduce:
                Intent intent_introduce = new Intent(getActivity(), PersonalIntroduceActivity.class);
                startActivityForResult(intent_introduce, RESULT_INTRODUCE_BACK);
                break;
            case R.id.fm_city:
                UIUtils.startAnimationActivity(getActivity(), new Intent(getActivity(), SelectProviceActivity.class));
                break;
            case R.id.but_update:
                items.clear();
                items.add(new MyActionSheetDialog.SheetItem(getString(R.string.take_picture), MyActionSheetDialog.SheetItemColor.Black));
                items.add(new MyActionSheetDialog.SheetItem(getString(R.string.local_image), MyActionSheetDialog.SheetItemColor.Black));
                showPicDialog();
                break;
        }

    }

    //选择图片
    private void showPicDialog() {
        MyActionSheetDialog dialog = new MyActionSheetDialog(getActivity()).builder().setCancelable(true)
                .setCanceledOnTouchOutside(true);
        for (int i = 0; i < items.size(); i++) {
            dialog.addSheetItem(items.get(i));
        }
        dialog.setSheetItemClickListener(new MyActionSheetDialog.SheetItemClickListener() {

            @Override
            public void onSheetItemClick(int position) {
                switch (position) {
                    case 0: {
                        dispatchTakePictureIntent();
                    }
                    break;

                    case 1: {
                        pickSinglePicture();
                    }
                    break;
                    default:
                        break;
                }
            }
        });
        dialog.show();
    }

    public final static int RCODE_PICK_PICTURE = 700;
    public final static int RCODE_TAKE_PHOTO = 800;

    public void pickSinglePicture() {

        Intent intent = PhotoPickerActivity.getSinglePicIntent(getActivity());
        startActivityForResult(intent, RCODE_PICK_PICTURE);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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

    private void selectPickBack(Bundle bundle) {

        if (bundle != null) {
            List<String> photos = bundle.getStringArrayList(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            if (null != photos && photos.size() > 0) {
                iv_upimg.setVisibility(View.VISIBLE);
                iv_upimg.setImageBitmap(UIUtils.getLocaleimage(photos.get(0)));
                but_update.setText("修改");
                tv_upimgintroduce.setVisibility(View.GONE);
                iv_upbg.setVisibility(View.GONE);
            }
            //  uploadImage();
        }
    }

    private void takePhotoBack() {

        if (TextUtils.isEmpty(mCurrentPhotoPath)) {
            iv_upimg.setVisibility(View.VISIBLE);
            iv_upimg.setImageBitmap(UIUtils.getLocaleimage(mCurrentPhotoPath));
            but_update.setText("修改");
            tv_upimgintroduce.setVisibility(View.GONE);
            iv_upbg.setVisibility(View.GONE);
        }
        // uploadImage();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            if (requestCode == RCODE_PICK_PICTURE) {
                // 相册选择
                selectPickBack(data.getExtras());
            }
            if (requestCode == RCODE_TAKE_PHOTO) {
                takePhotoBack();

            }

            if (requestCode == RESULT_INTRODUCE_BACK) {
                if (null == data) {
                    return;
                } else {
                    String content = data.getStringExtra(PersonalIntroduceActivity.RESULT_CONTENT);
                    tv_introduce.setText(content);
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
