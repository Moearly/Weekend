package com.martn.weekend.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Title: Weekend
 * Package: com.martn.weekend.model
 * Description: ("活动详情模型")
 * Date 2014/10/5 22:54
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class CourseDetailModel implements Serializable{
    private static final long serialVersionUID = -7823843220979179072L;

    public List<AdvisoryModel> advisoryList;
    public List<AssessModel> assessContentList;
    public int assessCount;
    public List<String> assessPhotoList;
    public String datetime;
    public String description;
    public List<DescriptionModel> descriptionList;
    public String detailPlace;
    public String distance;
    public int havecount;
    public String hint;
    public List<HintModel> hintPhotoList;
    public int id;
    public int isapply;
    public String isapplydes;
    public int iscollect;
    public int isteacher;
    public String lightspotContent;
    public String lightspotTitle;
    public int newHavecount;
    public String oldPrice;
    public String photo;
    public List<String> photoList;
    public String placeX;
    public String placeY;
    public String price;
    public List<PriceModel> priceList;
    public List<QuestreplyModel> questreplyList;
    public SenderModel sender;
    public String site;
    public List<String> studentPhotoList;
    public String suffix;
    public String teacherway;
    public String title;
    public int userFaveCount;
    public List<String> userFavePhotoList;
    public String viceTitle;

    public CourseDetailModel(JSONObject json) {
        parse(json);
    }

    private void parse(JSONObject json) {
        int i;
        this.id = json.optInt("courseid");
        this.iscollect = json.optInt("course_iscollect", 0);
        this.photo = json.optString("course_photo", "");
        this.title = json.optString("course_title", "");
        this.datetime = json.optString("course_datetime", "");
        this.price = json.optString("course_price", "");
        this.oldPrice = json.optString("course_old_price", "");
        this.teacherway = json.optString("course_teacherway", "");
        this.distance = json.optString("course_distance", "");
        this.site = json.optString("course_coursesite", "");
        this.detailPlace = json.optString("course_detailplace", "");
        this.placeX = json.optString("course_place_x");
        this.placeY = json.optString("course_place_y");
        this.viceTitle = json.optString("course_vice_title", "");
        this.description = json.optString("course_description", "");
        this.hint = json.optString("course_hint", "");
        if (json.has("course_teacher_id")) {
            this.sender = new SenderModel(json);
        }
        JSONArray studentsPhotoArr = json.optJSONArray("course_students_photo_arr");
        if (studentsPhotoArr != null) {
            this.studentPhotoList = new ArrayList();
            for (i = 0; i < studentsPhotoArr.length(); i++) {
                this.studentPhotoList.add(studentsPhotoArr.optString(i));
            }
        }
        JSONArray questreplyArr = json.optJSONArray("course_questreply_arr");
        if (questreplyArr != null) {
            this.questreplyList = new ArrayList();
            for (i = 0; i < questreplyArr.length(); i++) {
                this.questreplyList.add(new QuestreplyModel(questreplyArr.optJSONObject(i)));
            }
        }
        this.isteacher = json.optInt("course_isteacher");
        this.havecount = json.optInt("course_havecount", 0);
        this.isapply = json.optInt("course_isapply", -100);
        this.isapplydes = json.optString("course_isapplydes");
        JSONArray advisoryArr = json.optJSONArray("advisory_teacher_mobile_arr");
        if (advisoryArr != null) {
            this.advisoryList = new ArrayList();
            for (i = 0; i < advisoryArr.length(); i++) {
                this.advisoryList.add(new AdvisoryModel(advisoryArr.optJSONObject(i)));
            }
        }
        this.userFaveCount = json.optInt("course_userfave_photo_count", 0);
        JSONArray userfavePhotoArr = json.optJSONArray("course_userfave_photo_arr");
        if (userfavePhotoArr != null) {
            this.userFavePhotoList = new ArrayList();
            for (i = 0; i < userfavePhotoArr.length(); i++) {
                this.userFavePhotoList.add(userfavePhotoArr.optString(i));
            }
        }
        this.assessCount = json.optInt("course_assess_count", 0);
        JSONArray assessPhotoJSONArr = json.optJSONArray("course_assessuser_photo_arr");
        if (assessPhotoJSONArr != null) {
            this.assessPhotoList = new ArrayList();
            for (i = 0; i < assessPhotoJSONArr.length(); i++) {
                this.assessPhotoList.add(assessPhotoJSONArr.optString(i));
            }
        }
        JSONArray assessContentArr = json.optJSONArray("course_assess_arr");
        if (assessContentArr != null) {
            this.assessContentList = new ArrayList();
            for (i = 0; i < assessContentArr.length(); i++) {
                this.assessContentList.add(new AssessModel(assessContentArr.optJSONObject(i)));
            }
        }
        JSONArray photoArr = json.optJSONArray("course_photo_others");
        if (photoArr != null) {
            this.photoList = new ArrayList();
            for (i = 0; i < photoArr.length(); i++) {
                this.photoList.add(photoArr.optString(i));
            }
        }
        this.lightspotTitle = json.optString("course_lightspot_type");
        this.lightspotContent = json.optString("course_lightspot");
        JSONArray descriptionArr = json.optJSONArray("course_description_arr");
        if (descriptionArr != null) {
            this.descriptionList = new ArrayList();
            for (i = 0; i < descriptionArr.length(); i++) {
                this.descriptionList.add(new DescriptionModel(descriptionArr.optJSONObject(i)));
            }
        }
        JSONArray hintArr = json.optJSONArray("course_hint_photo_arr");
        if (hintArr != null) {
            this.hintPhotoList = new ArrayList();
            for (i = 0; i < hintArr.length(); i++) {
                this.hintPhotoList.add(new HintModel(hintArr.optJSONObject(i)));
            }
        }
        this.suffix = json.optString("course_price_suffix");
        JSONArray priceArr = json.optJSONArray("course_price_arr");
        if (priceArr != null) {
            this.priceList = new ArrayList();
            for (i = 0; i < priceArr.length(); i++) {
                this.priceList.add(new PriceModel(priceArr.optJSONObject(i)));
            }
        }
        this.newHavecount = json.optInt("course_new_havecount");
    }

    public String toString() {
        return "CourseDetailModel [id=" + this.id + ", photo=" + this.photo + ", title=" + this.title + ", datetime=" + this.datetime + ", price=" + this.price + ", oldPrice=" + this.oldPrice + ", site=" + this.site + ", detailPlace=" + this.detailPlace + "]";
    }

}
