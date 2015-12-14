package com.dkhs.portfolio.ui.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.percent.PercentFrameLayout;
import com.android.percent.PercentRelativeLayout;
import com.dkhs.adpter.adapter.SingleItemAdapter;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.OrganizationEventBean;
import com.dkhs.portfolio.bean.OrgtypeBean;
import com.dkhs.portfolio.bean.QualificationEventBean;
import com.dkhs.portfolio.bean.QualificationToPersonalEvent;
import com.dkhs.portfolio.ui.OrganizationActivity;
import com.dkhs.portfolio.ui.adapter.SelectQualificationAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.pickphoto.PhotoPickerActivity;
import com.dkhs.portfolio.ui.widget.GridViewEx;
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
 * 牛人招募之资质资料
 * Created by xuetong on 2015/12/7.
 */
public class QualificationFragment extends BaseFragment implements View.OnClickListener, SelectQualificationAdapter.IDeletePicListenr {
    private ImageView iv_right;
    private EditText et_content;
    //private GridViewWithHeaderAndFooter gv;
    private GridViewEx gv;
    private List<OrgtypeBean> list;
    private List<Integer> list_img;
    private QualificationAdapter adapter;
    private FrameLayout fm_main;
    private TextView tv_type;
    private LinearLayout ll_main;
    private EditText et_num;
    private TextView tv_organization;
    private Button but_next;
    private PercentFrameLayout fm_organization;
    private PercentRelativeLayout rl_top;
    private int width;
    private boolean isExpand = true;
    private SelectQualificationAdapter mPicAdapter;
    private GridViewEx gvSelectPic;
    private LinearLayout ll_footer;
    private List<String> mSelectPohotos = new ArrayList<>();
    public static final String ADD_PICTURE = "add_picture";
    public final static int RCODE_PICK_PICTURE = 700;
    public final static int RCODE_TAKE_PHOTO = 800;
    public static int MAX_TOPIC_PICSIZE = 6;
    private ScrollView sc_content;
    private int selectedItemType = TYPE_FIRST;
    private List<MyActionSheetDialog.SheetItem> items = new ArrayList<MyActionSheetDialog.SheetItem>();
    //  verified_type 认证类型 0, 投资牛人 1, 投资顾问 2, 分析师 3, 基金执业资格 4, 期货投资咨询
    //  org_profile_type 机构类型 0, 证券公司 1,投资咨询公司 2, 公募基金公司 3, 基金第三方销售公司 4, 商业银行 5, 期货公司
    private final static int TYPE_FIRST = 0;
    private final static int TYPE_SECOND = 1;
    private final static int TYPE_THREE = 2;
    private final static int TYPE_FOUR = 3;
    private final static int TYPE_FIVE = 4;
    private int type = 0;

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_qualification;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initValues();
    }

    private void initViews(View view) {
        width = UIUtils.getDisplayMetrics().widthPixels;
        fm_main = (FrameLayout) view.findViewById(R.id.fm_main);
        iv_right = (ImageView) view.findViewById(R.id.iv_right);
        tv_type = (TextView) view.findViewById(R.id.tv_type);
        gv = (GridViewEx) view.findViewById(R.id.gv);
        ll_main = (LinearLayout) view.findViewById(R.id.ll_main);
        rl_top = (PercentRelativeLayout) view.findViewById(R.id.rl_top);
        rl_top.setOnClickListener(this);
        ll_footer = (LinearLayout) view.findViewById(R.id.ll_footer);
        but_next = (Button) view.findViewById(R.id.but_next);
        but_next.setOnClickListener(this);
        but_next.setEnabled(false);
        sc_content = (ScrollView) view.findViewById(R.id.sc_content);

        gv.setVerticalSpacing((int) (0.03 * width));
        mSelectPohotos.add(ADD_PICTURE);
        mPicAdapter = new SelectQualificationAdapter(getActivity(), mSelectPohotos);

        initFooter0();

    }

    private void initFooter0() {
        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.layout_qualification_footer, null);
        ll_footer.removeAllViews();
        ll_footer.addView(footer);
        et_content = (EditText) footer.findViewById(R.id.et_content);
        et_content.setText("");
        mSelectPohotos.clear();
        mSelectPohotos.add(ADD_PICTURE);
        but_next.setEnabled(false);
        et_content.addTextChangedListener(et_content_textwatcher);
        gvSelectPic = (GridViewEx) footer.findViewById(R.id.gv_pic);
        mPicAdapter = new SelectQualificationAdapter(getActivity(), mSelectPohotos);
        gvSelectPic.isExpanded();
        gvSelectPic.setAdapter(mPicAdapter);
        mPicAdapter.setDeletePicListenr(this);
    }

    private void initFooterqita() {
        View footer_qita = LayoutInflater.from(getActivity()).inflate(R.layout.layout_qualification_footer_qita, null);
        but_next.setEnabled(false);
        ll_footer.removeAllViews();
        ll_footer.addView(footer_qita);
        ll_footer.setPadding(0, 0, 0, 0);
        et_num = (EditText) footer_qita.findViewById(R.id.et_num);
        et_num.addTextChangedListener(et_num_textwatcher);
        tv_organization = (TextView) footer_qita.findViewById(R.id.tv_organization);
        // tv_organization.addTextChangedListener(tv_organization_textwatcher);
        fm_organization = (PercentFrameLayout) footer_qita.findViewById(R.id.fm_organization);
        //  gvSelectPic = (GridViewEx) footer_qita.findViewById(R.id.gv_pic);
        et_num.setPadding((int) (0.05 * width), 0, 0, 0);
        tv_organization.setPadding((int) (0.05 * width), 0, 0, 0);
        fm_organization.setOnClickListener(this);
    }

    TextWatcher et_num_textwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String str = s.toString();

            if (str.length() > 0 && !TextUtils.isEmpty(tv_organization.getText().toString())) {
                but_next.setEnabled(true);
            } else {
                but_next.setEnabled(false);
            }
        }
    };

    /**
     * 支持只支持输入英文和中文，空格、下划线
     *
     * @param str
     * @return
     */
    private boolean checknum(String str) {
        boolean flag = false;
        try {
            String check = "^([a-zA-Z-_ \\u4e00-\\u9fa5]+)$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(str);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }

    private void initValues() {
        Bundle arguments = getArguments();
        type = arguments.getInt("type");
        list = new ArrayList<>();
        list_img = new ArrayList<>();
        list.add(new OrgtypeBean("投资牛人", TYPE_FIRST));
        list.add(new OrgtypeBean("投资顾问", TYPE_SECOND));
        list.add(new OrgtypeBean("分析师", TYPE_THREE));
        list.add(new OrgtypeBean("期货投资咨询", TYPE_FOUR));
        list.add(new OrgtypeBean("基金执业资格", TYPE_FIVE));
        //
        list_img.add(R.drawable.ic_qualification_better);
        list_img.add(R.drawable.ic_qualification_advister);
        list_img.add(R.drawable.ic_qualification_analyst);
        list_img.add(R.drawable.ic_qualification_investadvice);
        list_img.add(R.drawable.ic_qualification_fund);
        adapter = new QualificationAdapter(getActivity(), list);
        adapter.setSelectedPosition(type);
        gv.setAdapter(adapter);
        tv_type.setText(list.get(0).getOrgName());

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //更改背景颜色
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();
                tv_type.setText(list.get(position).getOrgName());
                selectedItemType = list.get(position).getType();
                initAnimation();
                if (position == 0) {
                    //投资牛人
                    initFooter0();
                } else {
                    //其他
                    initFooterqita();
                }
            }
        });

    }

    TextWatcher et_content_textwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkSendButtonEnable();

        }
    };
    ArrayList<String> list_photos = new ArrayList();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_top:
                initAnimation();
                break;
            case R.id.but_next:

                if (selectedItemType == TYPE_FIRST) {
                    //投资牛人
                    if (checknum(et_num.getText().toString())) {
                        //验证通过
                        BusProvider.getInstance().post(new QualificationToPersonalEvent());
                    } else {
                        //验证没有通过
                        PromptManager.showShortToast("请填写您的执业编号");
                    }
                } else {
                    BusProvider.getInstance().post(new QualificationToPersonalEvent());
                }

                break;
            case R.id.fm_organization:
                Intent intent = new Intent(getActivity(), OrganizationActivity.class);
                intent.putExtra(OrganizationActivity.KEY_TYPE, selectedItemType);
                UIUtils.startAnimationActivity(getActivity(), intent);
                break;
        }
    }

    private void initAnimation() {
        if (isExpand) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(sc_content, "translationY", 0, -(gv.getHeight() - sc_content.getScrollY())).setDuration(200);
            animator.setInterpolator(new LinearInterpolator());
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    gv.setVisibility(View.GONE);
                    sc_content.setTranslationY(0);
                    sc_content.setScrollY(0);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float interpolatedTime = animation.getAnimatedFraction();
                    gv.getLayoutParams().height = gv.getHeight()
                            - (int) (gv.getHeight() * interpolatedTime);
                    gv.requestLayout();
                }
            });
            animator.start();

            iv_right.setImageResource(R.drawable.ic_qualification_up);
            isExpand = false;
        } else {

            ObjectAnimator animator = ObjectAnimator.ofFloat(sc_content, "translationY", -(gv.getHeight() - sc_content.getScrollY()), 0).setDuration(200);
            animator.setInterpolator(new LinearInterpolator());
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    gv.setVisibility(View.VISIBLE);
                    sc_content.setTranslationY(0);
                    sc_content.setScrollY(0);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float interpolatedTime = animation.getAnimatedFraction();
                    if (interpolatedTime == 1) {
                        gv.setVisibility(View.VISIBLE);
                    } else {
                        gv.getLayoutParams().height = (int) (gv.getHeight() * interpolatedTime);
                        gv.requestLayout();
                        gv.setVisibility(View.VISIBLE);
                    }
                }
            });
            animator.start();

            iv_right.setImageResource(R.drawable.ic_qualification_down);
            isExpand = true;
        }

    }

    @Subscribe
    public void getOrganization(OrganizationEventBean bean) {
        if (null != bean.name) {
            tv_organization.setText(bean.name);
            // String str = s.toString().trim();
            if (et_num.length() > 0 && !TextUtils.isEmpty(bean.name)) {
                but_next.setEnabled(true);
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            if (requestCode == RCODE_PICK_PICTURE) {
                // 相册选择
                selectPickBack(data.getExtras());
                checkSendButtonEnable();
            }
            if (requestCode == RCODE_TAKE_PHOTO) {
                takePhotoBack();
                checkSendButtonEnable();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
        //  super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isTopicType() {
        return true;
    }

    private void takePhotoBack() {
        //have add picture image
        if (isTopicType()) {
            mSelectPohotos.remove(ADD_PICTURE);
            mSelectPohotos.add(mCurrentPhotoPath);

            if (mSelectPohotos.size() < MAX_TOPIC_PICSIZE || mSelectPohotos.size() == 0) {
                mSelectPohotos.add(ADD_PICTURE);
            }
        } else {
            mSelectPohotos.clear();
            mSelectPohotos.add(mCurrentPhotoPath);
        }

        mPicAdapter.notifyDataSetChanged();
        // uploadImage();
    }

    private void selectPickBack(Bundle bundle) {

        if (bundle != null) {
            List<String> photos = bundle.getStringArrayList(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            mSelectPohotos.clear();
            mSelectPohotos.addAll(photos);
            if (isTopicType()) {


                if (photos.size() < 6) {
                    mSelectPohotos.add(ADD_PICTURE);
                }

            }

            mPicAdapter.notifyDataSetChanged();
            //  uploadImage();
        }
    }

    @Override
    public void delFinish() {
        checkSendButtonEnable();

    }


    private void checkSendButtonEnable() {
        list_photos.clear();
        list_photos.addAll(mSelectPohotos);
        list_photos.remove(ADD_PICTURE);
        String str = et_content.getText().toString().trim();
        if (str.length() > 0 && null != list_photos && list_photos.size() > 0) {
            but_next.setEnabled(true);
        } else {
            but_next.setEnabled(false);
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

    public void pickMultiPicture() {
        ArrayList<String> sPhotos = new ArrayList();
        sPhotos.addAll(mSelectPohotos);
        sPhotos.remove(ADD_PICTURE);
        Intent intent = PhotoPickerActivity.getQualificationIntent(getActivity(), sPhotos);
        startActivityForResult(intent, RCODE_PICK_PICTURE);
    }

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

    @Subscribe
    public void addImage(QualificationEventBean event) {
        //  pickMultiPicture();
        items.clear();
        items.add(new MyActionSheetDialog.SheetItem(getString(R.string.take_picture), MyActionSheetDialog.SheetItemColor.Black));
        items.add(new MyActionSheetDialog.SheetItem(getString(R.string.local_image), MyActionSheetDialog.SheetItemColor.Black));
        showPicDialog();

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

    class QualificationAdapter extends SingleItemAdapter<OrgtypeBean> {
        Context context;
        List<OrgtypeBean> data;
        int selectedPosition;

        public void setSelectedPosition(int selectedPosition) {
            this.selectedPosition = selectedPosition;
        }

        @Override
        public void onBindView(ViewHolder vh, OrgtypeBean data, int position) {
            super.onBindView(vh, data, position);
            if (position == selectedPosition) {
                //选中
                vh.getConvertView().setBackgroundResource(R.drawable.but_qualification_pressed);
            } else {
                vh.getConvertView().setBackgroundColor(Color.parseColor("#f5f5f5"));
            }
            TextView tv = vh.get(R.id.tv_name);
            ImageView iv = vh.get(R.id.iv_img);
            tv.setText(data.getOrgName());
            iv.setImageResource(list_img.get(position));
        }

        public QualificationAdapter(Context context, List<OrgtypeBean> data) {
            super(context, data);
            this.context = context;
            this.data = data;
        }

        @Override
        public int getLayoutResId() {
            return R.layout.layout_qualification_gridview;
        }
    }

}
