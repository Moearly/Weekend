package com.martn.weekend.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Title: Weekend
 * Package: com.martn.weekend.model
 * Description: ("公告--顾问")
 * Date 2014/10/5 22:56
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class AdvisoryModel implements Serializable {
    private static final long serialVersionUID = -1717433365743195858L;
    public String desc;
    public String mobile;

    public AdvisoryModel(JSONObject json) {
        parse(json);
    }

    public void parse(JSONObject json) {
        this.mobile = json.optString("advisory_teacher_mobile");
        this.desc = json.optString("advisory_teacher_mobile_desc");
    }
}
