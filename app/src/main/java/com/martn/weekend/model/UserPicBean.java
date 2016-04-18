package com.martn.weekend.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Title: Weekend
 * Package: com.martn.weekend.model
 * Description: ("用户图片")
 * Date 2016/4/18 14:41
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class UserPicBean implements Serializable {
    private static final long serialVersionUID = 4738962622604728978L;
    public String picbigurl;
    public int picid;
    public String picmiddleurl;
    public String picname;
    public String picsmallurl;

    public void parse(JSONObject json) {
        this.picid = json.optInt("picid", -100);
        this.picname = json.optString("picname", "");
        this.picsmallurl = json.optString("picsmallurl", "");
        this.picmiddleurl = json.optString("picmiddleurl", "");
        this.picbigurl = json.optString("picbigurl", "");
    }
}
