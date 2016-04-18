package com.martn.weekend.result;

import com.martn.weekend.model.CourseBean;
import com.martn.weekend.model.UserPicBean;
import com.martn.weekend.model.VideoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Title: Weekend
 * Package: com.martn.weekend.result
 * Description: ("其他用户的详情信息")
 * Date 2016/4/18 14:40
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class GetOtherUserDetailV3Result { public String code;
    public List<CourseBean> collectCourseArr;
    public String description;
    public List<UserPicBean> piclist;
    public String teacherAge;
    public int teacherCollectcourseCount;
    public int teacherCollectmeCount;
    public String teacherConstellation;
    public String teacherHuanxinUsername;
    public int teacherId;
    public int teacherIscollect;
    public int teacherIsown;
    public int teacherMycollectCount;
    public String teacherNickname;
    public String teacherOneabstract;
    public List<CourseBean> teacherOvercourseArr;
    public int teacherOvercourseCount;
    public String teacherPhoto;
    public String teacherRemark;
    public int teacherSex;
    public List<CourseBean> teacherTakecourseArr;
    public int teacherTakecourseCount;
    public String teacherType;
    public List<VideoBean> videolist;

    public GetOtherUserDetailV3Result() {
        this.teacherOvercourseArr = new ArrayList();
        this.teacherTakecourseArr = new ArrayList();
        this.collectCourseArr = new ArrayList();
        this.piclist = new ArrayList();
        this.videolist = new ArrayList();
    }

    public void parse(JSONObject json) {
        int i;
        this.code = json.optString("code");
        this.description = json.optString("description");
        this.teacherId = json.optInt("teacher_id");
        this.teacherIsown = json.optInt("teacher_isown");
        this.teacherIscollect = json.optInt("teacher_iscollect", 0);
        this.teacherPhoto = json.optString("teacher_photo");
        this.teacherSex = json.optInt("teacher_sex", 0);
        this.teacherAge = json.optString("teacher_age", "");
        this.teacherConstellation = json.optString("teacher_constellation", "");
        this.teacherNickname = json.optString("teacher_nickname");
        this.teacherType = json.optString("teacher_type");
        this.teacherOneabstract = json.optString("teacher_oneabstract");
        this.teacherRemark = json.optString("teacher_remark", "");
        this.teacherOvercourseCount = json.optInt("teacher_overcourse_count");
        this.teacherOvercourseArr = new ArrayList();
        JSONArray teacherOvercourseJsonArr = json.optJSONArray("teacher_overcourse_arr");

        if (teacherOvercourseJsonArr != null) {
            for (i = 0; i < teacherOvercourseJsonArr.length(); i++) {
                CourseBean bean = new CourseBean();
                bean.parse(teacherOvercourseJsonArr.optJSONObject(i), 1);
                this.teacherOvercourseArr.add(bean);
            }
        }
        this.teacherTakecourseCount = json.optInt("teacher_takecourse_count", 0);
        this.teacherTakecourseArr = new ArrayList();
        JSONArray teacherTakecourseJsonArr = json.optJSONArray("teacher_takecourse_arr");
        if (teacherTakecourseJsonArr != null) {
            for (i = 0; i < teacherTakecourseJsonArr.length(); i++) {
                CourseBean bean = new CourseBean();
                bean.parse(teacherTakecourseJsonArr.optJSONObject(i), 2);
                this.teacherTakecourseArr.add(bean);
            }
        }
        this.teacherCollectcourseCount = json.optInt("teacher_collectcourse_count", 0);
        this.collectCourseArr = new ArrayList();
        JSONArray collectCourseJsonArr = json.optJSONArray("collect_course_arr");
        if (collectCourseJsonArr != null) {
            for (i = 0; i < collectCourseJsonArr.length(); i++) {
                CourseBean bean = new CourseBean();
                bean.parse(collectCourseJsonArr.optJSONObject(i), 3);
                this.collectCourseArr.add(bean);
            }
        }
        this.teacherMycollectCount = json.optInt("teacher_mycollect_count");
        this.teacherCollectmeCount = json.optInt("teacher_collectme_count");
        JSONArray picArr = json.optJSONArray("userpiclist");
        if (picArr != null) {
            for (i = 0; i < picArr.length(); i++) {
                UserPicBean pic = new UserPicBean();
                pic.parse(picArr.optJSONObject(i));
                this.piclist.add(pic);
            }
        }
        JSONArray videoArr = json.optJSONArray("videolist");
        if (videoArr != null) {
            for (i = 0; i < videoArr.length(); i++) {
                VideoBean video = new VideoBean();
                video.parse(videoArr.optJSONObject(i));
                this.videolist.add(video);
            }
        }
        this.teacherHuanxinUsername = json.optString("teacher_huanxin_username", "").toLowerCase(Locale.getDefault());
    }
}
