package com.martn.weekend.api.http;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Title: Juyixia
 * Package: com.lefu.juyixia.api.volley
 * Description: ("网络数据请求时的json参数")
 * Date 2015/8/27 17:59
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class JsonParams {
    

    protected final JSONObject params = new JSONObject();

    /**
     * 获得参数--转换为string--主要构建在尾部的请求
     * @return
     */
    public String getEncodedParamString() {

        StringBuilder builder = new StringBuilder();
        Iterator iterator = params.keys();
        while (iterator.hasNext()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            String key = (String)iterator.next();
            String value = params.optString(key);
            builder.append(URLEncoder.encode(key));
            builder.append("=");
            builder.append(URLEncoder.encode(value));
        }
        return builder.toString();
    }
    public void put(String key, float value) {
        if (key != null) {
            try {
                params.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void put(String key, int value) {
        if (key != null) {
            try {
                this.params.put(key, value);

            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
    }

    public void put(String key, JsonParams value) {
        if ((key != null) && (value != null)) {
            try {
                this.params.put(key, value.toJson());

            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
    }

    public void put(String key, String value) {
        if ((key != null) && (value != null)) {
            try {
                this.params.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void put(String key, JSONArray value) {
        if ((key != null) && (value != null)) {
            try {
                this.params.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void put(String key, JSONObject value) {
        if ((key != null) && (value != null)) {
            try {
                this.params.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void put(String key, boolean value) {
        if (key != null) {
            try {
                this.params.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void put(String key, String[] value) {
        if ((key != null) && (value != null)) {
            try {
                this.params.put(key, new JSONArray(Arrays.asList(value)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除操作
     * @param key
     * @return
     */
    public JsonParams remove(String key) {

        params.remove(key);
        return this;
    }

    public JSONObject toJson() {
        return params;
    }

    @Override
    public String toString() {
        return params.toString();
    }

    public JsonParams with(String key) {
        JsonParams p;
        if (key != null) {
            p = new JsonParams();
            p.put(key, params);
        } else {
            p = null;
        }
        return p;
    }
}
