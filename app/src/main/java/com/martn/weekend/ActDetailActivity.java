package com.martn.weekend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.martn.weekend.base.BaseActivity;

/**
 * Title: Weekend
 * Package: com.martn.weekend
 * Description: ("活动详情")
 * Date 2014/10/5 12:40
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class ActDetailActivity extends BaseActivity {
    public static final String COURSE_ID = "courseid";
    public static final int REQUEST_CODE = 1232;

    public static void comeBady(Context context, int courseid) {
        Intent intent = new Intent(context, ActDetailActivity.class);
        intent.putExtra(COURSE_ID, courseid);
        context.startActivity(intent);
    }

    public static void comeBady(Context context, int courseid, int inway) {
        Intent intent = new Intent(context, ActDetailActivity.class);
        intent.putExtra(COURSE_ID, courseid);
        intent.putExtra("inway", inway);
        context.startActivity(intent);
    }

    public static void comeBadyForResult(Context context, int courseid) {
        Intent intent = new Intent(context, ActDetailActivity.class);
        intent.putExtra(COURSE_ID, courseid);
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE);
    }

}
