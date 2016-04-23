package com.martn.weekend.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.martn.weekend.R;
import com.martn.weekend.share.FriendAndZoneShare;
import com.martn.weekend.share.SinaShare;
import com.qmusic.common.Common;
import com.qmusic.uitls.Helper;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;

/**
 * Title: Weekend
 * Package: com.martn.weekend.view
 * Description: ("分享弹出框")
 * Date 2014/10/5 23:51
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class SharePopupWindow extends PopupWindow implements OnClickListener {
    private Context context;
    private Bitmap shareBit;
    private String shareSinaBlogContent;
    private String shareSinaBlogTitle;
    private String shareSinaBlogUrl;
    private String shareWxFriendContent;
    private String shareWxFriendTitle;
    private String shareWxFriendUrl;
    private String shareWxFriendZoneContent;
    private String shareWxFriendZoneTitle;
    private String shareWxFriendZoneUrl;
    private View view;

    public SharePopupWindow(Context context) {
        super(context);
        this.shareWxFriendUrl = "";
        this.shareWxFriendTitle = "";
        this.shareWxFriendContent = "";
        this.shareWxFriendZoneUrl = "";
        this.shareWxFriendZoneTitle = "";
        this.shareWxFriendZoneContent = "";
        this.shareSinaBlogUrl = "";
        this.shareSinaBlogTitle = "";
        this.shareSinaBlogContent = "";
        this.shareBit = null;
        this.view = View.inflate(context, R.layout.dialog_share, null);
        setContentView(this.view);
        this.context = context;
        initView(null);
    }

    public SharePopupWindow(Context context, OnClickListener itemsOnClick) {
        super(context);
        this.shareWxFriendUrl = "";
        this.shareWxFriendTitle = "";
        this.shareWxFriendContent = "";
        this.shareWxFriendZoneUrl = "";
        this.shareWxFriendZoneTitle = "";
        this.shareWxFriendZoneContent = "";
        this.shareSinaBlogUrl = "";
        this.shareSinaBlogTitle = "";
        this.shareSinaBlogContent = "";
        this.shareBit = null;
        this.view = View.inflate(context, R.layout.dialog_share, null);
        setContentView(this.view);
        this.context = context;
        initView(itemsOnClick);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_textview:
                dismiss();
            case R.id.weixin_textview:
                FriendAndZoneShare toWeixinShare = new FriendAndZoneShare(this.context, false);
                toWeixinShare.setInformation(this.shareWxFriendTitle, this.shareWxFriendContent, this.shareWxFriendUrl);
                toWeixinShare.setBitmap(this.shareBit);
                toWeixinShare.sendUrlLinkReq(1);
                break;
            case R.id.friend_zone_textview:
                FriendAndZoneShare toFriendZoneShare = new FriendAndZoneShare(this.context, false);
                toFriendZoneShare.setInformation(this.shareWxFriendZoneTitle, this.shareWxFriendZoneContent, this.shareWxFriendZoneUrl);
                toFriendZoneShare.setBitmap(this.shareBit);
                toFriendZoneShare.sendUrlLinkReq(0);
            case R.id.sina_textview:
                IWeiboShareAPI mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this.context, Common.Share.SINA_APP_KEY);
                mWeiboShareAPI.registerApp();
                if (mWeiboShareAPI.isWeiboAppInstalled()) {
                    SinaShare sinaShare = new SinaShare(this.context, mWeiboShareAPI);
                    sinaShare.setInformation(this.shareSinaBlogContent, this.shareSinaBlogContent, this.shareSinaBlogContent, this.shareSinaBlogUrl);
                    sinaShare.setBitmap(this.shareBit);
                    sinaShare.sendMultiMessage();
                    return;
                }
                Helper.showToast("您还未安装新浪微博App");
            default:
        }
    }

    private void initView(OnClickListener itemsOnClick) {
        if (itemsOnClick != null) {
            view.findViewById(R.id.sina_textview).setOnClickListener(itemsOnClick);
            view.findViewById(R.id.weixin_textview).setOnClickListener(itemsOnClick);
            view.findViewById(R.id.friend_zone_textview).setOnClickListener(itemsOnClick);
        } else {
            view.findViewById(R.id.sina_textview).setOnClickListener(this);
            view.findViewById(R.id.weixin_textview).setOnClickListener(this);
            view.findViewById(R.id.friend_zone_textview).setOnClickListener(this);
        }
        this.view.findViewById(R.id.cancel_textview).setOnClickListener(this);
        this.view.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = SharePopupWindow.this.view.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == 1 && y < height) {
                    SharePopupWindow.this.dismiss();
                }
                return true;
            }
        });
        setWidth(-1);
        setHeight(-2);
        setFocusable(true);
        setAnimationStyle(R.style.AnimBottom);
//        setBackgroundDrawable(new ColorDrawable(-1342177280));
        setInputMethodMode(1);
        setSoftInputMode(16);
    }

    public void setItemOnClickListener(OnClickListener itemsOnClick) {
        view.findViewById(R.id.sina_textview).setOnClickListener(itemsOnClick);
        view.findViewById(R.id.weixin_textview).setOnClickListener(itemsOnClick);
        view.findViewById(R.id.friend_zone_textview).setOnClickListener(itemsOnClick);
    }

    public void show(View locationIV) {
        showAtLocation(locationIV, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public void setShareUrl(String shareUrl) {
        this.shareWxFriendUrl = shareUrl;
        this.shareWxFriendZoneUrl = shareUrl;
        this.shareSinaBlogUrl = shareUrl;
    }

    public void setShareTitle(String shareTitle) {
        this.shareWxFriendTitle = shareTitle;
        this.shareWxFriendZoneTitle = shareTitle;
        this.shareSinaBlogTitle = shareTitle;
    }

    public void setShareContent(String shareContent) {
        this.shareWxFriendContent = shareContent;
        this.shareWxFriendZoneContent = shareContent;
        this.shareSinaBlogContent = shareContent;
    }

    public void setShareWxFriendUrl(String shareWxFriendUrl) {
        this.shareWxFriendUrl = shareWxFriendUrl;
    }

    public void setShareWxFriendTitle(String shareWxFriendTitle) {
        this.shareWxFriendTitle = shareWxFriendTitle;
    }

    public void setShareWxFriendContent(String shareWxFriendContent) {
        this.shareWxFriendContent = shareWxFriendContent;
    }

    public void setShareWxFriendZoneUrl(String shareWxFriendZoneUrl) {
        this.shareWxFriendZoneUrl = shareWxFriendZoneUrl;
    }

    public void setShareWxFriendZoneTitle(String shareWxFriendZoneTitle) {
        this.shareWxFriendZoneTitle = shareWxFriendZoneTitle;
    }

    public void setShareWxFriendZoneContent(String shareWxFriendZoneContent) {
        this.shareWxFriendZoneContent = shareWxFriendZoneContent;
    }

    public void setShareSinaBlogUrl(String shareSinaBlogUrl) {
        this.shareSinaBlogUrl = shareSinaBlogUrl;
    }

    public void setShareSinaBlogTitle(String shareSinaBlogTitle) {
        this.shareSinaBlogTitle = shareSinaBlogTitle;
    }

    public void setShareSinaBlogContent(String shareSinaBlogContent) {
        this.shareSinaBlogContent = shareSinaBlogContent;
    }

    public void setShareBitmap(Bitmap bitmap) {
        this.shareBit = bitmap;
    }
}