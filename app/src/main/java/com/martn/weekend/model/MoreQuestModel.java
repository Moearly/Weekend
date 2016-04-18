package com.martn.weekend.model;

import com.martn.weekend.result.MoreQuestResult;

import org.json.JSONObject;

/**
 * Title: Weekend
 * Package: com.martn.weekend.model
 * Description: ("请描述功能")
 * Date 2016/4/18 17:26
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class MoreQuestModel {
    MoreQuestResult result;

    public MoreQuestModel(JSONObject json) {
        this.result = praseJson(json);
    }

    private MoreQuestResult praseJson(JSONObject json) {
        this.result = new MoreQuestResult();
        this.result.parse(json);
        return this.result;
    }

    public MoreQuestResult getResult() {
        return this.result;
    }

}
