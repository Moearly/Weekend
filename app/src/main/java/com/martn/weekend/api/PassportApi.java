package com.martn.weekend.api;

import android.content.Context;

import com.martn.weekend.api.http.JsonCallback;
import com.martn.weekend.api.http.JsonParams;
import com.martn.weekend.api.http.client.PassportClient;


/**
 * Title: ZeaApp
 * Package: com.martn.zeaapp.api
 * Description: ("请描述功能")
 * Date 2014/10/5 18:27
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class PassportApi {

    public static final String RE_LOGIN = "/call/reToken";
    public static final String QQ_LOGIN="/call/social";
    public static final String LOGIN = "/call/login";

    /**
     * 更新登陆
     * @param context
     * @param callback
     * @param token
     */
    public static void reLogin(Context context, JsonCallback callback, String token) {
        JsonParams params = new JsonParams();
        params.put("token", token);
        PassportClient.post(RE_LOGIN, params, context, callback);
    }

    /**
     * 登陆
     * @param context
     * @param callback
     * @param passwd
     * @param account
     */
    public static void Login(Context context, String passwd, String account,JsonCallback callback) {
        JsonParams params = new JsonParams();
//        params.put("password", CipherUtils.AESEncrypt(passwd));
//        params.put("username", CipherUtils.AESEncrypt(account));
        PassportClient.post(RE_LOGIN, params, context, callback);
    }


    public static void QQLogin(Context context, JsonCallback callback, JsonParams params) {
        PassportClient.post(RE_LOGIN, params, context, callback);
    }

//    public static void getWeChat()

}
