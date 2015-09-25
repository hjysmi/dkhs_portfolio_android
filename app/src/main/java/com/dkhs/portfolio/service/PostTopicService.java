package com.dkhs.portfolio.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.TextUtils;
import android.util.Log;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CommentBean;
import com.dkhs.portfolio.bean.DraftBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.bean.UploadImageBean;
import com.dkhs.portfolio.engine.DraftEngine;
import com.dkhs.portfolio.engine.StatusEngineImpl;
import com.dkhs.portfolio.engine.UploadImageEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ErrorBundle;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.PostTopComletedEvent;
import com.dkhs.portfolio.ui.eventbus.SendTopicEvent;
import com.dkhs.portfolio.utils.PromptManager;
import com.mingle.autolist.AutoData;

import org.parceler.Parcels;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zjz on 2015/8/4.
 */
public class PostTopicService extends IntentService {
    private static final String SERVICE_NAME = PostTopicService.class.getName();
    private static final String TAG = "PostTopicService";

    public static boolean hasFailSend;

    private NotificationManager notificationManager;
    private Builder notification;
    private PowerManager.WakeLock wakeLock;


    private static final int UPLOAD_NOTIFICATION_ID = 1722;
    private static final int UPLOAD_NOTIFICATION_ID_DONE = 1822;
    public static String NAMESPACE = "com.dkhs";
    private static final String ACTION_UPLOAD_SUFFIX = ".posttopicservice.action.upload";
    protected static final String PARAM_TOPICBEAN = "topic_bean";

    private static AtomicInteger mOpenCounter = new AtomicInteger();


    public static String getActionUpload() {
        return NAMESPACE + ACTION_UPLOAD_SUFFIX;
    }

    public static void startPost(Context context, final DraftBean postStatusBean) {


        final Intent intent = new Intent(context, PostTopicService.class);

        intent.setAction(getActionUpload());
        if (postStatusBean.getId() < 0) {
            postStatusBean.setId(mOpenCounter.incrementAndGet());
        }
        intent.putExtra(PARAM_TOPICBEAN, Parcels.wrap(postStatusBean));

        context.startService(intent);
    }


    public PostTopicService() {
        super(SERVICE_NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification = new NotificationCompat.Builder(this);
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (getActionUpload().equals(action)) {
                DraftBean statusBean = Parcels.unwrap(intent.getExtras().getParcelable(PARAM_TOPICBEAN));

                wakeLock.acquire();
                try {
                    createNotification();
                    postTopic(statusBean);
                } catch (Exception exception) {
                    updateNotificationError(statusBean);
                } finally {
                    wakeLock.release();
                }
            }
        }
    }


    private void postTopic(final DraftBean statusBean) {

        if (statusBean.getPhotoList().size() > 0) {
            UploadImageEngine uploadImageEngine = new UploadImageEngine(statusBean.getUploadMap());

            uploadImageEngine.setUploadListener(new UploadImageEngine.UploadImageListener() {
                @Override
                public void onFailure(String errorMsg) {
                    statusBean.setFailReason(errorMsg);
                    requestError(statusBean);
                }

                @Override
                public void onSuccess() {

                    StringBuilder mediaIDs = new StringBuilder();
                    for (String path : statusBean.getPhotoList()) {
                        mediaIDs.append(statusBean.getUploadMap().get(path).serverId);
                        mediaIDs.append(",");
                    }
                    String ids = mediaIDs.substring(0, mediaIDs.length() - 1);
                    StatusEngineImpl.postStatus(statusBean.getSimpleTitle(), statusBean.getSimpleContent(), statusBean.getStatusId(), null, 0, 0, ids, new PostTopicListener(statusBean));


                }
            });
            uploadImageEngine.setPhotoList(statusBean.getPhotoList());
            //上传图片
//            saveBitmapAndUpload(statusBean);
//            StatusEngineImpl.uploadImage(new File(statusBean.getImageFilepath()), new UploadListener(statusBean));
        } else {
            StatusEngineImpl.postStatus(statusBean.getSimpleTitle(), statusBean.getSimpleContent(), statusBean.getStatusId(), null, 0, 0, "", new PostTopicListener(statusBean));

        }

    }


//    private void saveBitmapAndUpload(final String filePath) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Bitmap imageBitmap = UIUtils.getLocaleimage(statusBean.getImageFilepath());
//                    File f = new File(statusBean.getImageFilepath());
//                    if (f.exists()) {
//                        f.delete();
//                    }
//                    FileOutputStream out = new FileOutputStream(f);
//                    Bitmap bitmap = UIUtils.loadBitmap(imageBitmap, statusBean.getImageFilepath());
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
//                    out.flush();
//                    out.close();
//                    StatusEngineImpl.uploadImage(new File(statusBean.getImageFilepath()), new UploadListener(statusBean));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }


    private class UploadListener extends ParseHttpListener<UploadImageBean> {

        private DraftBean mStatusBean;

        public UploadListener(DraftBean statusBean) {
            this.mStatusBean = statusBean;
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
            if (null != entity && null != mStatusBean) {
                // 图片上传完毕继续发表主题
//                PromptManager.showToast("图片上传成功，发表话题");
                StatusEngineImpl.postStatus(mStatusBean.getSimpleTitle(), mStatusBean.getSimpleContent(), mStatusBean.getStatusId(), null, 0, 0, entity.getId(), new PostTopicListener(mStatusBean));
//                StatusEngineImpl.postStatus(mStatusBean.getSimpleTitle(), mStatusBean.getSimpleContent(), null, null, 0, 0, entity.getId(), new PostTopicListener(mStatusBean));

            }
        }


        @Override
        public void onFailure(int errCode, String errMsg) {

            if (errCode == 777) {
                super.onFailure(errCode, errMsg);
            } else {
                Log.e(TAG, "UploadListener onFailure msg :" + errMsg);
                mStatusBean.setFailReason(errMsg);
                requestError(mStatusBean);

            }
        }


        @Override
        public void onFailure(ErrorBundle errorBundle) {
//            super.onFailure(errorBundle);

            mStatusBean.setFailReason(errorBundle.getErrorMessage());
            requestError(mStatusBean);
            Log.e(TAG, "UploadListener ErrorBundle msg :" + errorBundle.getErrorMessage());
        }

    }


    private class PostTopicListener extends ParseHttpListener<TopicsBean> {

        private DraftBean mStatusBean;

        public PostTopicListener(DraftBean statusBean) {
            this.mStatusBean = statusBean;
        }

        @Override
        protected TopicsBean parseDateTask(String jsonData) {
            if (TextUtils.isEmpty(jsonData)) {
                return null;
            }
            try {
                TopicsBean entity = DataParse.parseObjectJson(TopicsBean.class, jsonData);
                return entity;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void afterParseData(TopicsBean entity) {
            PromptManager.closeProgressDialog();
//            UploadImageEngine.uploadMap.clear();
            if (null != entity) {
                if (mStatusBean.getLabel() == 1) {
//                    AddTopicsEvent addTopicsEvent = new AddTopicsEvent(entity);
//                    BusProvider.getInstance().post(addTopicsEvent);
                    entity.appleAction(this, AutoData.Action.Add).post();
                } else {
//                    PromptManager.showSuccessToast(R.string.msg_post_reply_success);
//                    AddCommentEvent addCommentEvent = new AddCommentEvent();
//                    BusProvider.getInstance().post(addCommentEvent);
                    CommentBean.fromTopics(entity).appleAction(this, AutoData.Action.Add).post();
                }
                if (null != mStatusBean) {
                    new DraftEngine(null).delDraft(mStatusBean);
                }
                PostTopicService.this.updateNotificationCompleted();
            }
        }


        @Override
        public void onFailure(int errCode, String errMsg) {
            if (errCode == 777) {
                super.onFailure(errCode, errMsg);
            } else {
                Log.e(TAG, "UploadListener onFailure msg :" + errMsg);
                mStatusBean.setFailReason(errMsg);
                requestError(mStatusBean);

            }
        }


        @Override
        public void onFailure(ErrorBundle errorBundle) {
//            super.onFailure(errorBundle);

            mStatusBean.setFailReason(errorBundle.getErrorMessage());
            requestError(mStatusBean);
            Log.e(TAG, "PostTopicListener ErrorBundle msg :" + errorBundle.getErrorMessage());
        }
    }

    private void requestError(DraftBean statusBean) {
        PostTopicService.this.updateNotificationError(statusBean);
    }


    private void createNotification() {
        notification.setTicker(getString(R.string.msg_topic_sending))
                .setContentIntent(PendingIntent.getBroadcast(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT))
                .setSmallIcon(R.drawable.ic_launcher).setOngoing(true);

        startForeground(UPLOAD_NOTIFICATION_ID, notification.build());
    }


    private void updateNotificationCompleted() {
        stopForeground(false);

        notification
                .setTicker(getString(R.string.msg_topic_send_success))
                .setSmallIcon(R.drawable.ic_launcher).setOngoing(false);

        notificationManager.notify(UPLOAD_NOTIFICATION_ID, notification.build());
        notificationManager.cancel(UPLOAD_NOTIFICATION_ID);

        BusProvider.getInstance().post(new PostTopComletedEvent());

    }


    private void updateNotificationError(DraftBean draftBean) {
        new DraftEngine(null).saveDraft(draftBean);
        stopForeground(false);

        notification.setTicker(getString(R.string.msg_topic_send_fail))
                .setSmallIcon(R.drawable.ic_launcher).setOngoing(false);

        notificationManager.notify(UPLOAD_NOTIFICATION_ID, notification.build());
        notificationManager.cancel(UPLOAD_NOTIFICATION_ID);

        BusProvider.getInstance().post(new SendTopicEvent());

    }

}
