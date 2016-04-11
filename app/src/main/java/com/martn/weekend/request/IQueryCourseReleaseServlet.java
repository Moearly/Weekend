package com.martn.weekend.request;

import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.qmusic.common.BEnvironment;
import com.qmusic.uitls.AppUtils;
import com.socks.library.KLog;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Title: Weekend
 * Package: com.martn.weekend.request
 * Description: ("请描述功能")
 * Date 2014/10/5 23:12
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class IQueryCourseReleaseServlet {

    /**
     * 课程详情
     * @param courseid
     * @param listener
     * @param errorListener
     */
    public static void toDetail(int courseid, Listener<JSONObject> listener, ErrorListener errorListener) {
        try {
            HashMap<String, String> params = new HashMap();
            JSONObject json = new JSONObject();
            json.put("nowx", AppUtils.getLatitude());
            json.put("nowy", AppUtils.getLongitude());
            json.put("courseid", courseid);
            params.put("method", "toDetailV2");
            params.put("servicestr", json.toString());
            QMusicJSONRequest request = new QMusicJSONRequest(1, BEnvironment.I_QUERY_COURSE_RELEASE_V2_SERVLET, listener, errorListener);
            request.setParams(params);
            QMusicRequestManager.getInstance().getRequestQueue().add(request);
            KLog.e("ActListFragment url----->" + request.getUrl() + params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取热门课程
     * @param courseid
     * @param listener
     * @param errorListener
     */
    public static void hotCourse(int courseid, Listener<JSONObject> listener, ErrorListener errorListener) {
        try {
            HashMap<String, String> params = new HashMap();
            JSONObject json = new JSONObject();
            json.put("nowx", AppUtils.getLatitude());
            json.put("nowy", AppUtils.getLongitude());
            json.put("courseid", courseid);
            params.put("method", "hotCourse");
            params.put("servicestr", json.toString());
            QMusicJSONRequest request = new QMusicJSONRequest(1, BEnvironment.I_QUERY_COURSE_RELEASE_V2_SERVLET, listener, errorListener);
            request.setParams(params);
            QMusicRequestManager.getInstance().getRequestQueue().add(request);
            KLog.e("url----->" + request.getUrl() + params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
