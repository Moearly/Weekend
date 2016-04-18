package com.martn.weekend.model;

import org.json.JSONObject;

/**
 * Title: Weekend
 * Package: com.martn.weekend.model
 * Description: ("请描述功能")
 * Date 2016/4/18 15:43
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class UserfaveBean {
    public String age;
    public int id;
    public String nickname;
    public String oneabstract;
    public String photo;
    public int sex;
    public int status;
    public String statusDesc;

    public void parse(JSONObject json) {
        this.id = json.optInt("course_faveuser_id");
        this.photo = json.optString("course_faveuser_photo", "");
        this.nickname = json.optString("course_faveuser_nickname", "");
        this.age = json.optString("course_faveuser_age", "");
        this.sex = json.optInt("course_faveuser_sex", 0);
        this.oneabstract = json.optString("course_faveuser_oneabstract", "");
        this.status = json.optInt("course_faveuser_status", -100);
        this.statusDesc = json.optString("course_faveuser_status_desc", "");
    }

}
