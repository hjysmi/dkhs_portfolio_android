package com.dkhs.portfolio.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.percent.PercentFrameLayout;
import com.dkhs.adpter.adapter.SingleItemAdapter;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
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

/**
 * 牛人招募之资质资料
 * Created by xuetong on 2015/12/7.
 */
public class QualificationFragment extends BaseFragment implements View.OnClickListener, SelectQualificationAdapter.IDeletePicListenr {
    private ImageView iv_right;
    //private GridViewWithHeaderAndFooter gv;
    private GridViewEx gv;
    private List<String> list;
    private List<Integer> list_img;
    private QualificationAdapter adapter;
    private FrameLayout fm_main;
    private TextView tv_type;
    private LinearLayout ll_main;
    private EditText et_num;
    private TextView tv_organization;
    private Button but_next;
    private PercentFrameLayout fm_organization;
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
    private List<MyActionSheetDialog.SheetItem> items = new ArrayList<MyActionSheetDialog.SheetItem>();

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
        ll_footer = (LinearLayout) view.findViewById(R.id.ll_footer);
        but_next = (Button) view.findViewById(R.id.but_next);
        but_next.setOnClickListener(this);
        sc_content = (ScrollView) view.findViewById(R.id.sc_content);
       /* View head = new View(getActivity());
        head.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (0.01 * width)));*/
        // gv.addHeaderView(head);

        //gv.addFooterView(footer);
        //   fm_main.setPadding((int) (0.05 * width), 0, (int) (0.05 * width), 0);
        gv.setVerticalSpacing((int) (0.03 * width));
        mSelectPohotos.add(ADD_PICTURE);
        mPicAdapter = new SelectQualificationAdapter(getActivity(), mSelectPohotos);
        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.layout_qualification_footer, null);
        initFooter0(footer);

    }

    private void initFooter0(View footer) {
        ll_footer.removeAllViews();
        ll_footer.addView(footer);
        ll_footer.setPadding((int) (0.05 * width), 0, (int) (0.05 * width), 0);
        gvSelectPic = (GridViewEx) footer.findViewById(R.id.gv_pic);
        mPicAdapter = new SelectQualificationAdapter(getActivity(), mSelectPohotos);
        gvSelectPic.isExpanded();
        gvSelectPic.setAdapter(mPicAdapter);
        mPicAdapter.setDeletePicListenr(this);
    }

    private void initFooterqita(View footer_qita) {
        ll_footer.removeAllViews();
        ll_footer.addView(footer_qita);
        ll_footer.setPadding(0, 0, 0, 0);
        et_num = (EditText) footer_qita.findViewById(R.id.et_num);
        tv_organization = (TextView) footer_qita.findViewById(R.id.tv_organization);
        fm_organization = (PercentFrameLayout) footer_qita.findViewById(R.id.fm_organization);
        //  gvSelectPic = (GridViewEx) footer_qita.findViewById(R.id.gv_pic);
        et_num.setPadding((int) (0.05 * width), 0, 0, 0);
        tv_organization.setPadding((int) (0.05 * width), 0, 0, 0);
        fm_organization.setOnClickListener(this);
    }

    private void initValues() {
        list = new ArrayList<>();
        list_img = new ArrayList<>();
        list.add("投资牛人");
        list.add("投资顾问");
        list.add("分析师");
        list.add("期货投资咨询");
        list.add("基金执业资格");
        //
        list_img.add(R.drawable.ic_qualification_better);
        list_img.add(R.drawable.ic_qualification_advister);
        list_img.add(R.drawable.ic_qualification_analyst);
        list_img.add(R.drawable.ic_qualification_investadvice);
        list_img.add(R.drawable.ic_qualification_fund);
        adapter = new QualificationAdapter(getActivity(), list);
        gv.setAdapter(adapter);
        tv_type.setText(list.get(0));
        iv_right.setOnClickListener(this);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //更改背景颜色
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();
                tv_type.setText(list.get(position));
                if (position == 0) {
                    //投资牛人
                    View footer0 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_qualification_footer, null);
                    initFooter0(footer0);
                } else {
                    //其他
                    //  ll_footer.removeAllViews();
                    View footerqita = LayoutInflater.from(getActivity()).inflate(R.layout.layout_qualification_footer_qita, null);
                    //  ll_footer.addView(footer0);
                    initFooterqita(footerqita);
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_right:

             /*   if (isExpand) {
                    //展开
                    ObjectAnimator.ofFloat(gv, "translationY", 0, (int) (1 * width)).setDuration(200).start();
                    iv_right.setImageResource(R.drawable.ic_qualification_up);
                    isExpand = false;
                 //   fm_main.requestLayout();
                  //  gv.requestLayout();
                } else {
                    //收缩
                    ObjectAnimator.ofFloat(gv, "translationY", 0, -(int) (1 * width)).setDuration(200).start();
                    iv_right.setImageResource(R.drawable.ic_qualification_down);
                    isExpand = true;
                  //  fm_main.requestLayout();
                 //   gv.requestLayout();
                }*/
                if (isExpand) {
                    TranslateAnimation animation = new TranslateAnimation(0,0,0,-gv.getHeight());
                    animation.setDuration(300);
                    animation.setFillAfter(true);
                    sc_content.startAnimation(animation);
                    isExpand = false;
                } else {
                    TranslateAnimation animation = new TranslateAnimation(0,0,-gv.getHeight(),0);
                    animation.setDuration(300);
                    animation.setFillAfter(true);
                    sc_content.startAnimation(animation);
                    isExpand = true;
                }

                break;
            case R.id.but_next:
                BusProvider.getInstance().post(new QualificationToPersonalEvent());
                break;
            case R.id.fm_organization:
                UIUtils.startAnimationActivity(getActivity(),new Intent(getActivity(),OrganizationActivity.class));
                break;
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
        // checkSendButtonEnable();
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

    class QualificationAdapter extends SingleItemAdapter<String> {
        Context context;
        List<String> data;
        int selectedPosition;

        public void setSelectedPosition(int selectedPosition) {
            this.selectedPosition = selectedPosition;
        }

        @Override
        public void onBindView(ViewHolder vh, String data, int position) {
            super.onBindView(vh, data, position);
            if (position == selectedPosition) {
                //选中
                vh.getConvertView().setBackgroundResource(R.drawable.but_qualification_pressed);
            } else {
                vh.getConvertView().setBackgroundColor(Color.parseColor("#f5f5f5"));
            }
            TextView tv = vh.get(R.id.tv_name);
            ImageView iv = vh.get(R.id.iv_img);
            tv.setText(data);
            iv.setImageResource(list_img.get(position));
        }

        public QualificationAdapter(Context context, List<String> data) {
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
