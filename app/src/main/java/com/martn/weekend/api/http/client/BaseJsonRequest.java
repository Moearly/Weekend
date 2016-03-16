package com.martn.weekend.api.http.client;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.martn.weekend.api.http.JsonCallback;
import com.martn.weekend.api.http.JsonParams;
import com.martn.weekend.api.http.RequestManager;

import java.util.HashMap;
import java.util.Map;


/**
 * Title: Juyixia
 * Package: com.lefu.juyixia.api.http.request
 * Description: ("基本的json请求")
 * Date 2015/8/27 17:58
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class BaseJsonRequest extends JsonRequest<String> {
    protected static boolean DEBUG = false;

    public BaseJsonRequest(int method, String url, JsonParams requestBody, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody.toString(), listener, errorListener);
    }

    /**
     * 基础get请求
     * @param url
     * @param params
     * @param context
     */
    public static void get(String url, JsonParams params, JsonCallback jsonCallback, Context context) {
        JsonParams hasParams = putBaseParams(params);
        String hasUrl = getUrlWithQueryString(url, hasParams);
        RequestManager.addRequest(new BaseJsonRequest(Method.GET, hasUrl, null, jsonCallback, jsonCallback), context);
    }

    /**
     * 在url的尾部添加参数
     * @param url
     * @param params
     * @return
     */
    public static String getUrlWithQueryString(String url, JsonParams params) {

        String strParam;
        if (params != null) {
            strParam = params.getEncodedParamString();
            if (url.indexOf("?") != -1) {
                return url + "?" + strParam;
            } else {
                return url + "&" + strParam;
            }
        } else {
            return url;
        }
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        Object response ;
        if (networkResponse == null) {
            response = null;
        } else {
            try {
                //String charset = HttpHeaderParser.parseCharset(networkResponse.headers);
                Cache.Entry entry = HttpHeaderParser.parseCacheHeaders(networkResponse);
                response = Response.success(new String(networkResponse.data, "UTF-8"), entry);
            } catch (Exception e) {
                Cache.Entry entry = HttpHeaderParser.parseCacheHeaders(networkResponse);
                response = Response.success(new String(networkResponse.data),entry);
            }
        }
        return (Response<String>)response;
    }

    @Override
    public Map<String, String> getHeaders()
            throws AuthFailureError {

        HashMap map = new HashMap();
//        String versionName = Config.getVersionName();
//        map.put("app_version", versionName);
//        map.put("app_key", "one");
//        map.put("app_device", "Android");
//        String versionCode = Config.getVersionCode()+"";
//        map.put("os_version", versionCode);
//        String model = Config.getPhoneModel();
//        map.put("phone_model", model);
        map.put("Accept", "application/json");
        map.put("User-Agent", "qmusic_1.0");
        //String channel = Config.getChannel(BaseApplication.context());
//        map.put("channel", channel);
//        String token = UserPreference.getToken(BaseApplication.context());
//        map.put("token", token);
//        String userId = UserPreference.getUserId(BaseApplication.context());
//        map.put("user_key", userId);
        return map;
    }

    /**
     * 添加基础参数
     * @param params
     * @return
     */
    public static JsonParams putBaseParams(JsonParams params) {

        if (params == null)
            params = new JsonParams();
//        String token = UserPreference.getToken(BaseApplication.context());
//        params.put("token", token);
//        String userId = UserPreference.getUserId(BaseApplication.context());
//        params.put("user_id", userId);
////        String versionName = Config.getVersionName();
////        params.put("app_version", versionName);
////        params.put("app_device", "Android");
////        params.put("app_key", "one");
//        String versionCode = Config.getVersionCode()+"";
//        params.put("os_version", versionCode);
//        String model = Config.getPhoneModel();
//        params.put("phone_model", model);
//        String channel = Config.getChannel(BaseApplication.context());
//        params.put("channel", channel);
        return params;
    }
}
