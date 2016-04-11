package com.martn.weekend.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Title: Weekend
 * Package: com.martn.weekend.model
 * Description: ("价格")
 * Date 2014/10/5 23:02
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class PriceModel implements Serializable {
    private static final long serialVersionUID = -7863336811444597292L;
    public int count;
    public String desc;
    public int id;
    public String once;

    public PriceModel(JSONObject json) {
        parse(json);
    }

    private void parse(JSONObject json) {
        this.id = json.optInt("course_price_id");
        this.once = json.optString("course_price_once");
        this.desc = json.optString("course_price_once_desc");
        this.count = json.optInt("course_price_once_havecount", 0);
    }
}
