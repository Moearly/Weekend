package com.martn.weekend.dialogs;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.martn.weekend.R;
import com.martn.weekend.db.UserPreference;


/**
 * Title: Weekend
 * Package: com.martn.weekend.dialogs
 * Description: ("请描述功能")
 * Date 2016/3/25 14:52
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class CityNewDialog extends DialogFragment implements View.OnClickListener {
    private final String BJ = "北京";
    private final String SH = "上海";
    ImageView ivCityBg;
    ImageView ivBj;
    ImageView ivBjJ;
    ImageView ivSh;
    ImageView ivShJ;


//    FrameLayout flBj;
//    FrameLayout flSh;
//    @Bind(R.id.rl_city)
//    RelativeLayout rlCity;

    private String city;

    float bgHeight;
    float bgWidth;
    float cancelBottom;
    float cancelLeft;
    float cancelRight;
    float cancelTop;
    float confirmBottom;
    float confirmLeft;
    float confirmRight;
    float confirmTop;

    private Handler handler;
    private View view;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.dialog_city, null);
        //ButterKnife.bind(CityNewDialog.this, view);
        initView();
        getDialog().requestWindowFeature(1);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //ButterKnife.unbind(this);
    }

    private void initView() {
        findViews();
        setupCity();
        ivCityBg.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                if (event.getAction() == 0) {
                    if (isCancel(view, x, y)) {
                        dismiss();
                        handler.sendEmptyMessage(1);
                    } else if (isConfirm(view, x, y)) {
                        dismiss();
                        handler.sendEmptyMessage(0);//更换城市后刷新界面
                        UserPreference.getInstance(getContext()).setUserCity(getCity());
                    }
                }
                return false;

            }
        });
    }

    private void findViews() {
        ivCityBg = (ImageView) view.findViewById(R.id.iv_city_bg);
        ivBj = (ImageView) view.findViewById(R.id.iv_bj);
        ivBjJ = (ImageView) view.findViewById(R.id.iv_bj_j);

        ivSh = (ImageView) view.findViewById(R.id.iv_sh);
        ivShJ = (ImageView) view.findViewById(R.id.iv_sh_j);

    }


    private boolean isCancel(View view, float x, float y) {
        if (cancelLeft == 0.0f || cancelTop == 0.0f || cancelRight == 0.0f || cancelBottom == 0.0f) {
            bgWidth = (float) view.getWidth();
            bgHeight = (float) view.getHeight();
            cancelLeft = bgWidth * 0.84f;
            cancelTop = bgHeight * 0.05f;
            cancelRight = bgWidth;
            cancelBottom = bgHeight * 0.16f;
        } else if (x < cancelLeft || cancelLeft > cancelRight || y < cancelTop || y > cancelBottom) {
            return false;
        }
        return true;
    }

    private boolean isConfirm(View view, float x, float y) {
        if (confirmLeft == 0.0f || confirmTop == 0.0f || confirmRight == 0.0f || confirmBottom == 0.0f) {
            bgWidth = (float) view.getWidth();
            bgHeight = (float) view.getHeight();
            confirmLeft = bgWidth * 0.1f;
            confirmTop = bgHeight * 0.83f;
            confirmRight = bgWidth * 0.9f;
            confirmBottom = bgHeight * 0.93f;
        } else if (x < confirmLeft || x > confirmRight || y < confirmTop || y > confirmBottom) {
            return false;
        }
        return true;
    }




    private void setupCity() {
        if (getCity().equals(BJ)) {
            ivBj.setImageResource(R.drawable.img_bj_select);
            ivBjJ.setVisibility(View.VISIBLE);
            ivSh.setImageResource(R.drawable.img_sh_unselect);
            ivShJ.setVisibility(View.INVISIBLE);
        } else if (getCity().equals(SH)) {
            ivBj.setImageResource(R.drawable.img_bj_unselect);
            ivBjJ.setVisibility(View.INVISIBLE);
            ivSh.setImageResource(R.drawable.img_sh_select);
            ivShJ.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_bj:
                city = BJ;
                setupCity();
            case R.id.iv_sh :
                city = SH;
                setupCity();
            default:
        }
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Handler getHandler() {
        return this.handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

}
