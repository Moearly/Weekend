package com.qmusic.model;

import com.qmusic.result.FavFocusResult;

import org.json.JSONObject;

/**
 * Title: Weekend
 * Package: com.qmusic.model
 * Description: ("喜欢关注--数目")
 * Date 2016/3/25 10:21
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class FavFocusModel {
    FavFocusResult result;

    public FavFocusModel(JSONObject json) {
        this.result = praseJson(json);
    }

    private FavFocusResult praseJson(JSONObject json) {
        FavFocusResult result = new FavFocusResult();
        result.code = json.optString("code");
        result.description = json.optString("description");
        result.iscollect = json.optInt("iscollect");
        return result;
    }

    public FavFocusResult getResult() {
        return this.result;
    }

}
