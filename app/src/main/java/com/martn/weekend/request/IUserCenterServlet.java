package com.martn.weekend.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.qmusic.common.BEnvironment;
import com.socks.library.KLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Title: Weekend
 * Package: com.martn.weekend.request
 * Description: ("请描述功能")
 * Date 2016/3/21 11:18
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class IUserCenterServlet {

    public static void sendFavFocus(int favid, int tagtype, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        try {
            HashMap<String, String> params = new HashMap();
            JSONObject json = new JSONObject();
            json.put("favid", favid);
            json.put("tagtype", tagtype);
            params.put("method", "favFocus");
            params.put("servicestr", json.toString());
            QMusicJSONRequest request = new QMusicJSONRequest(Request.Method.POST, BEnvironment.IUSER_CENTER_V2_SERVLET, listener, errorListener);
            request.setParams(params);
            QMusicRequestManager.getInstance().getRequestQueue().add(request);
            KLog.e("url----->" + request.getUrl() + params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    /**
     * 获取用户信息
     * @param listener
     * @param errorListener
     */
    public static void sendToUserCenter(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        HashMap<String, String> params = new HashMap();
        params.put("method", "toUserCenter");
        QMusicJSONRequest request = new QMusicJSONRequest(Request.Method.POST, BEnvironment.IUSER_CENTER_V2_SERVLET, listener, errorListener);
        request.setParams(params);
        QMusicRequestManager.getInstance().getRequestQueue().add(request);
        KLog.e("url----->" + request.getUrl() + params);
    }

    /**
     * 获取通知消息
     * @param listener
     * @param errorListener
     */
    public static synchronized void getNewsCount(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        synchronized (IUserCenterServlet.class) {
            HashMap<String, String> params = new HashMap();
            params.put("method", "getNewsCountV3");
            QMusicJSONRequest request = new QMusicJSONRequest(Request.Method.POST, BEnvironment.IUSER_CENTER_V2_SERVLET, listener, errorListener);
            request.setParams(params);
            QMusicRequestManager.getInstance().getRequestQueue().add(request);
            KLog.e("url----->" + request.getUrl() + params);
        }
    }



}
