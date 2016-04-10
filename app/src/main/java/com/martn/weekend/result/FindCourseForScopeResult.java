package com.martn.weekend.result;

import com.martn.weekend.model.TcrModel;
import com.qmusic.result.BaseResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: Weekend
 * Package: com.martn.weekend.result
 * Description: ("根据区域查询的---类容返回")
 * Date 2014/10/5 9:34
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class FindCourseForScopeResult extends BaseResult {
    public int pageCount;
    public double scopeRange;
    public double scopex;
    public double scopey;
    public List<TcrModel> tcrArr;

    public void parse(JSONObject json) {
        super.bparse(json);
        this.scopeRange = json.optDouble("scope_range", 0.0d);
        this.pageCount = json.optInt("page_count", 0);
        this.scopex = json.optDouble("scopex", 0.0d);
        this.scopey = json.optDouble("scopey", 0.0d);
        JSONArray tcrJOSNArr = json.optJSONArray("tcrarr");
        if (tcrJOSNArr != null) {
            this.tcrArr = new ArrayList();
            for (int i = 0; i < tcrJOSNArr.length(); i++) {
                TcrModel tcr = new TcrModel();
                tcr.parse(tcrJOSNArr.optJSONObject(i));
                this.tcrArr.add(tcr);
            }
        }
    }
}