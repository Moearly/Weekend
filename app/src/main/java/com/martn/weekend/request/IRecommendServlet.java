package com.martn.weekend.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.baidu.location.BDLocation;
import com.qmusic.common.BEnvironment;
import com.qmusic.localplugin.BaiduMapPlug;
import com.qmusic.localplugin.PluginManager;
import com.qmusic.uitls.AppUtils;
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

    /**
     * 获取主页list数据
     * @param pageIndex
     * @param channelid
     * @param tagid
     * @param listener
     * @param errorListener
     */
    public static void mains(int pageIndex, String channelid, int tagid, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        try {
            HashMap<String, String> params = new HashMap();
            JSONObject json = new JSONObject();
            json.put("nowx", AppUtils.getLatitude());
            json.put("nowy", AppUtils.getLongitude());
            json.put("pageindex", pageIndex);
            json.put("channelid", channelid);
            json.put("tag_id", tagid);
            params.put("method", "mainV3");
            params.put("servicestr", json.toString());
            QMusicJSONRequest request = new QMusicJSONRequest(Request.Method.POST, BEnvironment.RECOMMEND_V2_SERVLET, listener, errorListener);
            request.setParams(params);
            QMusicRequestManager.getInstance().getRequestQueue().add(request);
            KLog.e("url----->" + request.getUrl() + params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取展示的热点标签数据
     * @param listener
     * @param errorListener
     */
    public static void findHotSubjectForShow(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        HashMap<String, String> params = new HashMap();
        params.put("method", "findHotSubjectForShow");
        QMusicJSONRequest request = new QMusicJSONRequest(Request.Method.POST, BEnvironment.RECOMMEND_V2_SERVLET, listener, errorListener);
        request.setParams(params);
        QMusicRequestManager.getInstance().getRequestQueue().add(request);
        KLog.e("url----->" + request.getUrl() + params);
    }

    /**
     *
     * @param listener
     * @param errorListener
     */
    public static void findSubjectForShow(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        HashMap<String, String> params = new HashMap();
        params.put("method", "findSubjectForShow");
        QMusicJSONRequest request = new QMusicJSONRequest(Request.Method.POST, BEnvironment.RECOMMEND_V2_SERVLET, listener, errorListener);
        request.setParams(params);
        QMusicRequestManager.getInstance().getRequestQueue().add(request);
        KLog.e("url----->" + request.getUrl() + params);
    }

    /**
     * 范围查找
     * @param tagId
     * @param pageIndex
     * @param scopeRange
     * @param maxScopeRange
     * @param nowx
     * @param nowy
     * @param scopex
     * @param scopey
     * @param listener
     * @param errorListener
     */
    public static void findCourseForScope(int tagId, int pageIndex, double scopeRange, double maxScopeRange, double nowx, double nowy, double scopex, double scopey, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        try {
            HashMap<String, String> params = new HashMap();
            JSONObject json = new JSONObject();
            json.put("nowx", nowx);
            json.put("nowy", nowy);
            json.put("scopex", scopex);
            json.put("scopey", scopey);
            json.put("tag_id", tagId);
            json.put("pageindex", pageIndex);
            json.put("scope_range", scopeRange);
            json.put("max_scope_range", maxScopeRange);
            params.put("method", "findCourseForScope");
            params.put("servicestr", json.toString());
            QMusicJSONRequest request = new QMusicJSONRequest(Request.Method.POST, BEnvironment.RECOMMEND_V2_SERVLET, listener, errorListener);
            request.setParams(params);
            QMusicRequestManager.getInstance().getRequestQueue().add(request);
            KLog.e("url----->" + request.getUrl() + params);
        } catch (JSONException e) {
        }
    }


    /**
     * 标签搜素
     * @param teacherid
     * @param pageIndex
     * @param listener
     * @param errorListener
     */
    public void sendSeachByTag(int teacherid, int pageIndex, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        try {
            HashMap<String, String> params = new HashMap();
            JSONObject json = new JSONObject();
            BDLocation loc = ((BaiduMapPlug) PluginManager.getPlugin(BaiduMapPlug.class.getSimpleName())).getLocation();
            if (loc != null) {
                json.put("nowx", String.valueOf(loc.getLatitude()));
                json.put("nowy", String.valueOf(loc.getLongitude()));
            }
            json.put("teacherid", teacherid);
            json.put("pageindex", pageIndex);
            params.put("method", "seachbytagV2");
            params.put("servicestr", json.toString());
            QMusicJSONRequest request = new QMusicJSONRequest(1, BEnvironment.RECOMMEND_V2_SERVLET, listener, errorListener);
            request.setParams(params);
            QMusicRequestManager.getInstance().getRequestQueue().add(request);
            KLog.e("url----->" + request.getUrl() + params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据用户id查询---已经参加的课程
     * @param userid
     * @param pageIndex
     * @param listener
     * @param errorListener
     */
    public void findTakePartCourseByUserId(int userid, int pageIndex, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        try {
            HashMap<String, String> params = new HashMap();
            JSONObject json = new JSONObject();
            BDLocation loc = ((BaiduMapPlug) PluginManager.getPlugin(BaiduMapPlug.class.getSimpleName())).getLocation();
            if (loc != null) {
                json.put("nowx", String.valueOf(loc.getLatitude()));
                json.put("nowy", String.valueOf(loc.getLongitude()));
            }
            json.put("course_user_id", userid);
            json.put("pageindex", pageIndex);
            params.put("method", "findTakePartCourseByUserId");
            params.put("servicestr", json.toString());
            QMusicJSONRequest request = new QMusicJSONRequest(1, BEnvironment.RECOMMEND_V2_SERVLET, listener, errorListener);
            request.setParams(params);
            QMusicRequestManager.getInstance().getRequestQueue().add(request);
            KLog.e("url----->" + request.getUrl() + params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void findCollectCourseByUserId(int userid, int pageIndex, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        try {
            HashMap<String, String> params = new HashMap();
            JSONObject json = new JSONObject();
            BDLocation loc = ((BaiduMapPlug) PluginManager.getPlugin(BaiduMapPlug.class.getSimpleName())).getLocation();
            if (loc != null) {
                json.put("nowx", String.valueOf(loc.getLatitude()));
                json.put("nowy", String.valueOf(loc.getLongitude()));
            }
            json.put("course_user_id", userid);
            json.put("pageindex", pageIndex);
            params.put("method", "findCollectCourseByUserId");
            params.put("servicestr", json.toString());
            QMusicJSONRequest request = new QMusicJSONRequest(1, BEnvironment.RECOMMEND_V2_SERVLET, listener, errorListener);
            request.setParams(params);
            QMusicRequestManager.getInstance().getRequestQueue().add(request);
            KLog.e("url----->" + request.getUrl() + params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





}
