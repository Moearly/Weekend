package com.qmusic.volley;

import android.text.TextUtils;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.qmusic.app.App;
import com.qmusic.base.BaseApplication;
import com.qmusic.common.Common;
import com.qmusic.db.UserPreference;
import com.qmusic.uitls.BLog;
import com.qmusic.uitls.SharedPreferencesUtil;
import com.qmusic.volley.cache.utils.L;
import com.socks.library.KLog;

import java.io.UnsupportedEncodingException;
import org.json.JSONObject;


/**
 * Title: Weekend
 * Package: com.qmusic.volley
 * Description: ("请描述功能")
 * Date 2016/3/18 10:32
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class QMusicJSONRequest extends QmusicRequest<JSONObject> {
    public QMusicJSONRequest(int method, String url, Listener<JSONObject> listener, ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        setShouldCache(false);
    }

    protected VolleyError parseNetworkError(VolleyError volleyError) {
        if (volleyError.networkResponse != null) {
            try {
                L.e("parseNetworkError message : " + new String(volleyError.networkResponse.data));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            BLog.e("QMusicJSONRequest", "Network response is null");
        }
        return volleyError;
    }

    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        Response<JSONObject> responseJSON;
        String str;
        try {
            str = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            str = new String(response.data);
        }
        if (TextUtils.isEmpty(str)) {
            responseJSON = Response.success(new JSONObject(), QmusicRequest.parseCacheHeaders(getUrl(), response));
        } else {
            try {
                responseJSON = Response.success(new JSONObject(str), QmusicRequest.parseCacheHeaders(getUrl(), response));
            } catch (Exception ex) {
                ex.printStackTrace();
                responseJSON = Response.error(new VolleyError(response));
            }
        }
        if (response.headers.containsKey("Set-Cookie")) {
            for (String s : ((String) response.headers.get("Set-Cookie")).split(";")) {
                if (s.indexOf("JSESSIONID") > -1) {
                    KLog.i("JSESSIONID get : " + s);
                    KLog.i("JSESSIONID save : " + s);
                    UserPreference.getInstance(BaseApplication.context()).saveToken(s);
                } else if (s.indexOf(Common.Key.CITY) > -1) {
                    Common.isRefresh = true;
                    UserPreference.getInstance(BaseApplication.context()).setUserCity(s.replaceAll(Common.Key.CITY+"=", ""));
                }
            }
        }
        KLog.e("", "url------> response : " + str);
        return responseJSON;
    }
}
