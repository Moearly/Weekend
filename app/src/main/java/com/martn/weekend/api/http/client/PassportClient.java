package com.martn.weekend.api.http.client;

import android.content.Context;

import com.android.volley.Response;
import com.martn.weekend.api.http.JsonCallback;
import com.martn.weekend.api.http.JsonParams;
import com.martn.weekend.api.http.RequestManager;

/**
 * Title: Juyixia
 * Package: com.lefu.juyixia.api.http.client
 * Description: ("新版登录--登录数据管理")
 * Date 2015/9/5 23:04
 *
 * @author MartnLei MartnLei_163_com
 * @version V2.0
 */
public class PassportClient extends BaseJsonRequest {

    public static final String PRODUCTION = "http://app.zealer.com";
    private static final String QA = "http://192.168.0.111";

    public PassportClient(int method, String url, JsonParams requestBody, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
    }

    public static String getAbsoluteUrl(String url) {
        if (DEBUG) {
            return QA + url;
        } else {
            return PRODUCTION + url;
        }
    }

    public static String getHost() {
        if (DEBUG) {
            return QA;
        } else {
            return PRODUCTION;
        }
    }

    public static void post(String url, JsonParams params, Context context, JsonCallback callback) {

        RequestManager.addRequest(new PassportClient(Method.POST, getAbsoluteUrl(url), signParams(params), callback, callback), context);
    }

    /**
     * 基本sign信息
     * @param jsonParams
     * @return
     */
    private static JsonParams signParams(JsonParams jsonParams) {
//        jsonParams.put("version", SystemUtil.getVersionName());
//        jsonParams.put("channel", SystemUtil.getChannel());
//        jsonParams.put("android","android");
        return jsonParams;
    }
}
