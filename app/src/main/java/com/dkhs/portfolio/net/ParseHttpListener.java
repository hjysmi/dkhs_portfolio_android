package com.dkhs.portfolio.net;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.dkhs.portfolio.security.SecurityUtils;
import com.dkhs.portfolio.utils.PromptManager;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author zhoujunzhou
 * @version 1.0
 * @ClassName: ParseHttpListener
 * @Description: 用于实现子线程解析服务器返回数据，返回数据到主程序
 * @date 2014-09-12 上午10:20:51
 */
public abstract class ParseHttpListener<T> extends BasicHttpListener {

    private Context mContext;
    private String msg;
    private boolean isHideDialog = true;

    public ParseHttpListener setLoadingDialog(Context context, int msgid) {
        this.mContext = context;
        this.msg = mContext.getString(msgid);
        return this;
    }

    public ParseHttpListener setLoadingDialog(Context context, int msgid, boolean isCancelable) {
        setLoadingDialog(context, context.getString(msgid), isCancelable);
        return this;
    }

    public ParseHttpListener setLoadingDialog(Context context, String msg) {
        this.mContext = context;
        this.msg = msg;
        return this;
    }

    public ParseHttpListener setLoadingDialog(Context context, String msg, boolean isCancelable) {
        this.mContext = context;
        this.msg = msg;
        this.isHideDialog = isCancelable;
        return this;
    }

    public ParseHttpListener setLoadingDialog(Context context) {
        this.mContext = context;
        this.msg = "";// mActivity.getString(R.string.loading);

        return this;
    }

    public ParseHttpListener setLoadingDialog(Context context, boolean isCancelable) {
        this.mContext = context;
        this.msg = "";// mActivity.getString(R.string.loading);
        this.isHideDialog = isCancelable;
        return this;
    }

    public ParseHttpListener cancelLoadingDialog() {
        this.mContext = null;
        this.msg = "";// mActivity.getString(R.string.loading);
        PromptManager.closeProgressDialog();
        return this;
    }

    @Override
    public void beforeRequest() {
        if (null != mContext) {
            // if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            // // On UI thread.
            // LogUtils.d("beforeRequest PromptManager.showProgressDialog");
            PromptManager.showProgressDialog(mContext, msg, isHideDialog);
            // } else {
            // // Not on UI thread.
            // LogUtils.d("beforeRequest Not on UI thread");
            // }
        }
    }

    @Override
    public void requestCallBack() {
        if (null != mContext) {
            // if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            // // On UI thread.
            LogUtils.d("requestCallBack PromptManager.closeProgressDialog");
            PromptManager.closeProgressDialog();
            // } else {
            // LogUtils.d("requestCallBack Not on UI thread");
            // }
        }

    }

    public ParseHttpListener() {
        HandlerThread thread = new HandlerThread("HttpListenerThread");
        thread.start();
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
        mMainHandler = new ServiceHandler(Looper.getMainLooper());
    }

    @Override
    public void onSuccess(String jsonObject) {

        beginParseDate(jsonObject);

    }


    /**
     * @param @param errCode 错误编码，具体查看
     * @param @param errMsg 错误信息
     * @return void 返回类型
     * @Title: onFailure
     * @Description: 网络错误处理，
     */
    public void onFailure(int errCode, String errMsg) {
        // GTGUtils.showTip(HttpCode.getCodeResId(errCode));
        // LogUtils.e("Error code :" + errCode + ",message : " + err.toString());
        super.onFailure(errCode, errMsg);
    }

    public static final int MSG_PARSEDATE = 10;
    public static final int MSG_UPDATEUI = 20;
    public static final int MSG_ERROR = 30;
    private volatile Looper mServiceLooper;
    private volatile ServiceHandler mServiceHandler;
    private volatile ServiceHandler mMainHandler;
    protected boolean isEncry;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_PARSEDATE:
                    String jsonObject = (String) msg.obj;
                    if (isEncry) {
//                        handleErrorMessage(jsonObject);
                        jsonObject = SecurityUtils.encryptResponeJsonData(jsonObject);
                        if (handleErrorMessage(jsonObject)) {
                            notifyError(jsonObject);
                        } else {
                            notifyDateParse(parseDateTask(jsonObject));

                        }
                    } else {

                        notifyDateParse(parseDateTask(jsonObject));
                    }

                    stopSelf();
                    break;
                case MSG_UPDATEUI:
                    T obj = (T) msg.obj;
                    afterParseData(obj);


////                    jsonObject = SecurityUtils.encryptResponeJsonData((String) obj);
//                    if (handleErrorMessage((String) obj)) {
//                        notifyError((String) obj);
//                    } else {
//                        afterParseData(obj);
//
//                    }
                    break;

                case MSG_ERROR:
                    String error = (String) msg.obj;
                    onFailure(777, error);
                    break;

                default:
                    break;
            }

        }
    }


    private boolean handleErrorMessage(String result) {

        try {

            return TextUtils.isEmpty(result) || ErrorBundle.isContainError(result);

        } catch (JSONException e) {

            e.printStackTrace();
        }
        return false;

    }


    public ParseHttpListener openEncry() {
        this.isEncry = true;
        return this;
    }

    public void beginParseDate(String jsonObject) {
        HandlerThread thread = new HandlerThread("HttpListenerThread");
        thread.start();
        mServiceLooper = thread.getLooper();
        mServiceHandler.obtainMessage(MSG_PARSEDATE, jsonObject).sendToTarget();

    }

    private void notifyDateParse(Object object) {
        Message msg = mMainHandler.obtainMessage();
        msg.what = MSG_UPDATEUI;
        msg.obj = object;
        mMainHandler.sendMessage(msg);
    }

    private void notifyError(Object object) {
        Message msg = mMainHandler.obtainMessage();
        msg.what = MSG_ERROR;
        msg.obj = object;
        mMainHandler.sendMessage(msg);
    }

    private void stopSelf() {
        mServiceLooper.quit();

    }

    // 耗时操作，解析数据

    /**
     * @param jsonData :服务器返回errorCode已验证成功的Json对象
     * @return Object :解析后的数据对象
     * @Title: parseDateTask
     * @Description: 该方法在子线程中执行，用于处理耗时操作，如解析从服务器中返回的数据，需要返回解析之后的数据对象
     * 在{@link #afterParseData(Object object)}取得返回的数据对象，
     */
    protected abstract T parseDateTask(String jsonData);

    // ui操作

    /**
     * @param object  中解析之后返回的数据对象
     * @return void
     * @Title: afterParseData
     * 该方法在主线程中执行，用于UI更新之类的操作
     * 在{@link #parseDateTask(JSONObject jsonData)}之后执行
     */
    protected abstract void afterParseData(T object);

}
