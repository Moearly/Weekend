package com.martn.weekend.result;


import com.martn.weekend.model.QuestreplyModel;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Title: Weekend
 * Package: com.martn.weekend.result
 * Description: ("请描述功能")
 * Date 2016/4/18 17:26
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class MoreQuestResult {
    public String code;
    public List<QuestreplyModel> courseQuestreplyList;
    public String description;

    public MoreQuestResult() {
        this.courseQuestreplyList = new ArrayList();
    }

    public void parse(JSONObject json) {
        this.code = json.optString("code");
        this.description = json.optString("description");
        JSONArray courseQuestreplyArr = json.optJSONArray("course_questreply_arr");
        for (int i = 0; i < courseQuestreplyArr.length(); i++) {
            JSONObject questreplyJson = courseQuestreplyArr.optJSONObject(i);
            QuestreplyModel questreplyModel = new QuestreplyModel();
            questreplyModel.parse(questreplyJson);
            this.courseQuestreplyList.add(questreplyModel);
        }
    }

}
