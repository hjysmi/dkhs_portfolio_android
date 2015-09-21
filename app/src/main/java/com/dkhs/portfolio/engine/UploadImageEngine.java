package com.dkhs.portfolio.engine;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.PostImageBean;
import com.dkhs.portfolio.bean.UploadImageBean;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ErrorBundle;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.PostTopicActivity;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zjz on 2015/9/15.
 */
public class UploadImageEngine implements Runnable {

    private HashMap<String, PostImageBean> uploadMap = new HashMap<>();

    public UploadImageEngine(HashMap<String, PostImageBean> upMap) {
        if (null != upMap) {
            this.uploadMap = upMap;
        }
    }

    public UploadImageEngine() {
    }

    public List<String> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<String> photoList) {
        this.photoList = photoList;
        setCancelUpload(false);
        isNeedUpload();
    }


    public HashMap<String, PostImageBean> getUploadMap() {
        return uploadMap;
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
        mCurrentUpload = filePath;
        new Thread(this).start();

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

    private String mCurrentUpload;

    @Override
    public void run() {
//        Thread thisThread = Thread.currentThread();
//        while (myThread == thisThread) {
        try {
            String tempFilePath = mCurrentUpload;
            Bitmap imageBitmap = UIUtils.getLocaleimage(mCurrentUpload);
            File f = getTempFile();
            if (null == f) {
                f = new File(mCurrentUpload);
            } else if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            Bitmap bitmap = UIUtils.loadBitmap(imageBitmap, mCurrentUpload);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            StatusEngineImpl.uploadImage(f, new UploadListener().setFilePath(mCurrentUpload));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public class UploadListener extends ParseHttpListener<UploadImageBean> {


        private String mFilePath;

        public UploadListener setFilePath(String path) {
            this.mFilePath = path;
            return this;
        }

        @Override
        protected UploadImageBean parseDateTask(String jsonData) {
            if (isCancelUpload) { //取消上传
                return null;
            }
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
                uploadMap = new HashMap<>();
            }
            if (isCancelUpload) { //取消上传
                return;
            }
            if (null != entity && !TextUtils.isEmpty(mFilePath)) {
                PostImageBean imageBean = new PostImageBean();
                imageBean.filePath = mFilePath;
                imageBean.serverId = entity.getId();
                imageBean.uploadTime = System.currentTimeMillis() / 1000;
                uploadMap.put(mFilePath, imageBean);
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

            if (isCancelUpload) {
                return;
            }
            if (errCode == 777) {
                super.onFailure(errCode, errMsg);
            } else if (null != uploadListener) {
                uploadListener.onFailure(errMsg);
            }


        }


        @Override
        public void onFailure(ErrorBundle errorBundle) {
            if (isCancelUpload) {
                return;
            }
            if (null != uploadListener) {
                uploadListener.onFailure(errorBundle.getErrorMessage());
            }
        }

    }

    private boolean isNeedUpload() {
        if (null != photoList) {
            for (String path : photoList) {
                if (!TextUtils.isEmpty(path)) {
                    PostImageBean imageBean = uploadMap.get(path);
                    boolean isReUploadImage = (null != imageBean &&!TimeUtils.isEnableImageTime(imageBean.uploadTime));

                    boolean isUnupload = (!path.equals(PostTopicActivity.ADD_PICTURE) && imageBean == null);
                    if (isUnupload || isReUploadImage) {
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


    private boolean isCancelUpload;

    //是否取消下载
    public void cancelUpload() {
        setCancelUpload(true);
    }

    private void setCancelUpload(boolean isCancel) {
        this.isCancelUpload = isCancel;
    }

    //线程对象引用
    private Thread myThread;

    public synchronized void stop() {
        if (myThread == null) {
            return;
        }
        Thread moribund = myThread;
        myThread = null;
        moribund.interrupt();
    }

    //开始
    public void start() {
        myThread = new Thread(this, "myThread");
        myThread.start();
    }

    public interface UploadImageListener {
        void onFailure(String errorMsg);

        void onSuccess();
    }


}
