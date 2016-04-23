package com.martn.weekend.result;

import com.martn.weekend.model.TcrModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: Weekend
 * Package: com.martn.weekend.result
 * Description: ("请描述功能")
 * Date 2014/10/5 14:46
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class SeachByTagResult {
    public String code;
    public List<TcrModel> courseUserList;
    public String description;
    public int teacherId;
    public int teacherIscollect;
    public int teacherIsown;
    public String teacherNickname;
    public String teacherOneabtract;
    public String teacherPhoto;
    public String teacherType;

    public SeachByTagResult() {
        this.courseUserList = new ArrayList();
    }

    public String toString() {
        return "SeachByTagResult [code=" + this.code + ", description=" + this.description + ", teacherId=" + this.teacherId + ", teacherType=" + this.teacherType + ", teacherNickname=" + this.teacherNickname + ", teacherOneabtract=" + this.teacherOneabtract + ", teacherIscollect=" + this.teacherIscollect + ", teacherPhoto=" + this.teacherPhoto + ", courseUserList=" + this.courseUserList + "]";
    }

}
