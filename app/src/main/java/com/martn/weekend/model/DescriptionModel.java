package com.martn.weekend.model;

import java.io.Serializable;


import org.json.JSONObject;

/**
 * Title: Weekend
 * Package: com.martn.weekend.model
 * Description: ("描述")
 * Date 2014/10/5 23:00
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class DescriptionModel implements Serializable{
    private static final long serialVersionUID = 7951525516080359378L;
    public String content;
    public String title;

    public DescriptionModel(JSONObject json) {
        parse(json);
    }

    public void parse(JSONObject json) {
        this.title = json.optString("course_description_title");
        this.content = json.optString("course_description_content");
    }

    public String toString() {
        return "DescriptionModel [title=" + this.title + ", content=" + this.content + "]";
    }

}
