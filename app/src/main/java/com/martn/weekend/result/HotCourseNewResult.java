package com.martn.weekend.result;

import com.martn.weekend.model.TcrModel;
import com.qmusic.result.BaseResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: Weekend
 * Package: com.martn.weekend.result
 * Description: ("热门活动")
 * Date 2014/10/5 23:16
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class HotCourseNewResult extends BaseResult {
    public String courseHotType;
    public List<TcrModel> tcrList;

    public HotCourseNewResult(JSONObject json) {
        super.bparse(json);
        parse(json);
    }

    private void parse(JSONObject json) {
        this.courseHotType = json.optString("coursehottype", "推荐活动");
        JSONArray tcrArr = json.optJSONArray("tcrarr");
        if (tcrArr != null) {
            this.tcrList = new ArrayList();
            for (int i = 0; i < tcrArr.length(); i++) {
                this.tcrList.add(new TcrModel(tcrArr.optJSONObject(i)));
            }
        }
    }
}
