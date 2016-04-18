package com.martn.weekend.result;

import com.martn.weekend.model.UserfaveBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: Weekend
 * Package: com.martn.weekend.result
 * Description: ("参加活动的人")
 * Date 2016/4/18 15:43
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class UserForCoureFaveResult {
    public String code;
    public List<UserfaveBean> courseUserfaveArr;
    public String description;

    public UserForCoureFaveResult() {
        this.courseUserfaveArr = new ArrayList();
    }

    public void parse(JSONObject json) {
        this.code = json.optString("code");
        this.description = json.optString("description");
        JSONArray courseUserfaveJSONArr = json.optJSONArray("course_userfave_arr");
        if (courseUserfaveJSONArr != null) {
            for (int i = 0; i < courseUserfaveJSONArr.length(); i++) {
                UserfaveBean bean = new UserfaveBean();
                bean.parse(courseUserfaveJSONArr.optJSONObject(i));
                this.courseUserfaveArr.add(bean);
            }
        }
    }

}
