package com.martn.weekend.model;

import org.json.JSONObject;

/**
 * Title: Weekend
 * Package: com.martn.weekend.model
 * Description: ("新的标签")
 * Date 2014/10/5 22:28
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class TagNewModel {
    public int id;
    public String name;

    public TagNewModel(JSONObject json) {
        parse(json);
    }

    private void parse(JSONObject json) {
        this.id = json.optInt("tag_id");
        this.name = json.optString("tag_name");
    }

}
