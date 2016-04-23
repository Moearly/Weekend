package com.martn.weekend.share;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Build.VERSION;

import com.martn.weekend.R;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.utils.Utility;
import com.socks.library.KLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Title: Weekend
 * Package: com.martn.weekend.share
 * Description: ("微博分享")
 * Date 2014/10/5 10:40
 *
 * @author MartnLei MartnLei_163_com
 * @version V1.0
 */
public class SinaShare {
    Bitmap bitmap;
    Context context;
    String description;
    String imgUrl;
    IWeiboShareAPI mWeiboShareAPI;
    String title;
    String url;
    String webDes;

    public SinaShare(Context context, IWeiboShareAPI mWeiboShareAPI) {
        this.context = context;
        this.mWeiboShareAPI = mWeiboShareAPI;
    }

    public void sendMultiMessage() {
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.mediaObject = getWebpageObj();
        weiboMessage.textObject = getTextObj();
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        this.mWeiboShareAPI.sendRequest((Activity) this.context, request);
    }

    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = this.description;
        return textObject;
    }

    private WebpageObject getWebpageObj() {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = this.title;
        mediaObject.description = this.webDes;
        if (this.bitmap == null) {
            this.bitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.img_share_icon);
        }
        mediaObject.thumbData = compressImage(this.bitmap);
        mediaObject.actionUrl = this.url;
        mediaObject.defaultText = this.webDes;
        return mediaObject;
    }

    public void setInformation(String title, String description, String webDes, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.webDes = webDes;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @SuppressLint({"NewApi"})
    public int KBSizeOf(Bitmap bitmap) {
        if (VERSION.SDK_INT >= 19) {
            return bitmap.getAllocationByteCount() / 1024;
        }
        if (VERSION.SDK_INT >= 12) {
            return bitmap.getByteCount() / 1024;
        }
        return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024;
    }

    private Bitmap compressImage(Bitmap sourceBitmap, int maxKBSize, int base) {
        Bitmap targetBitmap = ThumbnailUtils.extractThumbnail(sourceBitmap, sourceBitmap.getWidth() / base, sourceBitmap.getHeight() / base);
        return KBSizeOf(targetBitmap) <= maxKBSize ? targetBitmap : compressImage(sourceBitmap, maxKBSize, base + 1);
    }

    public byte[] compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);
        int options = 90;
        while (baos.toByteArray().length / 1024 > 30) {
            baos.reset();
            image.compress(CompressFormat.JPEG, options, baos);
            options -= 10;
            KLog.e("sendUrlLinkReq 0 : " + (baos.toByteArray().length / 1024));
        }
        Bitmap bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()));
        KLog.e("sendUrlLinkReq 1 : " + (baos.toByteArray().length / 1024));
        KLog.e("sendUrlLinkReq 2 : " + KBSizeOf(bitmap) + " | " + KBSizeOf(image) + " | ");
        return baos.toByteArray();
    }

}
