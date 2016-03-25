package com.qmusic.model;

import org.json.JSONObject;

/**
 * Title: Weekend
 * Package: com.qmusic.model
 * Description: ("请描述功能")
 * Date 2016/3/25 10:12
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class TcrModel {
    public int collectcount;
    public int courseHavecount;
    public int courseIsover;
    public String courseTime;
    public String coursedistance;
    public int courseid;
    public String courseprice;
    public String coursesite;
    public String headphoto;
    public int iscollect;
    public String oldCourseprice;
    public String suffix;
    public String title;

    public TcrModel() {
        this.courseHavecount = 0;
    }

    public TcrModel(JSONObject json) {
        this.courseHavecount = 0;
        parse(json);
    }

    public void parse(JSONObject json) {
        this.headphoto = json.optString("headphoto");
        this.title = json.optString("title");
        this.courseTime = json.optString("course_time");
        this.coursesite = json.optString("coursesite");
        this.coursedistance = json.optString("coursedistance");
        this.collectcount = json.optInt("collectcount", 0);
        this.courseid = json.optInt("courseid");
        this.courseHavecount = json.optInt("course_havecount");
        this.iscollect = json.optInt("iscollect");
        this.courseprice = json.optString("courseprice");
        this.oldCourseprice = json.optString("old_courseprice");
        this.courseIsover = json.optInt("course_isover", 0);
        this.suffix = json.optString("courseprice_suffix");
    }

    public String toString() {
        return "TcrModel [headphoto=" + this.headphoto + ", title=" + this.title + ", coursesite=" + this.coursesite + ", coursedistance=" + this.coursedistance + ", collectcount=" + this.collectcount + ", courseid=" + this.courseid + ", courseHavecount=" + this.courseHavecount + ", iscollect=" + this.iscollect + ", courseprice=" + this.courseprice + ", oldCourseprice=" + this.oldCourseprice + ", courseTime=" + this.courseTime + ", courseIsover=" + this.courseIsover + ", suffix=" + this.suffix + "]";
    }
}
