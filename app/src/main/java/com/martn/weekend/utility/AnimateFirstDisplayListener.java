package com.martn.weekend.utility;

import android.graphics.Bitmap;
import android.view.View;

import com.martn.weekend.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
    public static final List<String> displayedImages = Collections.synchronizedList(new LinkedList());
    private static DisplayImageOptions headOptions;
    private static AnimateFirstDisplayListener listener;
    private static DisplayImageOptions options;
    private static DisplayImageOptions optionsNoCache;
    private static DisplayImageOptions systemHeadOptions;

    public static AnimateFirstDisplayListener getListener() {
        if (listener == null) {
            synchronized (AnimateFirstDisplayListener.class) {
                if (listener == null) {
                    listener = new AnimateFirstDisplayListener();
                }
            }
        }
        return listener;
    }

    public static DisplayImageOptions getOptions() {
        if (options == null) {
            synchronized (AnimateFirstDisplayListener.class) {
                if (options == null) {
                    options = new DisplayImageOptions.Builder()
                            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                            .showImageOnLoading(R.drawable.img_default_download)
                            .showImageForEmptyUri(R.drawable.img_default_download)
                            .cacheInMemory(true)
                            .cacheOnDisk(true)
                            .considerExifParams(true)
                            .build();
                }
            }
        }
        return options;
    }

    public static DisplayImageOptions getOptionsNoCache() {
        if (optionsNoCache == null) {
            synchronized (AnimateFirstDisplayListener.class) {
                if (optionsNoCache == null) {
                    optionsNoCache = new DisplayImageOptions.Builder()
                            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                            .showImageOnLoading(R.drawable.img_default_download)
                            .showImageForEmptyUri(R.drawable.img_default_download)
                            .cacheInMemory(false)
                            .cacheOnDisk(false)
                            .considerExifParams(false)
                            .build();
                }
            }
        }
        return options;
    }

    public static DisplayImageOptions getHeadOptions() {
        if (headOptions == null) {
            synchronized (AnimateFirstDisplayListener.class) {
                if (headOptions == null) {
                    headOptions = new DisplayImageOptions.Builder()
                            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                            .showImageOnLoading(R.drawable.img_default_head)
                            .showImageForEmptyUri(R.drawable.img_default_head)
                            .cacheInMemory(true)
                            .cacheOnDisk(true)
                            .considerExifParams(true)
                            .build();
                }
            }
        }
        return headOptions;
    }

    public static DisplayImageOptions getSystemOptions() {
        if (systemHeadOptions == null) {
            synchronized (AnimateFirstDisplayListener.class) {
                if (systemHeadOptions == null) {
                    systemHeadOptions = new DisplayImageOptions.Builder()
                            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                            .showImageOnLoading(R.drawable.img_system_head)
                            .showImageForEmptyUri(R.drawable.img_system_head)
                            .cacheInMemory(true)
                            .cacheOnDisk(true)
                            .considerExifParams(true)
                            .build();
                }
            }
        }
        return headOptions;
    }


    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        if (loadedImage != null) {
            if (!displayedImages.contains(imageUri)) {
                displayedImages.add(imageUri);
            }
        }
    }
}