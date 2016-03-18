package com.martn.weekend.api.http;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.martn.weekend.api.ApiError;
import com.qmusic.uitls.Helper;
import com.socks.library.KLog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


/**
 * Title: Juyixia
 * Package: com.lefu.juyixia.api.volley
 * Description: ("处理json数据的返回----统一处理")
 * Date 2015/8/25 12:05
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class JsonCallback implements Response.Listener<String>, Response.ErrorListener {

    //错误码
    private int errorCode;
    //是否有错误消息
    private boolean hasErrorMsg = false;
    private Context mContext;



    public JsonCallback(Context context) {
        mContext = context;
    }

    public void fail(String error) {

        Helper.showToast(error);
    }

    public void fail(String mes, boolean hasError, int code) {

    }

    public void ok(String string) {
        KLog.d(string);
    }

    public void ok(JSONArray array) {
        if (array != null) {
            KLog.d(array.toString());
        }
    }

    public void ok(JSONObject object) {
        if (object != null) {
            KLog.d(object.toString());
        }
    }

    public void ok(JSONObject jsonObject, boolean showE) {
        if (showE && (!TextUtils.isEmpty(ApiError.getErrorMessage(jsonObject))))
            fail(ApiError.getErrorMessage(jsonObject));
    }


    @Override
    public void onErrorResponse(VolleyError error) {

        String errMes = error.getMessage();
        KLog.e(errMes);
        NetworkResponse response = error.networkResponse;
        if (response != null) {
            if ((response.data != null) && (TextUtils.isEmpty(errMes))) {
                errMes = new String(error.networkResponse.data);
            }
            if (response.data != null) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(response.data));
                    if (jsonObject.has("errors")) {
                        hasErrorMsg = true;
                        errMes = ApiError.getErrorMessage(jsonObject);
                        errorCode = ApiError.getErrorCode(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if ((error instanceof NoConnectionError)) {
            MobclickAgent.onEvent(mContext, "AndroidNoConnectionError");
            errMes = "NoConnectionError：连接错误，请检查您的网络稍后重试";
        } else if ((error instanceof AuthFailureError)) {
            KLog.e("AuthFailureError");
        } else if (error instanceof ServerError) {
            MobclickAgent.onEvent(mContext, "AndroidServerError");
            errMes = "ServerError：服务器出错啦，请稍后重试";
        } else if ((error instanceof ParseError)) {
            KLog.e("ParseError");
        } else if ((error instanceof NetworkError)) {
            MobclickAgent.onEvent(mContext, "AndroidNetworkError");
            errMes = "NetworkError：网络出错啦，请检查您的网络稍后重试";
        } else if ((error instanceof TimeoutError)) {
            MobclickAgent.onEvent(mContext, "AndroidTimeoutError");
            errMes = "TimeoutError：请求超时，请检查您的网络稍后重试";
        }
        fail(errMes);
        fail(errMes, hasErrorMsg, errorCode);
        onFinish();
    }

    @Override
    public void onResponse(String response) {

        String newResponse = response.trim();
        if ((TextUtils.isEmpty(newResponse)) || (mContext == null)) {
            return;
        } else {
            try {
                Object valueData = null;
                if ((newResponse.startsWith("{")) || (newResponse.startsWith("["))) {
                    JSONTokener jsonTokener = new JSONTokener(newResponse);
                    valueData = jsonTokener.nextValue();
                    if ((valueData instanceof JSONObject)) {
                        if (!((JSONObject) valueData).has("errors")) {
                            ok((JSONObject) valueData);
                        } else {
                            hasErrorMsg = true;
                            ok((JSONObject) valueData, true);
                        }
                    } else if (valueData instanceof JSONArray) {
                        ok((JSONArray) valueData);
                    } else {
                        ok(newResponse);
                    }

                } else {
                    ok(newResponse);
                }
                onFinish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onFinish() {
    }

}
