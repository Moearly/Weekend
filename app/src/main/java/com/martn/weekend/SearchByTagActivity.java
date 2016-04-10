package com.martn.weekend;

import android.content.Context;
import android.content.Intent;

import com.martn.weekend.base.BaseActivity;

/**
 * Title: Weekend
 * Package: com.martn.weekend
 * Description: ("请描述功能")
 * Date 2014/10/5 23:57
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class SearchByTagActivity extends BaseActivity {

    public static final int FROM_TYPE_HOT = 1;
    public static final int FROM_TYPE_OTHER = 0;


    public static void comeBady(Context context, int id, String name, int fromtype, int tagid) {
        Intent intent = new Intent(context, SearchByTagActivity.class);
        intent.putExtra("coursetype", id);
        intent.putExtra("title", name);
        intent.putExtra("fromtype", fromtype);
        intent.putExtra("tagid", tagid);
        context.startActivity(intent);
    }

}
