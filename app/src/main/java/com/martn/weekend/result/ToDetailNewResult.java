package com.martn.weekend.result;

import com.martn.weekend.model.CourseDetailModel;
import com.qmusic.result.BaseResult;

import org.json.JSONObject;

import java.io.Serializable;

public class ToDetailNewResult extends BaseResult implements Serializable {
    public String code;
    public CourseDetailModel courseDetail;
    public String description;

    public ToDetailNewResult(JSONObject json) {
        super.bparse(json);
        parse(json);
    }

    private void parse(JSONObject json) {
        this.code = json.optString("code");
        this.description = json.optString("description");
        this.courseDetail = new CourseDetailModel(json);
    }
}