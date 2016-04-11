package com.martn.weekend.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Title: Weekend
 * Package: com.martn.weekend.model
 * Description: ("提示")
 * Date 2014/10/5 23:01
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class HintModel implements Serializable {
    private static final long serialVersionUID = 2274786810055697414L;
    public String name;
    public String photo;

    public HintModel(JSONObject json) {
        parse(json);
    }

    private void parse(JSONObject json) {
        this.name = json.optString("course_hint_name");
        this.photo = json.optString("course_hint_photo");
    }
}

