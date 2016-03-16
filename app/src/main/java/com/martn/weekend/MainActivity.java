package com.martn.weekend;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.martn.weekend.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * Title: Weekend
 * Package: com.martn.weekend
 * Description: ("请描述功能")
 * Date 2016/3/16 16:12
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
}
