package com.martn.weekend;

import android.content.Context;
import android.content.Intent;

import com.martn.weekend.base.BaseActivity;

/**
 * Title: Weekend
 * Package: com.martn.weekend
 * Description: ("消息中心")
 * Date 2014/10/5 23:24
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class MessageActivity extends BaseActivity{
    public static void comeBady(Context context) {
        context.startActivity(new Intent(context, MessageActivity.class));
    }
}
