package com.martn.weekend.model;

import com.martn.weekend.result.GetOtherUserDetailV3Result;

import org.json.JSONObject;

/**
 * Title: Weekend
 * Package: com.martn.weekend.model
 * Description: ("请描述功能")
 * Date 2014/10/5 16:07
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class GetOtherUserDetailV2Model {
    GetOtherUserDetailV3Result result;

    public GetOtherUserDetailV2Model(JSONObject json) {
        this.result = praseJson(json);
    }

    private GetOtherUserDetailV3Result praseJson(JSONObject json) {
        this.result = new GetOtherUserDetailV3Result();
        this.result.parse(json);
        return this.result;
    }

    public GetOtherUserDetailV3Result getResult() {
        return this.result;
    }

}
