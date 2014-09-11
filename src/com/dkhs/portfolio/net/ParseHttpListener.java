package com.dkhs.portfolio.net;

import org.json.JSONObject;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.lidroid.xutils.util.LogUtils;

/**
 * @ClassName: ParseHttpListener
 * @Description: 用于实现子线程解析服务器返回数据，返回数据到主程序
 * @author zhoujunzhou
 * @date 2014-5-6 上午10:20:51
 * @version 1.0
 */
public abstract class ParseHttpListener<T> extends BasicHttpListener {

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

    // @Override
    // public final void onHttpFailure(int errCode, String errMsg) {
    // // MSLog.d("Error code :", errCode, ",message : ", errMsg);
    // LogUtils.d("Error code :" + errCode + ",message : " + errMsg);
    // onFailure(errCode, errMsg);
    // }
    //
    // @Override
    // public final void onHttpFailure(int errCode, Throwable err) {
    // LogUtils.d("Error code :" + errCode + ",message : " + err.toString());
    // // MSLog.d("Error code :", errCode, ",message : ", err);
    // onFailure(errCode, err.getMessage());
    // }

    // /**
    // *
    // *
    // * @Title: onSuccess
    // * @Description:
    // * errorCode已验证成功，不需要在验证，可从{@code result} 中直接获取数据<br>
    // * errorCode验证不成功处理逻辑在 {@link #onFailure(int, String)}中处理
    // * @param @param jsonObject 设定文件
    // * @return void 返回类型
    // */
    // public abstract void onSuccess(JSONObject result);

    /**
     * 
     * 
     * @Title: onFailure
     * @Description: 网络错误处理，
     * @param @param errCode 错误编码，具体查看 {@link Network.HttpCode}
     * @param @param errMsg 错误信息
     * @return void 返回类型
     */
    public void onFailure(int errCode, String errMsg) {
        // GTGUtils.showTip(HttpCode.getCodeResId(errCode));
        // LogUtils.e("Error code :" + errCode + ",message : " + err.toString());
        super.onFailure(errCode, errMsg);
    }

    public static final int MSG_PARSEDATE = 10;
    public static final int MSG_UPDATEUI = 20;
    private volatile Looper mServiceLooper;
    private volatile ServiceHandler mServiceHandler;
    private volatile ServiceHandler mMainHandler;

    // private JSONObject jsonDate;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            System.out.println("++++++++++++++++++++Handler message what:" + msg.what);
            switch (msg.what) {
                case MSG_PARSEDATE:
                    String jsonObject = (String) msg.obj;
                    notifyDateParse(parseDateTask(jsonObject));
                    stopSelf();
                    break;
                case MSG_UPDATEUI:
                    T obj = (T) msg.obj;
                    afterParseData(obj);
                    break;

                default:
                    break;
            }

        }
    }

    public void beginParseDate(String jsonObject) {
        mServiceHandler.obtainMessage(MSG_PARSEDATE, jsonObject).sendToTarget();
    }

    private void notifyDateParse(Object object) {
        System.out.println("============notifyDateParse===========");
        Message msg = mMainHandler.obtainMessage();
        msg.what = MSG_UPDATEUI;
        msg.obj = object;
        mMainHandler.sendMessage(msg);
    }

    private void stopSelf() {
        mServiceLooper.quit();
    }

    // 耗时操作，解析数据
    /**
     * 
     * 
     * @Title: parseDateTask
     * @Description:
     * 
     * 该方法在子线程中执行，用于处理耗时操作，如解析从服务器中返回的数据，需要返回解析之后的数据对象
     * 在{@link #afterParseData(Object object)}取得返回的数据对象，
     * @param jsonData :服务器返回errorCode已验证成功的Json对象
     * @return Object :解析后的数据对象
     */
    protected abstract T parseDateTask(String jsonData);

    // ui操作
    /**
     * 
     * 
     * @Title: afterParseData
     * @Description:
     * 该方法在主线程中执行，用于UI更新之类的操作
     * 在{@link #parseDateTask(JSONObject jsonData)}之后执行
     * @param object :{@link #parseDateTask(JSONObject jsonData)}中解析之后返回的数据对象
     * @return void
     */
    protected abstract void afterParseData(T object);

}
