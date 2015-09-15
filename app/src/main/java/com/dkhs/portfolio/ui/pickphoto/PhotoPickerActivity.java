package com.dkhs.portfolio.ui.pickphoto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.ModelAcitivity;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.entity.Photo;
import me.iwf.photopicker.entity.PhotoDirBean;
import me.iwf.photopicker.event.OnItemCheckListener;
import me.iwf.photopicker.fragment.PhotoPickerFragment;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by zjz on 2015/9/14.
 */
public class PhotoPickerActivity extends ModelAcitivity {


    private PhotoPickerFragment pickerFragment;
//    private ImagePagerFragment imagePagerFragment;

    public final static String EXTRA_MAX_COUNT = "MAX_COUNT";
    public final static String EXTRA_SHOW_CAMERA = "SHOW_CAMERA";
    public final static String EXTRA_SHOW_GIF = "SHOW_GIF";
    public final static String KEY_SELECTED_PHOTOS = "SELECTED_PHOTOS";

//    private MenuItem menuDoneItem;

    public final static int DEFAULT_MAX_COUNT = 9;

    private int maxCount = DEFAULT_MAX_COUNT;

    //    /**
//     * to prevent multiple calls to inflate menu
//     */
//    private boolean menuIsInflated = false;
    private TextView btnRight;
    private TextView tvTitle;


    private boolean showGif = false;

    private ArrayList<String> selectFilePath = new ArrayList<>();

    public static Intent getIntent(Context context, ArrayList<String> paths) {
        Intent intent = new Intent(context, PhotoPickerActivity.class);
        intent.putStringArrayListExtra(KEY_SELECTED_PHOTOS, paths);
        return intent;
    }

    public static Intent getSinglePicIntent(Context context) {
        Intent intent = new Intent(context, PhotoPickerActivity.class);
        intent.putExtra(EXTRA_MAX_COUNT, 1);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean showCamera = getIntent().getBooleanExtra(EXTRA_SHOW_CAMERA, false);
        boolean showGif = getIntent().getBooleanExtra(EXTRA_SHOW_GIF, false);
        setShowGif(showGif);

        setContentView(R.layout.activity_photopicker);
        setTitle(R.string.title_picker_photo);
        com.dkhs.portfolio.base.widget.TextView backBtn = (com.dkhs.portfolio.base.widget.TextView) getBtnBack();
        backBtn.setCompoundDrawables(null, null, null, null);
        backBtn.setText(R.string.cancel);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_photodir_down, 0);
        tvTitle.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.widget_margin_small));
        btnRight = getRightButton();
        btnRight.setEnabled(false);


        maxCount = getIntent().getIntExtra(EXTRA_MAX_COUNT, DEFAULT_MAX_COUNT);
        selectFilePath = getIntent().getStringArrayListExtra(KEY_SELECTED_PHOTOS);
        if (null == selectFilePath) {
            selectFilePath = new ArrayList<>();
        } else if (selectFilePath.size() > 0) {
            btnRight.setEnabled(true);
            btnRight.setText(getString(R.string.btn_next_step, selectFilePath.size(), maxCount));
        }


        pickerFragment = (PhotoPickerFragment) getSupportFragmentManager().findFragmentById(me.iwf.photopicker.R.id.photoPickerFragment);

        pickerFragment.getPhotoGridAdapter().setShowCamera(showCamera);
        pickerFragment.getPhotoGridAdapter().setSelectFilePaths(selectFilePath);

        pickerFragment.getPhotoGridAdapter().setOnItemCheckListener(new OnItemCheckListener() {
            @Override
            public boolean OnItemCheck(int position, Photo photo, final boolean isCheck, int selectedItemCount) {

                int total = selectedItemCount + (isCheck ? -1 : 1);

                btnRight.setEnabled(total > 0);

                if (maxCount <= 1) {
                    List<String> photos = pickerFragment.getPhotoGridAdapter().getSelectedPhotos();
                    if (!photos.contains(photo)) {
                        photos.clear();
                        pickerFragment.getPhotoGridAdapter().notifyDataSetChanged();
                    }
                    btnRight.setEnabled(true);
                    btnRight.setText(R.string.next_step);
                    return true;
                }

                if (total > maxCount) {
                    Toast.makeText(getActivity(), getString(me.iwf.photopicker.R.string.over_max_count_tips, maxCount),
                            LENGTH_LONG).show();
                    return false;
                }

                btnRight.setText(getString(R.string.btn_next_step, total, maxCount));
                return true;
            }
        });
        pickerFragment.setDirListClick(new PhotoPickerFragment.DirItemClickListener() {
            @Override
            public void onSelectDir(PhotoDirBean dirBean) {
                setTitle(dirBean.getName());
            }

            @Override
            public void onDirListShow() {
                tvTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_photodir_up, 0);
                tvTitle.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.widget_margin_small));
            }

            @Override
            public void onDirListHide() {
                tvTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_photodir_down, 0);
                tvTitle.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.widget_margin_small));
            }
        });

        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickerFragment.showDirDialog();
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ArrayList<String> selectedPhotos = pickerFragment.getPhotoGridAdapter().getSelectedPhotoPaths();
                intent.putStringArrayListExtra(KEY_SELECTED_PHOTOS, selectedPhotos);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }



//    /**
//     * Overriding this method allows us to run our exit animation first, then exiting
//     * the activity when it complete.
//     */
//    @Override
//    public void onBackPressed() {
//        if (imagePagerFragment != null && imagePagerFragment.isVisible()) {
//            imagePagerFragment.runExitAnimation(new Runnable() {
//                public void run() {
//                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//                        getSupportFragmentManager().popBackStack();
//                    }
//                }
//            });
//        } else {
//            super.onBackPressed();
//        }
//    }


//    public void addImagePagerFragment(ImagePagerFragment imagePagerFragment) {
//        this.imagePagerFragment = imagePagerFragment;
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(me.iwf.photopicker.R.id.container, this.imagePagerFragment)
//                .addToBackStack(null)
//                .commit();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!menuIsInflated) {
//            getMenuInflater().inflate(me.iwf.photopicker.R.menu.menu_picker, menu);
//            menuDoneItem = menu.findItem(me.iwf.photopicker.R.id.done);
//            menuDoneItem.setEnabled(false);
//            menuIsInflated = true;
//            return true;
//        }
//        return false;
//    }


//    @Override
//    //right button click
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            super.onBackPressed();
//            return true;
//        }
//
//        if (item.getItemId() == me.iwf.photopicker.R.id.done) {
//            Intent intent = new Intent();
//            ArrayList<String> selectedPhotos = pickerFragment.getPhotoGridAdapter().getSelectedPhotoPaths();
//            intent.putStringArrayListExtra(KEY_SELECTED_PHOTOS, selectedPhotos);
//            setResult(RESULT_OK, intent);
//            finish();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public PhotoPickerActivity getActivity() {
        return this;
    }

    public boolean isShowGif() {
        return showGif;
    }

    public void setShowGif(boolean showGif) {
        this.showGif = showGif;
    }


}
