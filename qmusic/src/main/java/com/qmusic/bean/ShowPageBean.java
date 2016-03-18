package com.qmusic.bean;

import com.socks.library.KLog;

import org.json.JSONObject;

public class ShowPageBean {
    public String address;
    public long endTime;
    public String id;
    public String photo;
    public long startTime;
    public String tagname;
    public int tagtype;
    public int timeout;
    public int type;
    public String url;

    public void parse(JSONObject json) {
        type = json.optInt("showpage_type", -1);
        switch (type) {
            case 0 /*0*/:
            case 3 /*3*/:
                id = json.optString("showpage_address", "");
                break;
            case 1:
                try {
                    JSONObject addressJson = new JSONObject(json.optString("showpage_address", ""));
                    tagtype = addressJson.optInt("tagtype", -1);
                    tagname = addressJson.optString("tagname", "");
                    break;
                } catch (Exception e) {
                    KLog.e("addressJson Exception : " + e.toString());
                    break;
                }
            case 2 /*2*/:
                url = json.optString("showpage_address", "");
                break;
        }
        photo = json.optString("showpage_photo", "");
        timeout = json.optInt("showpage_timeout", 3);
        startTime = json.optLong("showpage_starttime", -1);
        endTime = json.optLong("showpage_endtime", -1);
    }
}