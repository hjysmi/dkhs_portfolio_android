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

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.DraftBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.bean.UploadImageBean;
import com.dkhs.portfolio.engine.DraftEngine;
import com.dkhs.portfolio.engine.StatusEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.eventbus.AddTopicsEvent;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.utils.PromptManager;

import org.parceler.Parcels;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zjz on 2015/8/4.
 */
public class PostTopicService extends IntentService {
    private static final String SERVICE_NAME = PostTopicService.class.getName();
    private static final String TAG = "PostTopicService";

    private NotificationManager notificationManager;
    private Builder notification;
    private PowerManager.WakeLock wakeLock;
    //    private UploadNotificationConfig notificationConfig;
    private int lastPublishedProgress;


    private static final int UPLOAD_NOTIFICATION_ID = 1722;
    private static final int UPLOAD_NOTIFICATION_ID_DONE = 1822; 
    public static String NAMESPACE = "com.dkhs";
    private static final String ACTION_UPLOAD_SUFFIX = ".posttopicservice.action.upload";
    protected static final String PARAM_TOPICBEAN = "topic_bean";

    private static AtomicInteger mOpenCounter = new AtomicInteger();


    public static String getActionUpload() {
        return NAMESPACE + ACTION_UPLOAD_SUFFIX;
    }

//    public static String getActionBroadcast() {
//        return NAMESPACE + BROADCAST_ACTION_SUFFIX;
//    }

    public static void startUpload(Context context, final DraftBean postStatusBean) {


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
//
            if (getActionUpload().equals(action)) {
                DraftBean statusBean = Parcels.unwrap(intent.getExtras().getParcelable(PARAM_TOPICBEAN));
//                notificationConfig = intent.getParcelableExtra(PARAM_NOTIFICATION_CONFIG);
//                final String uploadId = intent.getStringExtra(PARAM_ID);
//                final String url = intent.getStringExtra(PARAM_URL);
//                final String method = intent.getStringExtra(PARAM_METHOD);
//                final ArrayList<FileToUpload> files = intent.getParcelableArrayListExtra(PARAM_FILES);
//                final ArrayList<NameValue> headers = intent.getParcelableArrayListExtra(PARAM_REQUEST_HEADERS);
//                final ArrayList<NameValue> parameters = intent.getParcelableArrayListExtra(PARAM_REQUEST_PARAMETERS);
//
//                lastPublishedProgress = 0;
                wakeLock.acquire();
                try {
                    createNotification();
                    postTopic(statusBean);
                } catch (Exception exception) {
                    broadcastError(statusBean.getId() + "", exception);
                } finally {
                    wakeLock.release();
                }
            }
        }
    }


    private void postTopic(DraftBean statusBean) {


//            uploadFiles(uploadId, requestStream, filesToUpload, boundaryBytes);
//
//            final byte[] trailer = getTrailerBytes(boundary);
//            requestStream.write(trailer, 0, trailer.length);
//            final int serverResponseCode = conn.getResponseCode();
//
//            if (serverResponseCode / 100 == 2) {
//                responseStream = conn.getInputStream();
//            } else { // getErrorStream if the response code is not 2xx
//                responseStream = conn.getErrorStream();
//            }
//            final String serverResponseMessage = getResponseBodyAsString(responseStream);

        if (statusBean.getLabel() == 1 && !TextUtils.isEmpty(statusBean.getImageUri())) {
            //上传图片
            StatusEngineImpl.uploadImage(new File(statusBean.getImageUri()), new UploadListener(statusBean));
        } else {
            StatusEngineImpl.postStatus(statusBean.getTitle(), statusBean.getContent(), statusBean.getStatusId(), null, 0, 0, "", new PostTopicListener(statusBean));

        }


    }

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
                StatusEngineImpl.postStatus(mStatusBean.getTitle(), mStatusBean.getContent(), null, null, 0, 0, entity.getId(), new PostTopicListener(mStatusBean));

            }
        }

        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
//            PromptManager.closeProgressDialog();
            PostTopicService.this.updateNotificationError();
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
            if (null != entity) {
                // 图片上传完毕继续发表主题
                if (mStatusBean.getLabel() == 1) {

//                    PromptManager.showSuccessToast(R.string.msg_post_topic_success);
                    AddTopicsEvent addTopicsEvent = new AddTopicsEvent(entity);
                    BusProvider.getInstance().post(addTopicsEvent);
                } else {
//                    PromptManager.showSuccessToast(R.string.msg_post_reply_success);

                }
                if (null != mStatusBean) {
                    new DraftEngine(null).delDraft(mStatusBean);
                }
                PostTopicService.this.updateNotificationCompleted();
            }
        }

        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
//            PromptManager.closeProgressDialog();
            PostTopicService.this.updateNotificationError();
        }
    }


    private void broadcastCompleted(final String uploadId, final int responseCode, final String responseMessage) {

        final String filteredMessage;
        if (responseMessage == null) {
            filteredMessage = "";
        } else {
            filteredMessage = responseMessage;
        }

        if (responseCode >= 200 && responseCode <= 299)
            updateNotificationCompleted();
        else
            updateNotificationError();

//        final Intent intent = new Intent(getActionBroadcast());
//        intent.putExtra(UPLOAD_ID, uploadId);
//        intent.putExtra(STATUS, STATUS_COMPLETED);
//        intent.putExtra(SERVER_RESPONSE_CODE, responseCode);
//        intent.putExtra(SERVER_RESPONSE_MESSAGE, filteredMessage);
//        sendBroadcast(intent);
    }


    /**
     * 是否区分提交话题的ID
     */
    private void broadcastError(final String uploadId, final Exception exception) {

        updateNotificationError();

//        final Intent intent = new Intent(getActionBroadcast());
//        intent.setAction(getActionBroadcast());
//        intent.putExtra(UPLOAD_ID, uploadId);
//        intent.putExtra(STATUS, STATUS_ERROR);
//        intent.putExtra(ERROR_EXCEPTION, exception);
//        sendBroadcast(intent);
    }

    private void createNotification() {
        notification.setContentTitle(getString(R.string.app_name)).setTicker("正在上传...")
                .setContentIntent(PendingIntent.getBroadcast(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT))
                .setSmallIcon(R.drawable.ic_launcher).setProgress(100, 0, true).setOngoing(true);

        startForeground(UPLOAD_NOTIFICATION_ID, notification.build());
    }


    private void updateNotificationCompleted() {
        stopForeground(false);

        notification.setContentTitle(getString(R.string.app_name))
                .setTicker("内容发送成功")
                .setSmallIcon(R.drawable.ic_launcher).setProgress(0, 0, false).setOngoing(false);

        notificationManager.notify(UPLOAD_NOTIFICATION_ID_DONE, notification.build());

    }


    private void updateNotificationError() {
        stopForeground(false);

        notification.setContentTitle(getString(R.string.app_name)).setTicker("内容发送失败")
                .setSmallIcon(R.drawable.ic_launcher).setProgress(0, 0, false).setOngoing(false);

        notificationManager.notify(UPLOAD_NOTIFICATION_ID_DONE, notification.build());
    }

}
