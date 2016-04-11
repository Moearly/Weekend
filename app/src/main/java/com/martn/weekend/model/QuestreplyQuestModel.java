package com.martn.weekend.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Title: Weekend
 * Package: com.martn.weekend.model
 * Description: ("问题")
 * Date 2014/10/5 23:04
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class QuestreplyQuestModel implements Serializable{
    private static final long serialVersionUID = 279130879233892676L;
    public String questContent;
    public int questCourseId;
    public String questCourseTitle;
    public long questDatetime;
    public String questUserNickname;
    public String questUserPhoto;
    public int questUserid;

    public void parse(JSONObject json) {
        this.questUserPhoto = json.optString("quest_user_photo");
        this.questContent = json.optString("quest_content");
        this.questUserNickname = json.optString("quest_user_nickname");
        this.questUserid = json.optInt("quest_userid");
        this.questDatetime = json.optLong("quest_datetime");
        this.questCourseTitle = json.optString("quest_course_title");
        this.questCourseId = json.optInt("quest_course_id");
    }

    public String toString() {
        return "QuestreplyQuestModel [questUserPhoto=" + this.questUserPhoto + ", questContent=" + this.questContent + ", questUserNickname=" + this.questUserNickname + ", questUserid=" + this.questUserid + ", questDatetime=" + this.questDatetime + "]";
    }

}
