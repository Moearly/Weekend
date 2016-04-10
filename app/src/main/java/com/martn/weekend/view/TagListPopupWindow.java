package com.martn.weekend.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.martn.weekend.R;
import com.martn.weekend.adapter.TagsListAdapter;
import com.martn.weekend.result.TagsResult;
import com.qmusic.uitls.AppUtils;
import com.socks.library.KLog;

/**
 * Title: Weekend
 * Package: com.martn.weekend.view
 * Description: ("请描述功能")
 * Date 2014/10/5 11:26
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class TagListPopupWindow extends PopupWindow {
    TagsListAdapter adapter;
    private TagListDialogCallback callback;
    private Context context;
    private int tagId;
    private TagsResult tagsResult;
    private View view;

    public TagListPopupWindow(Context context, TagListDialogCallback callback) {
        super(context);
        tagId = 0;
        this.context = context;
        this.callback = callback;
        view = View.inflate(context, R.layout.dialog_tags, null);
        initView();
    }

    private void initView() {
        setContentView(view);
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setAnimationStyle(android.R.style.Animation_Translucent);//16973827
        setBackgroundDrawable(new ColorDrawable(ViewCompat.MEASURED_SIZE_MASK));
        setInputMethodMode(1);
        setSoftInputMode(16);
    }

    private void setupListView() {
        if (tagsResult.tagList != null && tagsResult.tagList.size() > 0) {
            adapter = new TagsListAdapter(context, tagsResult.tagList);
            final ListView listview = (ListView) view.findViewById(R.id.listview);
            adapter.setTagId(tagId);
            listview.setAdapter(adapter);
            listview.post(new Runnable() {
                @Override
                public void run() {
                    int i = 7;
                    View listItem = View.inflate(context, R.layout.item_tags_list, null);
                    if (listItem != null) {
                        listItem.measure(0, 0);
                        int measuredHeight = listItem.getMeasuredHeight();
                        if (tagsResult.tagList.size() <= 7) {
                            i = tagsResult.tagList.size();
                        }
                        int height = (i * measuredHeight) + listview.getDividerHeight();
                        LayoutParams params = listview.getLayoutParams();
                        params.height = height;
                        listview.setLayoutParams(params);
                    }
                }
            });
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    KLog.e("position : " + position);
                    callback.click(adapter.getTagId(position));
                    adapter.setTagId(adapter.getTagId(position));
                    dismiss();
                }
            });
        }
    }

    public void show(View locationIV) {
        showAsDropDown(locationIV, 0, (int) (((float) (-AppUtils.getScreenHeight())) * 0.01f));
    }

    public void dismiss() {
        callback.dismiss();
        super.dismiss();
    }

    public void setTagsResult(TagsResult result) {
        tagsResult = result;
        setupListView();
    }

    public void setTagId(int id) {
        tagId = id;
        if (adapter != null) {
            adapter.setTagId(id);
        }
    }

    public interface TagListDialogCallback {
        void click(int i);

        void dismiss();
    }

}
