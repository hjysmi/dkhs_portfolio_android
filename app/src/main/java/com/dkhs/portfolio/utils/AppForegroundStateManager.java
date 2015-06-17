package com.dkhs.portfolio.utils;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;

/**
 * This class is responsible for tracking all currently open activities.
 * By doing so this class can detect when the application is in the foreground
 * and when it is running in the background.
 * 
 * 
 */
public class AppForegroundStateManager {
    private static final String TAG = AppForegroundStateManager.class.getSimpleName();
    private static final int MESSAGE_NOTIFY_LISTENERS = 1;
    public static final long APP_CLOSED_VALIDATION_TIME_IN_MS = 30 * DateUtils.SECOND_IN_MILLIS; // 30 Seconds
    private Reference<Activity> mForegroundActivity;
    private Set<OnAppForegroundStateChangeListener> mListeners = new HashSet<OnAppForegroundStateChangeListener>();
    private AppForegroundState mAppForegroundState = AppForegroundState.NOT_IN_FOREGROUND;
    private NotifyListenersHandler mHandler;

    // Make this class a thread safe singleton
    private static class SingletonHolder {
        public static final AppForegroundStateManager INSTANCE = new AppForegroundStateManager();
    }

    public static AppForegroundStateManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private AppForegroundStateManager() {
        // Create the handler on the main thread
        mHandler = new NotifyListenersHandler(Looper.getMainLooper());
    }

    public enum AppForegroundState {
        IN_FOREGROUND, NOT_IN_FOREGROUND
    }

    public interface OnAppForegroundStateChangeListener {
        /** Called when the foreground state of the app changes */
        public void onAppForegroundStateChange(AppForegroundState newState);
    }

    /** An activity should call this when it becomes visible */
    public void onActivityVisible(Activity activity) {
        if (mForegroundActivity != null)
            mForegroundActivity.clear();
        mForegroundActivity = new WeakReference<Activity>(activity);
        determineAppForegroundState();
    }

    /** An activity should call this when it is no longer visible */
    public void onActivityNotVisible(Activity activity) {
        /*
         * The foreground activity may have been replaced with a new foreground activity in our app.
         * So only clear the foregroundActivity if the new activity matches the foreground activity.
         */
        if (mForegroundActivity != null) {
            Activity ref = mForegroundActivity.get();

            if (activity == ref) {
                // This is the activity that is going away, clear the reference
                mForegroundActivity.clear();
                mForegroundActivity = null;
            }
        }

        determineAppForegroundState();
    }

    /** Use to determine if this app is in the foreground */
    public Boolean isAppInForeground() {
        return mAppForegroundState == AppForegroundState.IN_FOREGROUND;
    }

    /**
     * Call to determine the current state, update the tracking global, and notify subscribers if the state has changed.
     */
    private void determineAppForegroundState() {
        /* Get the current state */
        AppForegroundState oldState = mAppForegroundState;

        /* Determine what the new state should be */
        final boolean isInForeground = mForegroundActivity != null && mForegroundActivity.get() != null;
        mAppForegroundState = isInForeground ? AppForegroundState.IN_FOREGROUND : AppForegroundState.NOT_IN_FOREGROUND;

        /* If the new state is different then the old state the notify subscribers of the state change */
        if (mAppForegroundState != oldState) {
            validateThenNotifyListeners();
        }
    }

    /**
     * Add a listener to be notified of app foreground state change events.
     * 
     * @param listener
     */
    public void addListener(@NonNull OnAppForegroundStateChangeListener listener) {
        mListeners.add(listener);
    }

    /**
     * Remove a listener from being notified of app foreground state change events.
     * 
     * @param listener
     */
    public void removeListener(OnAppForegroundStateChangeListener listener) {
        mListeners.remove(listener);
    }

    /** Notify all listeners the app foreground state has changed */
    private void notifyListeners(AppForegroundState newState) {
        android.util.Log.i(TAG, "Notifying subscribers that app just entered state: " + newState);

        for (OnAppForegroundStateChangeListener listener : mListeners) {
            listener.onAppForegroundStateChange(newState);
        }
    }

    /**
     * This method will notify subscribes that the foreground state has changed when and if appropriate. <br>
     * <br>
     * We do not want to just notify listeners right away when the app enters of leaves the foreground. When changing
     * orientations or opening and
     * closing the app quickly we briefly pass through a NOT_IN_FOREGROUND state that must be ignored. To accomplish
     * this a delayed message will be
     * Sent when we detect a change. We will not notify that a foreground change happened until the delay time has been
     * reached. If a second
     * foreground change is detected during the delay period then the notification will be canceled.
     */
    private void validateThenNotifyListeners() {
        // If the app has any pending notifications then throw out the event as the state change has failed validation
        if (mHandler.hasMessages(MESSAGE_NOTIFY_LISTENERS)) {
            android.util.Log.v(TAG, "Validation Failed: Throwing out app foreground state change notification");
            mHandler.removeMessages(MESSAGE_NOTIFY_LISTENERS);
        } else {
            if (mAppForegroundState == AppForegroundState.IN_FOREGROUND) {
                // If the app entered the foreground then notify listeners right away; there is no validation time for
                // this
                mHandler.sendEmptyMessage(MESSAGE_NOTIFY_LISTENERS);
            } else {
                // We need to validate that the app entered the background. A delay is used to allow for time when the
                // application went into the
                // background but we do not want to consider the app being backgrounded such as for in app purchasing
                // flow and full screen ads.
                mHandler.sendEmptyMessageDelayed(MESSAGE_NOTIFY_LISTENERS, APP_CLOSED_VALIDATION_TIME_IN_MS);
            }
        }
    }

    private class NotifyListenersHandler extends Handler {
        private NotifyListenersHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message inputMessage) {
            switch (inputMessage.what) {
            // The decoding is done
                case MESSAGE_NOTIFY_LISTENERS:
                    /* Notify subscribers of the state change */
                    android.util.Log.v(TAG, "App just changed foreground state to: " + mAppForegroundState);
                    notifyListeners(mAppForegroundState);
                    break;
                default:
                    super.handleMessage(inputMessage);
            }
        }
    }

    /******** Activity Notification **********/
    // @Override
    // protected void onStart() {
    // super.onStart();
    // AppForegroundStateManager.getInstance().onActivityVisible(this);
    // }
    //
    // @Override
    // protected void onStop() {
    // AppForegroundStateManager.getInstance().onActivityNotVisible(this);
    // super.onStop();
    // }
    /******************/

    /******** Applicatiion Init **********/
    // public class MyApplication extends Application {
    // @Override
    // public void onCreate() {
    // super.onCreate();
    // AppForegroundStateManager.getInstance().addListener(this);
    // }
    //
    // @Override
    // public void onAppForegroundStateChange(AppForegroundStateManager.AppForegroundState newState) {
    // if (AppForegroundStateManager.AppForegroundState.IN_FOREGROUND == newState) {
    // // App just entered the foreground. Do something here!
    // } else {
    // // App just entered the background. Do something here!
    // }
    // }
    // }
    /******************/

}
