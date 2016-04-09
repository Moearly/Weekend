package com.martn.weekend;

import android.content.Context;
import android.content.Intent;

import com.martn.weekend.base.BaseActivity;

/**
 * Title: Weekend
 * Package: com.martn.weekend
 * Description: ("搜索")
 * Date 2014/10/5 23:08
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class SearchClassActivity extends BaseActivity {




    public static void comeBady(Context context, String tagName, int inway) {
        Intent intent = new Intent(context, SearchClassActivity.class);
        intent.putExtra("tagname", tagName);
        intent.putExtra("inway", inway);
        context.startActivity(intent);
    }

}
