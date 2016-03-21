package com.qmusic.result;

import org.json.JSONObject;

import java.util.Locale;

public class ToUserCenterModel {
    ToUserCenterResult result;

    public ToUserCenterModel(JSONObject json) {
        this.result = praseJson(json);
    }

    private ToUserCenterResult praseJson(JSONObject json) {
        ToUserCenterResult result = new ToUserCenterResult();
        result.code = json.optString("code");
        result.description = json.optString("description");
        result.userId = json.optString("user_id");
        result.userNickname = json.optString("user_nickname");
        result.userType = json.optString("user_type");
        result.userPhoto = json.optString("user_photo");
        result.userOneabstract = json.optString("user_oneabstract");
        result.userNotlookinfoCount = json.optString("user_notlookinfo_count");
        result.userRecommendnum = json.optString("user_recommendnum");
        result.userHuanxinUsername = json.optString("user_huanxin_username", "").toLowerCase(Locale.getDefault());
        result.userHuanxinPassword = json.optString("user_huanxin_password", "");
        result.userConstellation = json.optString("user_constellation", "");
        result.userAge = json.optString("user_age", "");
        result.userSex = json.optInt("user_sex", 0);
        result.userBirthday = json.optString("user_birthday", "");
        result.faveCount = json.optInt("user_fave_count", 0);
        result.faveMeCount = json.optInt("user_faveme_count", 0);
        return result;
    }

    public ToUserCenterResult getResult() {
        return this.result;
    }
}