package com.martn.weekend.share;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Build;

import com.martn.weekend.R;
import com.qmusic.base.BaseApplication;
import com.qmusic.common.Common;
import com.qmusic.uitls.Helper;
import com.socks.library.KLog;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX.Req;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


/**
 * Title: Weekend
 * Package: com.martn.weekend.share
 * Description: ("朋友圈分享")
 * Date 2014/10/5 10:02
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class FriendAndZoneShare implements IWXAPIEventHandler {
    public static final int SHARE_TO_FRIEND_ZONE = 0;
    public static final int SHARE_TO_WEIXIN = 1;
    IWXAPI api;
    Bitmap bitmap;
    String description;
    String title;
    String url;

    public FriendAndZoneShare(Context context, boolean isPayFriendZone) {
        Common.isPayFriendZone = isPayFriendZone;
        api = WXAPIFactory.createWXAPI(context, Common.WXCommon.APP_ID, true);
        api.registerApp(Common.WXCommon.APP_ID);
        if (!api.isWXAppInstalled()) {
            Helper.showToast("您还未安装微信App");
        }
    }

    public IWXAPI getAPI() {
        return api;
    }

    public void setInformation(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void setInformation(String title, String description, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
    }

    /**
     * 发送请求
     */
    public void sendReq() {
        WXTextObject textObject = new WXTextObject();
        textObject.text = description;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObject;
        msg.description = "xdsm_description";
        Req req = new Req();
        req.scene = SHARE_TO_WEIXIN;
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        api.sendReq(req);
    }

    /**
     * 场景
     * @param scene
     */
    public void sendUrlLinkReq(int scene) {
        new WXTextObject().text = description;
        try {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = url;
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = title;
            msg.description = description;
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(
                        BaseApplication.context().getResources(), R.drawable.img_share_icon);
            }
            msg.thumbData = compressImage(bitmap);

            Req req = new Req();
            req.transaction = buildTransaction("webpage");
            req.message = msg;
            req.scene = scene;
            api.sendReq(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onReq(BaseReq arg0) {
    }

    public void onResp(BaseResp arg0) {
    }

    private String buildTransaction(String type) {
        return type == null ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @SuppressLint({"NewApi"})
    public int KBSizeOf(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= 19) {
            return bitmap.getAllocationByteCount() / 1024;
        }
        if (Build.VERSION.SDK_INT >= 12) {
            return bitmap.getByteCount() / 1024;
        }
        return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024;
    }

    private Bitmap compressImage(Bitmap sourceBitmap, int maxKBSize, int base) {
        Bitmap targetBitmap = ThumbnailUtils.extractThumbnail(
                sourceBitmap, sourceBitmap.getWidth() / base, sourceBitmap.getHeight() / base);
        return KBSizeOf(targetBitmap) <= maxKBSize ?
                targetBitmap : compressImage(sourceBitmap, maxKBSize, base + SHARE_TO_WEIXIN);
    }

    public byte[] compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        while (baos.toByteArray().length / 1024 >= 32) {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;
            KLog.e("sendUrlLinkReq 0 : " + (baos.toByteArray().length / 1024));
        }
        Bitmap bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()));
        KLog.e("sendUrlLinkReq 1 : " + (baos.toByteArray().length / 1024));
        KLog.e("sendUrlLinkReq 2 : " + KBSizeOf(bitmap) + " | " + KBSizeOf(image) + " | ");
        return baos.toByteArray();
    }

}
