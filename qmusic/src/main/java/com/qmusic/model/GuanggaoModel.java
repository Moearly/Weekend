package com.qmusic.model;

import org.json.JSONException;
import org.json.JSONObject;

public class GuanggaoModel {
    public String address;
    public String des;
    public String photo;
    public String tagname;
    public int tagtype;
    public String title;
    public int type;

    public GuanggaoModel(JSONObject json) {
        parse(json);
    }

    public void parse(JSONObject json) {
        try {
            this.type = json.optInt("guanggao_type");
            if (this.type == 0) {
                this.address = json.optString("guanggao_address");
            } else if (this.type == 1) {
                JSONObject addJson = new JSONObject(json.optString("guanggao_address"));
                this.tagtype = addJson.optInt("tagtype");
                this.tagname = addJson.optString("tagname");
            } else if (this.type == 2) {
                this.address = json.optString("guanggao_address");
            } else if (this.type == 3) {
                this.address = json.optString("guanggao_address");
            }
            this.title = json.optString("guanggao_title");
            this.photo = json.optString("guanggao_photo");
            this.des = json.optString("guanggao_abstract");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        return "GuanggaoModel [type=" + this.type + ", address=" + this.address + ", title=" + this.title + ", photo=" + this.photo + ", tagtype=" + this.tagtype + ", tagname=" + this.tagname + ", des=" + this.des + "]";
    }
}