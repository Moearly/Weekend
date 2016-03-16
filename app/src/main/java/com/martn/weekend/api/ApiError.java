package com.martn.weekend.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Title: Juyixia
 * Package: com.lefu.juyixia.one.model
 * Description: ("网络错误描述")
 * Date 2015/7/30 18:06
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class ApiError extends TypeToken<ArrayList<ApiError>> {

    //错误代码
    public int code;
    //错误信息
    public String message;

    public static int getErrorCode(JSONObject json) {

        int code = 0;
        JSONArray errorsA = json.optJSONArray("errors");
        if ((errorsA != null) && (errorsA.length() > 0)) {
            JSONObject jsonObject = errorsA.optJSONObject(0);
            if (jsonObject != null)
                code = jsonObject.optInt("code");
        }
        return code;
    }


    public static String getErrorMessage(JSONObject json) {
        String str = "";
        JSONArray errorsA = json.optJSONArray("errors");
        if ((errorsA != null) && (errorsA.length() > 0)) {
            JSONObject obj = errorsA.optJSONObject(0);
            if (obj != null) {
                str = obj.optString("message");
                obj.optInt("code");
            }
        }
        return str;
    }

    /**
     * 解析错误
     * @param json
     * @return
     */
    public static ArrayList<ApiError> parseErrors(JSONObject json) {
        Object obj;
        if (!json.has("errors")) {
            obj = null;
        } else {
            JSONArray errors = json.optJSONArray("errors");
            if (errors == null) {
                obj = null;
            } else {
                Type type = new ApiError().getType();
                obj = (ArrayList) new Gson().fromJson(errors.toString(), type);
            }
        }
        return (ArrayList<ApiError>) obj;
    }

}
