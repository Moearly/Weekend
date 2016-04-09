package com.martn.weekend.result;

import com.martn.weekend.model.TagNewModel;
import com.qmusic.result.BaseResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: Weekend
 * Package: com.martn.weekend.result
 * Description: ("")
 * Date 2014/10/5 22:25
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class SubjectResult extends BaseResult {
    public List<TagNewModel> tagList;

    public SubjectResult(JSONObject json) {
        super.bparse(json);
        parse(json);
    }

    public SubjectResult() {

    }
    private void parse(JSONObject json) {
        JSONArray tagArr = json.optJSONArray("tag_arr");
        if (tagArr != null) {
            this.tagList = new ArrayList();
            for (int i = 0; i < tagArr.length(); i++) {
                this.tagList.add(new TagNewModel(tagArr.optJSONObject(i)));
            }
        }
    }



}
