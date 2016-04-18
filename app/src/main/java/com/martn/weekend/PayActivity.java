package com.martn.weekend;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.martn.weekend.base.BaseActivity;
import com.qmusic.common.Common;

/**
 * Title: Weekend
 * Package: com.martn.weekend
 * Description: ("支付")
 * Date 2016/4/18 18:01
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class PayActivity extends BaseActivity implements View.OnClickListener {


    public static void comeBady(Context context, int courseid, int priceid) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra(Common.Key.COURSE_ID, courseid);
        intent.putExtra("priceid", priceid);
        context.startActivity(intent);
    }

}
