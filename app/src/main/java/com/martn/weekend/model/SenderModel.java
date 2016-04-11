package com.martn.weekend.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Title: Weekend
 * Package: com.martn.weekend.model
 * Description: ("寄件人")
 * Date 2014/10/5 23:06
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class SenderModel implements Serializable{
    private static final long serialVersionUID = 2936836518102522826L;
    public String huanxinUsername;
    public int id;
    public String nickname;
    public String oneabstract;
    public String photo;
    public String type;

    public SenderModel(JSONObject json) {
        this.id = json.optInt("course_teacher_id");
        this.nickname = json.optString("course_teacher_nickname");
        this.type = json.optString("course_teacher_type");
        this.photo = json.optString("course_teacher_photo");
        this.oneabstract = json.optString("course_teacher_oneabstract");
        this.huanxinUsername = json.optString("course_teacher_huanxin_username");
    }

}
