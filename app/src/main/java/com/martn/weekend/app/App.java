package com.martn.weekend.app;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.provider.Settings;

import com.baidu.mapapi.SDKInitializer;
import com.martn.weekend.request.QMusicRequestManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.qmusic.base.BaseApplication;
import com.qmusic.common.BLocationManager;
import com.qmusic.localplugin.PluginManager;
import com.qmusic.uitls.AppUtils;
import com.socks.library.KLog;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;


/**
 * Title: ZeaApp
 * Package: com.martn.zeaapp.app
 * Description: (application类")
 * Date 2014/10/5 14:45
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class App extends BaseApplication {

    private RefWatcher mRefWatcher;
    public static boolean isRefresh;
    public static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        mRefWatcher = LeakCanary.install(this);
//        initPicassoLib();
        //在这里为应用设置异常处理程序，然后我们的程序才能捕获未处理的异常
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(this);

        SDKInitializer.initialize(context());
        QMusicRequestManager.init(context());
        initImageLoader();
        AppUtils.init(context());

    }

    private void initImageLoader() {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context());
        config.threadPriority(5);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(52428800);
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.threadPoolSize(3);
        config.threadPriority(3);
        ImageLoader.getInstance().init(config.build());
    }

//    /**
//     * 初始化图片缓存--下载库 picasso
//     */
//    private void initPicassoLib() {
//
//        Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
//        //2M 1024*1024*2
//        builder.memoryCache(new LruCache(2097152));
//        Picasso.setSingletonInstance(builder.build());
//    }

    public static RefWatcher getRefWatcher(Context context) {

        App application = (App) context.getApplicationContext();
        return application.mRefWatcher;
    }

    public static String getAndroidId() {
        return Settings.Secure.getString(
                context().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 上传参数配置
     */
    private void initUploader() {

//        UploaderConfig.init(getContext());
//        UploaderConfig.debug(0);
//        String str1 = Config.getVersionName();
//        String str2 = UserPreference.getUserKey(getContext());
//        String str3 = UserPreference.getToken(getContext());
//        UploaderConfig.setBooheeUploadParams(str1, str2, str3);
    }



}
