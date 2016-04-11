package com.martn.weekend.model;
import android.widget.TextView;

import com.martn.weekend.R;

import java.io.Serializable;
import org.json.JSONObject;

/**
 * Title: Weekend
 * Package: com.martn.weekend.model
 * Description: ("评级")
 * Date 2014/10/5 22:57
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class AssessModel implements Serializable {
    private static final long serialVersionUID = 8563307393083405148L;
    public String assessContent;
    public int assessLevel;
    public String assessTime;
    public int assessUserId;
    public String assessUserNickname;
    public String assessUserPhoto;

    public AssessModel(JSONObject json) {
        parse(json);
    }

    public void parse(JSONObject json) {
        this.assessLevel = json.optInt("assess_level", 0);
        this.assessUserPhoto = json.optString("assess_user_photo", "");
        this.assessUserNickname = json.optString("assess_user_nickname", "");
        this.assessTime = json.optString("assess_time", "");
        this.assessContent = json.optString("assess_content", "");
        this.assessUserId = json.optInt("assess_user_id", -100);
    }

    public static void setupAssessTV(TextView tv, int starNum) {
        switch (starNum) {
            case 1:
            case 2:
                tv.setText("差评");
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_assess_bad, 0, 0, 0);
                break;
            case 3 /*3*/:
                tv.setText("中评");
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_assess_normal, 0, 0, 0);
                break;
            case 4 /*4*/:
            case 5 /*5*/:
                tv.setText("好评");
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_assess_good, 0, 0, 0);
                break;
            default:
        }
    }
}