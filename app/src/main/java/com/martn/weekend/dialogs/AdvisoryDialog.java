package com.martn.weekend.dialogs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.martn.weekend.R;
import com.martn.weekend.model.AdvisoryModel;
import com.martn.weekend.view.CusTextView;
import com.qmusic.uitls.AppUtils;

import java.util.List;

/**
 * Title: Weekend
 * Package: com.martn.weekend.dialogs
 * Description: ("活动公告")
 * Date 2014/10/5 11:02
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class AdvisoryDialog extends DialogFragment {
    LinearLayout advisoryLayout;
    LayoutParams ivParams;
    List<AdvisoryModel> list;
    LayoutParams tvParams;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvParams = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ivParams = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ivParams.height = 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_advisory, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        initView();
        return view;
    }

    private void initView() {
        findViewById();
        setupAdvisoryLayout();
    }

    private void setupAdvisoryLayout() {
        if (list != null && !list.isEmpty()) {
            advisoryLayout.removeAllViews();
            for (int i = 0; i < list.size(); i++) {
                CusTextView textView = new CusTextView(getActivity());
                textView.setText(list.get(i).desc + ":" + list.get(i).mobile);
                textView.setPadding(20, 20, 20, 20);
                textView.setTypeface(AppUtils.getTypefaceZiTi());
                textView.setSingleLine(true);
                textView.setTextSize(17.0f);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_phone, 0, 0, 0);
                textView.setCompoundDrawablePadding(10);
                textView.setGravity(16);
                textView.setLayoutParams(tvParams);
                textView.setEllipsize(TruncateAt.MARQUEE);
                textView.setTag(Integer.valueOf(i));
                advisoryLayout.addView(textView);
                ImageView imageview = new ImageView(getActivity());
                imageview.setBackgroundColor(getResources().getColor(R.color.main_text_gray));
                imageview.setLayoutParams(ivParams);
                advisoryLayout.addView(imageview);
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(
                                "android.intent.action.CALL",
                                Uri.parse("tel:" + list.get(Integer.parseInt(v.getTag().toString())).mobile)));
                    }
                });
            }
        }
    }

    private void findViewById() {
        advisoryLayout = (LinearLayout) view.findViewById(R.id.advisory_layout);
    }

    public void setInfo(List<AdvisoryModel> list) {
        this.list = list;
    }
}
