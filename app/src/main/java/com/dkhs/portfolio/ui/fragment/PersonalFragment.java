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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.percent.PercentFrameLayout;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.PersonalEventBean;
import com.dkhs.portfolio.bean.PersonalQualificationEventBean;
import com.dkhs.portfolio.ui.AgreementTextActivity;
import com.dkhs.portfolio.ui.PersonalIntroduceActivity;
import com.dkhs.portfolio.ui.city.SelectProviceActivity;
import com.dkhs.portfolio.ui.eventbus.BackCityEvent;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.pickphoto.PhotoPickerActivity;
import com.dkhs.portfolio.ui.widget.MyActionSheetDialog;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private Button but_submit;
    private ImageView iv_upimg;
    private ImageView iv_upbg;
    private TextView tv_upimgintroduce;
    private EditText et_id;
    private CheckBox cb_agree;
    private boolean hasphotos = false;
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
        but_submit = (Button) view.findViewById(R.id.but_submit);
        tv_upimgintroduce = (TextView) view.findViewById(R.id.tv_upimgintroduce);
        et_id = (EditText) view.findViewById(R.id.et_id);
        cb_agree = (CheckBox) view.findViewById(R.id.cb_agree);
        // et_id.addTextChangedListener(et_id_textwatcher);
        et_name.addTextChangedListener(et_name_textwatcher);
        et_id.addTextChangedListener(et_id_textwatcher);
    }

    private void initEvents() {
        rlt_agreement.setOnClickListener(this);
        fm_introduce.setOnClickListener(this);
        fm_city.setOnClickListener(this);
        but_update.setOnClickListener(this);
        but_submit.setOnClickListener(this);
        but_submit.setEnabled(false);
        cb_agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               checkSubmit();
            }
        });
    }

    TextWatcher et_id_textwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkSubmit();
        }
    };
    TextWatcher et_name_textwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkSubmit();
        }
    };

    /**
     * 验证身份证
     *
     * @param str_id
     * @return
     */
    private boolean checkId(String str_id) {
        boolean flag = false;
        try {
            String check = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(str_id);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }

    /**
     * 验证中文
     *
     * @param str_id
     * @return
     */
    private boolean checkName(String str_id) {
        boolean flag = false;
        try {
            String check = "^([\\u4e00-\\u9fa5]+)$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(str_id);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }

    private void checkSubmit() {
        if (!TextUtils.isEmpty(et_name.getText().toString().trim()) && !TextUtils.isEmpty(et_id.getText().toString().trim())
                && !TextUtils.isEmpty(tv_city.getText().toString().trim())
                && !TextUtils.isEmpty(tv_introduce.getText().toString().trim()) && hasphotos
                && cb_agree.isChecked()) {
            but_submit.setEnabled(true);
        } else {
            but_submit.setEnabled(false);
        }
       /* if (checkName(et_name.getText().toString().trim()) && checkId(et_id.getText().toString().trim())
                && !TextUtils.isEmpty(tv_city.getText().toString().trim())
                && !TextUtils.isEmpty(tv_introduce.getText().toString().trim()) && !TextUtils.isEmpty(mCurrentPhotoPath)
                && cb_agree.isChecked()) {
            but_submit.setEnabled(true);
        } else {
            but_submit.setEnabled(false);
        }*/
    }

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
                intent_introduce.putExtra(PersonalIntroduceActivity.RESULT_CONTENT, tv_introduce.getText().toString().trim());
                UIUtils.startAnimationActivity(getActivity(), intent_introduce);
                //   startActivityForResult(intent_introduce, RESULT_INTRODUCE_BACK);
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
            case R.id.but_submit:
                //
                if (checkName(et_name.getText().toString().trim())) {
                    //姓名匹配
                    if (checkId(et_id.getText().toString().trim())) {
                        //身份证匹配
                        BusProvider.getInstance().post(new PersonalEventBean());
                    } else {
                        //身份证不匹配
                        PromptManager.showShortToast("请填写正确的身份证号");
                    }
                } else {
                    //姓名不匹配
                    PromptManager.showShortToast("请填写您的真实姓名");
                }

   /* if (checkName(et_name.getText().toString().trim()) && checkId(et_id.getText().toString().trim())
                && !TextUtils.isEmpty(tv_city.getText().toString().trim())
                && !TextUtils.isEmpty(tv_introduce.getText().toString().trim()) && !TextUtils.isEmpty(mCurrentPhotoPath)
                && cb_agree.isChecked()) {
            but_submit.setEnabled(true);
        } else {
            but_submit.setEnabled(false);
        }*/

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
                hasphotos = true;
                checkSubmit();
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
            hasphotos = true;
            checkSubmit();
        }
        // uploadImage();
    }

    @Subscribe
    public void getIntroduce(PersonalQualificationEventBean bean) {
        if (null != bean.name) {
            tv_introduce.setText(bean.name);
            checkSubmit();
        }
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

           /* if (requestCode == RESULT_INTRODUCE_BACK) {
                if (null == data) {
                    return;
                } else {
                    PromptManager.showShortToast("ok");
                    String content = data.getStringExtra(PersonalIntroduceActivity.RESULT_CONTENT);
                    tv_introduce.setText(content);
                    checkSubmit();
                }
            }*/

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
