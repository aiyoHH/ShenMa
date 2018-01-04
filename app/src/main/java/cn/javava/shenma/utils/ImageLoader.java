package cn.javava.shenma.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import cn.javava.shenma.R;


/**
 * Created by Lio on 2017/8/1.
 */
public class ImageLoader {

    public static void loadRight(Context context, String url, ImageView iv) {
      Glide.with(context).load(url).override(700, 700).crossFade(300).priority(Priority.IMMEDIATE).error(R.drawable.ic_launcher_background).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
    }

    public static void load(Context context, String url, ImageView iv) {
        if(TextUtils.isEmpty(url)){
            Glide.with(context).load(url).into(iv);
        }else{
            Glide.with(context).load(url).override(300, 300).crossFade(300).error(R.drawable.ic_launcher_background).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
        }
    }

    public static void loadTop(Context context, String url, ImageView iv) {
        if(TextUtils.isEmpty(url)){
            Glide.with(context).load(url).into(iv);
        }else{
            Glide.with(context).load(url).crossFade(300).priority(Priority.IMMEDIATE).error(R.drawable.ic_launcher_background).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
        }
    }
    public static void loadplaceholder(Context context, String url, ImageView iv) {
          Glide
                .with(context)
                .load(url)
                .override(700, 700) // resizes the image to these dimensions (in pixel)
                .fitCenter()
                .error(R.drawable.ic_launcher_background)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(iv);// this cropping technique scales the image so that it fills the requested bounds and then crops the extra.
    }

    public static void load(Context context, int resId, ImageView iv) {
            Glide.with(context).load(resId).crossFade(300).error(R.drawable.ic_launcher_background).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
    }

    public static void load(Fragment fragment, String url, ImageView iv) {
        if(TextUtils.isEmpty(url)){
            Glide.with(fragment).load(url).into(iv);
        }else{
            Glide.with(fragment).load(url).crossFade(300).error(R.drawable.ic_launcher_background).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
        }
    }

    public static void load(Activity activity, String url, ImageView iv) {
        if(TextUtils.isEmpty(url)){
            Glide.with(activity).load(url).into(iv);
        }else{
            Glide.with(activity).load(url).crossFade(300).error(R.drawable.ic_launcher_background).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
        }
    }

    public static void loadGif(Fragment fragment, String url, ImageView iv) {
        Glide.with(fragment).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
    }

    public static void loadGif(Activity activity, String url, ImageView iv) {
        Glide.with(activity).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
    }

    public static void loadCircular(final Context context, String url, final ImageView iv) {
        Glide.with(context).load(url).asBitmap().into(new BitmapImageViewTarget(iv) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                iv.setImageDrawable(circularBitmapDrawable);
            }
        });
    }
    public static void loadCircular(final Fragment context, String url, final ImageView iv) {
        Glide.with(context).load(url).asBitmap().into(new BitmapImageViewTarget(iv) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                iv.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    public static void loadAll(Activity activity, String url, ImageView iv) {
        if(!activity.isDestroyed()) {
            Glide.with(activity).load(url).crossFade().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv);
        }
    }
}
