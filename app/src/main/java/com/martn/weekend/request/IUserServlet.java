package com.martn.weekend.request;

import android.content.Context;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.baidu.location.BDLocation;
import com.qmusic.common.BEnvironment;
import com.qmusic.localplugin.BaiduMapPlug;
import com.qmusic.localplugin.PluginManager;
import com.qmusic.uitls.AppUtils;
import com.qmusic.volley.QMusicJSONRequest;
import com.qmusic.volley.QMusicRequestManager;
import com.socks.library.KLog;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class IUserServlet {

    public static void sendLogin(Context context, String mobile, String password, Listener<JSONObject> listener, ErrorListener errorListener) {
        try {
            HashMap<String, String> params = new HashMap();
            JSONObject json = new JSONObject();
            BDLocation loc = ((BaiduMapPlug) PluginManager.getPlugin(BaiduMapPlug.class.getSimpleName())).getLocation();
            if (loc != null) {
                json.put("userx", String.valueOf(loc.getLatitude()));
                json.put("usery", String.valueOf(loc.getLongitude()));
            }
            json.put("facility", AppUtils.getPhoneInfo());
            json.put("facilityNum", "");//PushManager.getInstance().getClientid(context)
            json.put("mobile", mobile);
            json.put("password", password);
            params.put("method", "login");
            params.put("servicestr", json.toString());
            QMusicJSONRequest request = new QMusicJSONRequest(1, BEnvironment.I_USER_SERVLET, listener, errorListener);
            request.setParams(params);
            QMusicRequestManager.getInstance().getRequestQueue().add(request);
            KLog.e("url----->" + request.getUrl() + params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    public static void sendReg(Context context, String mobile, String dynamic, String password, String againPassword, String userRecommendnum, Listener<JSONObject> listener, ErrorListener errorListener) {
//        try {
//            HashMap<String, String> params = new HashMap();
//            JSONObject json = new JSONObject();
//            BDLocation loc = ((BaiduMapPlug) PluginManager.getPlugin(BaiduMapPlug.class.getSimpleName())).getLocation();
//            if (loc != null) {
//                json.put("userx", String.valueOf(loc.getLatitude()));
//                json.put("usery", String.valueOf(loc.getLongitude()));
//            }
//            json.put("facility", BUtilities.getPhoneInfo());
//            json.put("facilityNum", PushManager.getInstance().getClientid(context));
//            json.put("mobile", mobile);
//            json.put("dynamic_password", dynamic);
//            json.put("password", password);
//            json.put("verify_password", againPassword);
//            if (BUtilities.stringIsNotNull(userRecommendnum)) {
//                json.put("user_recommendnum", userRecommendnum);
//            }
//            params.put("method", "registerV2");
//            params.put("servicestr", json.toString());
//            QMusicJSONRequest request = new QMusicJSONRequest(1, BEnvironment.I_USER_SERVLET, listener, errorListener);
//            request.setParams(params);
//            QMusicRequestManager.getInstance().getRequestQueue().add(request);
//            L.e("url----->" + request.getUrl() + params);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void sendDynamic(String mobile, Listener<JSONObject> listener, ErrorListener errorListener) {
//        try {
//            HashMap<String, String> params = new HashMap();
//            JSONObject json = new JSONObject();
//            json.put("mobile", mobile);
//            params.put("method", "dynamic");
//            params.put("servicestr", json.toString());
//            QMusicJSONRequest request = new QMusicJSONRequest(1, BEnvironment.I_USER_SERVLET, listener, errorListener);
//            request.setParams(params);
//            QMusicRequestManager.getInstance().getRequestQueue().add(request);
//            BLog.e(CoinPacketExtension.NAMESPACE, "url----->" + request.getUrl() + params);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void getDynamic(String mobile, String verifyCode, Listener<JSONObject> listener, ErrorListener errorListener) {
//        try {
//            HashMap<String, String> params = new HashMap();
//            JSONObject json = new JSONObject();
//            json.put("mobile", mobile);
//            json.put("verify_code", verifyCode);
//            params.put("method", "getDynamic");
//            params.put("servicestr", json.toString());
//            QMusicJSONRequest request = new QMusicJSONRequest(1, BEnvironment.I_USER_SERVLET, listener, errorListener);
//            request.setParams(params);
//            QMusicRequestManager.getInstance().getRequestQueue().add(request);
//            L.e("url----->" + request.getUrl() + params);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void sendForgetPw(String mobile, String dynamic, String password, String againPassword, Listener<JSONObject> listener, ErrorListener errorListener) {
//        try {
//            HashMap<String, String> params = new HashMap();
//            JSONObject json = new JSONObject();
//            json.put("mobile", mobile);
//            json.put("dynamic_password", dynamic);
//            json.put("password", password);
//            json.put("verify_password", againPassword);
//            params.put("method", "forgetPw");
//            params.put("servicestr", json.toString());
//            QMusicJSONRequest request = new QMusicJSONRequest(1, BEnvironment.I_USER_SERVLET, listener, errorListener);
//            request.setParams(params);
//            QMusicRequestManager.getInstance().getRequestQueue().add(request);
//            L.e("url----->" + request.getUrl() + params);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void sendLoginOut(Listener<JSONObject> listener, ErrorListener errorListener) {
//        HashMap<String, String> params = new HashMap();
//        JSONObject json = new JSONObject();
//        params.put("method", "loginOut");
//        params.put("servicestr", json.toString());
//        QMusicJSONRequest request = new QMusicJSONRequest(1, BEnvironment.I_USER_SERVLET, listener, errorListener);
//        request.setParams(params);
//        QMusicRequestManager.getInstance().getRequestQueue().add(request);
//        BLog.e(CoinPacketExtension.NAMESPACE, "url----->" + request.getUrl() + params);
//    }
//
//    public void sendSetupPassword(String mobile, String password, String verifyPassword, Listener<JSONObject> listener, ErrorListener errorListener) {
//        try {
//            HashMap<String, String> params = new HashMap();
//            JSONObject json = new JSONObject();
//            json.put("mobile", mobile);
//            json.put("password", password);
//            json.put("verify_password", verifyPassword);
//            params.put("method", "setPw");
//            params.put("servicestr", json.toString());
//            QMusicJSONRequest request = new QMusicJSONRequest(1, BEnvironment.I_USER_SERVLET, listener, errorListener);
//            request.setParams(params);
//            QMusicRequestManager.getInstance().getRequestQueue().add(request);
//            BLog.e(CoinPacketExtension.NAMESPACE, "url----->" + request.getUrl() + params);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void sendDLogin(Context context, String mobile, String password, Listener<JSONObject> listener, ErrorListener errorListener) {
//        try {
//            HashMap<String, String> params = new HashMap();
//            JSONObject json = new JSONObject();
//            BDLocation loc = ((BaiduMapPlug) PluginManager.getPlugin(BaiduMapPlug.class.getSimpleName())).getLocation();
//            if (loc != null) {
//                json.put("userx", String.valueOf(loc.getLatitude()));
//                json.put("usery", String.valueOf(loc.getLongitude()));
//            }
//            json.put("facility", BUtilities.getPhoneInfo());
//            json.put("facilityNum", PushManager.getInstance().getClientid(context));
//            json.put("mobile", mobile);
//            json.put("password", password);
//            params.put("method", "dloginreg");
//            params.put("servicestr", json.toString());
//            QMusicJSONRequest request = new QMusicJSONRequest(1, BEnvironment.I_USER_SERVLET, listener, errorListener);
//            request.setParams(params);
//            QMusicRequestManager.getInstance().getRequestQueue().add(request);
//            BLog.e(CoinPacketExtension.NAMESPACE, "url----->" + request.getUrl() + params);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void sendWeixinLogin(String code, Listener<JSONObject> listener, ErrorListener errorListener) {
//        try {
//            HashMap<String, String> params = new HashMap();
//            JSONObject json = new JSONObject();
//            json.put("wei_code", code);
//            json.put("wei_type", 1);
//            params.put("method", "weixinLogin");
//            params.put("servicestr", json.toString());
//            QMusicJSONRequest request = new QMusicJSONRequest(1, BEnvironment.I_USER_SERVLET, listener, errorListener);
//            request.setParams(params);
//            QMusicRequestManager.getInstance().getRequestQueue().add(request);
//            L.e("url----->" + request.getUrl() + params);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void sendQQLogin(String openid, Listener<JSONObject> listener, ErrorListener errorListener) {
//        try {
//            HashMap<String, String> params = new HashMap();
//            JSONObject json = new JSONObject();
//            json.put("qq_openid", openid);
//            params.put("method", "qqLogin");
//            params.put("servicestr", json.toString());
//            QMusicJSONRequest request = new QMusicJSONRequest(1, BEnvironment.I_USER_SERVLET, listener, errorListener);
//            request.setParams(params);
//            QMusicRequestManager.getInstance().getRequestQueue().add(request);
//            BLog.e(CoinPacketExtension.NAMESPACE, "url----->" + request.getUrl() + params);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void toRegV3(Listener<JSONObject> listener, ErrorListener errorListener) {
//        try {
//            HashMap<String, String> params = new HashMap();
//            JSONObject json = new JSONObject();
//            BDLocation loc = ((BaiduMapPlug) PluginManager.getPlugin(BaiduMapPlug.class.getSimpleName())).getLocation();
//            if (loc != null) {
//                json.put("userx", String.valueOf(loc.getLatitude()));
//                json.put("usery", String.valueOf(loc.getLongitude()));
//            }
//            params.put("method", "toRegV3");
//            params.put("servicestr", json.toString());
//            QMusicJSONRequest request = new QMusicJSONRequest(1, BEnvironment.I_USER_SERVLET, listener, errorListener);
//            request.setParams(params);
//            QMusicRequestManager.getInstance().getRequestQueue().add(request);
//            BLog.e(CoinPacketExtension.NAMESPACE, "url----->" + request.getUrl() + params);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void getVerifyCode(Listener<JSONObject> listener, ErrorListener errorListener) {
//        HashMap<String, String> params = new HashMap();
//        params.put("method", "getVerifyCode");
//        QMusicJSONRequest request = new QMusicJSONRequest(1, BEnvironment.I_USER_SERVLET, listener, errorListener);
//        request.setParams(params);
//        QMusicRequestManager.getInstance().getRequestQueue().add(request);
//        L.e("url----->" + request.getUrl() + params);
//    }
//
//    public static void weixinBoundMobile(Context context, String mobile, String dynamicPasswor, Listener<JSONObject> listener, ErrorListener errorListener) {
//        try {
//            HashMap<String, String> params = new HashMap();
//            JSONObject json = new JSONObject();
//            json.put("mobile", mobile);
//            json.put("password", dynamicPasswor);
//            json.put("facility", BUtilities.getPhoneInfo());
//            json.put("facilityNum", PushManager.getInstance().getClientid(context));
//            json.put("userx", Utils.getLatitude());
//            json.put("usery", Utils.getLongitude());
//            params.put("method", "weixinBoundMobile");
//            params.put("servicestr", json.toString());
//            QMusicJSONRequest request = new QMusicJSONRequest(1, BEnvironment.I_USER_SERVLET, listener, errorListener);
//            request.setParams(params);
//            QMusicRequestManager.getInstance().getRequestQueue().add(request);
//            L.e("url----->" + request.getUrl() + params);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}