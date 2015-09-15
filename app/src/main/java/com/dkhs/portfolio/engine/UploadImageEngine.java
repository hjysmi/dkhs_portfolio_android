package com.dkhs.portfolio.engine;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.UploadImageBean;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ErrorBundle;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.PostTopicActivity;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by zjz on 2015/9/15.
 */
public class UploadImageEngine {

    public static WeakHashMap<String, String> uploadMap = new WeakHashMap<>();


    public List<String> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<String> photoList) {
        this.photoList = photoList;

        LogUtils.d("setPhotoList");
        isNeedUpload();
    }


    private List<String> photoList;

    public UploadImageListener getUploadListener() {
        return uploadListener;
    }

    public void setUploadListener(UploadImageListener uploadListener) {
        this.uploadListener = uploadListener;
    }

    private UploadImageListener uploadListener;

    public synchronized void saveBitmapAndUpload(final String filePath) {
        LogUtils.d("saveBitmapAndUpload:" + filePath);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String tempFilePath = filePath;
                    Bitmap imageBitmap = UIUtils.getLocaleimage(filePath);
                    File f = getTempFile();
                    if (null == f) {
                        f = new File(filePath);
                    } else if (f.exists()) {
                        f.delete();
                    }
                    FileOutputStream out = new FileOutputStream(f);
                    Bitmap bitmap = UIUtils.loadBitmap(imageBitmap, filePath);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    StatusEngineImpl.uploadImage(f, new UploadListener().setFilePath(filePath));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private File getTempFile() {
        File outputFile = null;
        try {
            File outputDir = PortfolioApplication.getInstance().getCacheDir(); // context being the Activity pointer
            outputFile = File.createTempFile("temp_upload", ".jpg", outputDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputFile;
    }

    public class UploadListener extends ParseHttpListener<UploadImageBean> {


        private String mFilePath;

        public UploadListener setFilePath(String path) {
            this.mFilePath = path;
            return this;
        }

        @Override
        protected UploadImageBean parseDateTask(String jsonData) {
            if (TextUtils.isEmpty(jsonData)) {
                return null;
            }
            try {
                UploadImageBean entity = DataParse.parseObjectJson(UploadImageBean.class, jsonData);
                return entity;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void afterParseData(UploadImageBean entity) {
            if (null == uploadMap) {
                uploadMap = new WeakHashMap<>();
            }
            if (null != entity && !TextUtils.isEmpty(mFilePath)) {
                uploadMap.put(mFilePath, entity.getId());
            }
            if (!isNeedUpload()) {
                LogUtils.d("All photo upload finish");
            }
//            if (null != entity && null != mStatusBean) {
            // 图片上传完毕继续发表主题
//                PromptManager.showToast("图片上传成功，发表话题");
//                StatusEngineImpl.postStatus(mStatusBean.getSimpleTitle(), mStatusBean.getSimpleContent(), mStatusBean.getStatusId(), null, 0, 0, entity.getId(), new PostTopicListener(mStatusBean));
//                StatusEngineImpl.postStatus(mStatusBean.getSimpleTitle(), mStatusBean.getSimpleContent(), null, null, 0, 0, entity.getId(), new PostTopicListener(mStatusBean));

//            }
        }


        @Override
        public void onFailure(int errCode, String errMsg) {

            if (errCode == 777) {
                super.onFailure(errCode, errMsg);
            } else if (null != uploadListener) {
                uploadListener.onFailure(errMsg);
            }


        }


        @Override
        public void onFailure(ErrorBundle errorBundle) {

            if (null != uploadListener) {
                uploadListener.onFailure(errorBundle.getErrorMessage());
            }
        }

    }

    private boolean isNeedUpload() {
        if (null != photoList) {
            for (String path : photoList) {
                if (!TextUtils.isEmpty(path)) {
                    if (!path.equals(PostTopicActivity.ADD_PICTURE) && uploadMap.get(path) == null) {
                        saveBitmapAndUpload(path);
                        return true;
                    }
                }
            }


            if (null != uploadListener)

            {
                uploadListener.onSuccess();
            }

        }
        return false;
    }


    public interface UploadImageListener {
        void onFailure(String errorMsg);

        void onSuccess();
    }


}
