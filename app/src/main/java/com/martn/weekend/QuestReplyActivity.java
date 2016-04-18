package com.martn.weekend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.martn.weekend.base.BaseActivity;
import com.martn.weekend.fragment.QuestReplyFragment;
import com.qmusic.common.Common;

import java.lang.ref.WeakReference;

/**
 * Title: Weekend
 * Package: com.martn.weekend
 * Description: ("请描述功能")
 * Date 2016/4/18 16:44
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class QuestReplyActivity extends BaseActivity implements View.OnClickListener {
    private int courseid;
    private Fragment fragment;
    private final QuestReplyFragment questReplyFragment = new QuestReplyFragment();
    private final QuestReplyHandler questReplyHandler = new QuestReplyHandler(this);

    static class QuestReplyHandler extends Handler {
        WeakReference<QuestReplyActivity> mActivity;

        public QuestReplyHandler(QuestReplyActivity activity) {
            this.mActivity = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            QuestReplyActivity questReplyActivity = mActivity.get();
            if (questReplyActivity != null) {
                switch (msg.what) {
                    case 10:
                        questReplyActivity.changeFragment(msg.what);
                    default:
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_common);
        this.courseid = getIntent().getIntExtra(Common.Key.COURSE_ID, 0);
        initView();
        this.questReplyHandler.sendEmptyMessage(10);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topbar_left_textview:
                finish();
            default:
        }
    }

    private void initView() {
        findViewById();
    }

    private void findViewById() {
        ((TextView) findViewById(R.id.topbar_mid_textview)).setText(R.string.info_quest);
    }

    private void changeFragment(int type) {
        switch (type) {
            case 10:
                fragment = questReplyFragment;
                Bundle b = new Bundle();
                b.putInt(Common.Key.COURSE_ID, this.courseid);
                fragment.setArguments(b);
                break;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, this.fragment);
        ft.commit();
    }

    public static void comeBady(Context context, int courseid) {
        Intent intent = new Intent(context, QuestReplyActivity.class);
        intent.putExtra(Common.Key.COURSE_ID, courseid);
        context.startActivity(intent);
    }
}
