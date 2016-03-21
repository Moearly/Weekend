package com.martn.weekend.request;

import com.android.volley.Response;
import com.baidu.location.BDLocation;
import com.qmusic.common.BEnvironment;
import com.qmusic.localplugin.BaiduMapPlug;
import com.qmusic.localplugin.PluginManager;
import com.qmusic.volley.QMusicJSONRequest;
import com.qmusic.volley.QMusicRequestManager;
import com.socks.library.KLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Title: Weekend
 * Package: com.martn.weekend.request
 * Description: ("请描述功能")
 * Date 2016/3/18 10:11
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class IRecommendServlet {

    public static void findShowPageList(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        try {
            HashMap<String, String> params = new HashMap();
            JSONObject json = new JSONObject();
            BDLocation loc = ((BaiduMapPlug) PluginManager.getPlugin(BaiduMapPlug.class.getSimpleName())).getLocation();
            if (loc != null) {
                json.put("nowx", String.valueOf(loc.getLatitude()));
                json.put("nowy", String.valueOf(loc.getLongitude()));
            }
            params.put("method", "findShowPageList");
            params.put("servicestr", json.toString());
            QMusicJSONRequest request = new QMusicJSONRequest(1, BEnvironment.RECOMMEND_V2_SERVLET, listener, errorListener);
            request.setParams(params);
            QMusicRequestManager.getInstance().getRequestQueue().add(request);
            KLog.e("url----->" + request.getUrl() + params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void tags(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        HashMap<String, String> params = new HashMap();
        params.put("method", "getTags");
        QMusicJSONRequest request = new QMusicJSONRequest(1, BEnvironment.RECOMMEND_V2_SERVLET, listener, errorListener);
        request.setParams(params);
        QMusicRequestManager.getInstance().getRequestQueue().add(request);
        KLog.e("url----->" + request.getUrl() + params);
    }


}
