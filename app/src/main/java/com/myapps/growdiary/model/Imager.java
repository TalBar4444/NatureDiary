package com.myapps.growdiary.model;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

public class Imager {

    private static Imager instance;
    private static Context appContext;

    public static Imager me() {
        return instance;
    }

    private Imager(Context context) {
        appContext = context;
    }

    public static Imager initHelper(Context context) {
        if (instance == null)
            instance = new Imager(context);
        return instance;
    }

    public void setLocked(ImageView imageView) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  //0 means grayscale
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(cf);
        imageView.setImageAlpha(128);   // 128 = 0.5
    }

    public void setUnlocked(ImageView imageView) {
        imageView.setColorFilter(null);
        imageView.setImageAlpha(255);
    }
    public void setUnlock(ImageView imageView, boolean toLock) {
        if (toLock) {
            setLocked(imageView);
        } else {
            setUnlocked(imageView);
        }
    }

    public void tintRes(ImageView imageView, @ColorRes int color) {
        if (color == 0) {
            imageView.setImageTintList(null);
        } else {
            imageView.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(appContext, color)));
        }
    }

    public void tint(ImageView imageView, int color) {
        if (color == 0) {
            imageView.setImageTintList(null);
        } else {
            imageView.setImageTintList(ColorStateList.valueOf(color));
        }
    }

    public void tint2(ImageView imageView, int color) {
        if (color == 0) {
            imageView.setColorFilter(0, android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            imageView.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    public void imageByDrawableCrop(ImageView imageView, int resourceId) {
        Glide.with(appContext).load(resourceId).centerCrop().into(imageView);
    }

    public void imageByDrawableName(ImageView imageView, String name) {
        final int resourceId = appContext.getResources().getIdentifier(name, "drawable", appContext.getPackageName());
        Glide.with(appContext).load(resourceId).fitCenter().into(imageView);
    }

    public void imageByDrawableNameCenterCrop(ImageView imageView, String name) {
        final int resourceId = appContext.getResources().getIdentifier(name, "drawable", appContext.getPackageName());
        Glide.with(appContext).load(resourceId).centerCrop().into(imageView);
    }

    public void imageFromAssets(ImageView imageView, String path) {
        Glide.with(appContext).load(Uri.parse("file:///android_asset/" + path)).fitCenter().into(imageView);
    }
    public void imageFromAssetsCenterCrop(ImageView imageView, String path) {
        Glide.with(appContext).load(Uri.parse("file:///android_asset/" + path)).centerCrop().into(imageView);
    }

    public void imageCrop(ImageView imageView, String url) {
        Glide.with(appContext).load(url).centerCrop().into(imageView);
    }
}