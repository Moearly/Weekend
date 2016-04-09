package com.martn.weekend.result;

import com.martn.weekend.model.TagModel;
import com.qmusic.result.BaseResult;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;


public class TagsResult extends BaseResult {
    public List<TagModel> tagList;

    public TagsResult(JSONObject json) {
        super.bparse(json);
        parse(json);
    }

    private void parse(JSONObject json) {
        JSONArray tagArr = json.optJSONArray("tag_arr");
        if (tagArr != null) {
            this.tagList = new ArrayList();
            for (int i = 0; i < tagArr.length(); i++) {
                this.tagList.add(new TagModel(tagArr.optJSONObject(i)));
            }
        }
    }
}