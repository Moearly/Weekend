package com.qmusic.base;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.os.Handler;

import com.qmusic.common.BLocationManager;
import com.qmusic.localplugin.PluginManager;
import com.qmusic.uitls.AppUtils;
import com.socks.library.KLog;
import com.umeng.analytics.MobclickAgent;


@SuppressLint("InflateParams")
public class BaseApplication extends Application{
    public static boolean DEBUG;
    protected static Context context;
    protected static Resources resource;
    private boolean isMainOpened = false;
    protected static Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        resource = context.getResources();


        ApplicationInfo appInfo = context().getApplicationInfo();
        if ((appInfo.flags & 2) == 0) {
            DEBUG = false;
        } else {
            DEBUG = true;
        }
        ActivityManager.RunningAppProcessInfo appProcessInfo = AppUtils.getCurProcess(context());
        if (appProcessInfo == null) {
            KLog.e("Should never get here");
        } else if (appInfo.packageName.equals(appProcessInfo.processName)) {
            handler = new Handler();
            PluginManager.init(context());
            BLocationManager.init(context());
            MobclickAgent.setDebugMode(DEBUG);
            MobclickAgent.updateOnlineConfig(context());
            MobclickAgent.setSessionContinueMillis(60000);
        } else {
            KLog.w("In remote process");
        }
    }

    public static synchronized BaseApplication context() {
        return (BaseApplication) context;
    }

    public static Resources resources() {
        return resource;
    }

    public boolean getIsMainOpened() {

        return isMainOpened;
    }

    public void setIsMainActivityOpened(boolean opened) {

        isMainOpened = opened;
    }

    public static final void post(Runnable runnable) {
        handler.post(runnable);
    }

    public static final void removePost(Runnable runnable) {
        handler.removeCallbacks(runnable);
    }

    public static final void postDelayed(Runnable runnable, long delayMillis) {
        handler.postDelayed(runnable, delayMillis);
    }
}
