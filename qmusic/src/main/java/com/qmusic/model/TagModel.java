package com.qmusic.model;

import android.support.v4.widget.ExploreByTouchHelper;
import org.json.JSONObject;

public class TagModel {
    public int id;
    public String name;

    public TagModel(JSONObject json) {
        parse(json);
    }

    public void parse(JSONObject json) {
        this.id = json.optInt("tag_id", ExploreByTouchHelper.INVALID_ID);
        this.name = json.optString("tag_name", "");
    }
}