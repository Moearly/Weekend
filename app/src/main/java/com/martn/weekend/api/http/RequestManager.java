package com.martn.weekend.api.http;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.qmusic.base.BaseApplication;
import com.socks.library.KLog;

/**
 * Title: LeiFuUIMainDemo
 * Package: com.martn.leifuuimaindemo.one.http
 * Description: ("使用volley发出请求的管理")
 * Date 2015/7/30 17:17
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class RequestManager {

    private static OkHttpStack okHttpStack = new OkHttpStack();
    /**
     * Volley中特有的请求队列
     */
    public static RequestQueue mRequestQueue = Volley.newRequestQueue(BaseApplication.context(),okHttpStack);//, new OkHttpStack()



    public static void addRequest(Request<?> request, Object obj) {

        String cacheKey = request.getCacheKey();
        KLog.w("Volley/URL", cacheKey);
        if (obj != null)
            request.setTag(obj);
        //重试机制处理---默认的--也可以自定义得
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(15000, 0, 1.0F);
        request.setRetryPolicy(retryPolicy);
        mRequestQueue.add(request);
    }

    /**
     * 取消请求队列中的所以请求
     * @param obj
     */
    public static void cancelAll(Object obj) {
        if (obj != null)
            mRequestQueue.cancelAll(obj);
    }
}