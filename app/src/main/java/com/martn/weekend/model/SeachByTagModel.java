package com.martn.weekend.model;

import com.martn.weekend.result.SeachByTagResult;
import com.sina.weibo.sdk.register.mobile.SelectCountryActivity;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Title: Weekend
 * Package: com.martn.weekend.model
 * Description: ("请描述功能")
 * Date 2014/10/5 14:51
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class SeachByTagModel {
    SeachByTagResult result;

    public SeachByTagModel(JSONObject json) {
        this.result = praseJson(json);
    }

    private SeachByTagResult praseJson(JSONObject json) {
        SeachByTagResult result = new SeachByTagResult();
        result.code = json.optString("code");
        result.description = json.optString("description");
        result.teacherId = json.optInt("teacher_id");
        result.teacherType = json.optString("teacher_type");
        result.teacherNickname = json.optString("teacher_nickname");
        result.teacherOneabtract = json.optString("teacher_iscollect");
        result.teacherIscollect = json.optInt("teacher_iscollect");
        result.teacherPhoto = json.optString("teacher_photo");
        result.teacherIsown = json.optInt("teacher_isown", 0);
        result.courseUserList = new ArrayList();
        JSONArray courseUserArr = json.optJSONArray("course_user_arr");
        if (courseUserArr != null) {
            for (int i = 0; i < courseUserArr.length(); i++) {
                TcrModel tcr = new TcrModel();
                tcr.parse(courseUserArr.optJSONObject(i));
                if (tcr != null) {
                    result.courseUserList.add(tcr);
                }
            }
        }
        return result;
    }

    public SeachByTagResult getResult() {
        return this.result;
    }

}
