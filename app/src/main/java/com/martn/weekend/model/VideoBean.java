package com.martn.weekend.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Title: Weekend
 * Package: com.martn.weekend.model
 * Description: ("视频")
 * Date 2016/4/18 14:42
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class VideoBean implements Serializable {
    private static final long serialVersionUID = 221134925499155352L;
    public int videoid;
    public String videoname;
    public String videopicurl;
    public String videovidurl;

    public void parse(JSONObject json) {
        this.videoid = json.optInt("videoid", -100);
        this.videoname = json.optString("videoname", "");
        this.videopicurl = json.optString("videopicurl", "");
        this.videovidurl = json.optString("videovidurl", "");
    }
}