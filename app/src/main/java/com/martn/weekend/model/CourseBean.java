package com.martn.weekend.model;

import org.json.JSONObject;

/**
 * Title: Weekend
 * Package: com.martn.weekend.model
 * Description: ("课程")
 * Date 2016/4/18 14:40
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class CourseBean {
    public static final int TYPE_ATTENTION = 3;
    public static final int TYPE_OVER = 1;
    public static final int TYPE_TAKE = 2;
    public int id;
    public String photo;
    public String title;

    public void parse(JSONObject json, int type) {
        switch (type) {
            case TYPE_OVER :
                this.id = json.optInt("over_course_id", 0);
                this.photo = json.optString("over_course_photo");
                this.title = json.optString("over_course_title");
                break;
            case TYPE_TAKE:
                this.id = json.optInt("takepart_course_id", 0);
                this.photo = json.optString("takepart_course_photo");
                this.title = json.optString("takepart_course_title");
                break;
            case TYPE_ATTENTION:
                this.id = json.optInt("collect_course_id", 0);
                this.photo = json.optString("collect_course_photo");
                this.title = json.optString("collect_course_title");
                break;
            default:
        }
    }

}
