package com.ss.popupdemo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;

/**
 * 用于检测系统弹出框，例如dialog，或者状态栏下拉，或者压入后台， （注意这里无法区分具体显示的窗口是dialog 还是 状态栏）
 * Created by song on 2018/4/17.
 */

public class CheckWindowManager implements WindowCallBack.WindowsFocusListener {

    private static CheckWindowManager mInstance;

    public static CheckWindowManager getInstance() {
        synchronized (CheckWindowManager.class) {
            if (mInstance == null) {
                mInstance = new CheckWindowManager();
            }

            return mInstance;
        }
    }

    private CheckWindowManager() {
    }


    public interface OnAppStatusListener {
        public void hasPopupWindow();

        public void onAppNormal();

        public void onInBackground();
    }

    private static final String TAG = "CheckWindow";
    private int windowCount;
    private boolean initialized = false;
    private boolean isPupopWindows = false;
    private boolean isInBackground = false;
    private Context context;

    private OnAppStatusListener mAppStatusListener;


    // 监听window焦点，信息返回
    private static final int HAS_POPUP_WINDOW = 14;
    private static final int APP_NORMAL = 15;
    private static final int APP_IN_BACKGROUND = 16;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {

                case HAS_POPUP_WINDOW:
                    isPupopWindows = true;
                    if (mAppStatusListener != null) {
                        mAppStatusListener.hasPopupWindow();
                    }
                    Logd("popup dialog or pull down notification bar");
                    break;
                case APP_NORMAL:
                    isInBackground = false;
                    isPupopWindows = false;
                    if (mAppStatusListener != null) {
                        mAppStatusListener.onAppNormal();
                    }
                    Logd("return to normal");
                    break;
                case APP_IN_BACKGROUND:
                    isInBackground = true;
                    if (mAppStatusListener != null) {
                        mAppStatusListener.onInBackground();
                    }
                    Logd("app running in background");
                    break;

                default:
                    break;
            }
        }

    };

    private void Logd(String msg){
        Log.d(TAG, "appStatusManager-" + msg);
    }

    public boolean isPupopWindows() {
        return isPupopWindows;
    }

    public boolean isInBackground() {
        return isInBackground;
    }



    public void init(Context context) {
        if (!initialized) {
            this.context = context;
            ((Application) context).registerActivityLifecycleCallbacks(mALCallbacks);
            initialized = true;
            windowCount = 0;
        }
    }

    public  void release() {
        if (initialized) {
            ((Application) context).unregisterActivityLifecycleCallbacks(mALCallbacks);
            isPupopWindows = false;
            isInBackground = false;
            initialized = false;
            mAppStatusListener = null;
            this.context = null;
            windowCount = 0;
        }
    }


    public void setOnAppStatusListener(OnAppStatusListener mAppStatusListener) {
        this.mAppStatusListener = mAppStatusListener;
    }

    /**
     * 该接口只能在Android4.0以上的版本中才能够使用，因此该SDK暂时只支持Android4.0（包括）以上的版本。
     * 该接口主要用来监控Activity的生命周期。 创建该接口实例，并实现该接口。
     */
    private Application.ActivityLifecycleCallbacks mALCallbacks = new Application.ActivityLifecycleCallbacks() {

        @Override
        public void onActivityStopped(Activity activity) {
            Logd( "onActivityStopped===>>" +activity.getLocalClassName() );
            windowCount--;

            if (windowCount < 1) {
                Message m = Message.obtain();
                m.what = APP_IN_BACKGROUND;
                mHandler.sendMessage(m);
                isInBackground = true;
            }
            /**
             * 因为activity 切换的时候也会有焦点切换，失去获取，所以这里在activity stop的时候，把msg 消息清楚一下
             */
            mHandler.removeMessages(HAS_POPUP_WINDOW);
            mHandler.removeMessages(APP_NORMAL);
        }

        @Override
        public void onActivityStarted(final Activity activity) {
            windowCount++;
            Logd("onActivityStarted====>>"+activity.getLocalClassName()+"/====="+windowCount);
            addWindowCallback(activity);
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Logd("onActivityResumed===>>" + activity.getLocalClassName());

        }

        @Override
        public void onActivityPaused(Activity activity) {
            Logd("onActivityPaused===>>" + activity.getLocalClassName());

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Logd( "onActivityDestroyed===>>" + activity.getLocalClassName());
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }
    };


    private void addWindowCallback(Activity activity) {
        Window mWindow = activity.getWindow();
        WindowCallBack b = new WindowCallBack(activity);
        b.setOnWindowFocusListener(CheckWindowManager.this);
        mWindow.setCallback(b);
    }

    @Override
    public void onWindowFocusChanged(String tag, boolean hasFocus) {
        Logd( "onWindowFocusChanged===>>" +tag);
        Message m = Message.obtain();
        if (!hasFocus && windowCount < 1/*GraphicCommonUtils.isBackground(mContext)*/) {
            return;
        } else if (hasFocus) {
            m.what = APP_NORMAL;
        } else {
            m.what = HAS_POPUP_WINDOW;
        }
        /**
         * 因为activity 切换的时候也会有焦点切换，失去获取，所以这里延迟一下，（用于等待清除操作，有就清除， 没有到计时后就发送）
         */
        mHandler.sendMessageDelayed(m, 700);
    }
}
