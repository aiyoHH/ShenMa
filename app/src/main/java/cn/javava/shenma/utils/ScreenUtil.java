package cn.javava.shenma.utils;

import android.util.DisplayMetrics;

import cn.javava.shenma.app.App;


public class ScreenUtil {
    /**
     * dpתpx
     */
    public static int dip2px( float dpValue) {
        final float scale =  App.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * pxתdp
     */
    public static int px2dip( float pxValue) {
        final float scale = App.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * screenWidth
     *
     * @param
     * @return
     */
    public static int getScreenWidth() {
        DisplayMetrics dm = App.getInstance().getResources().getDisplayMetrics();

        return dm.widthPixels;
    }

    /**
     * screenHeight
     *
     * @param
     * @return
     */
    public static int getScreenHeight() {

        DisplayMetrics dm =  App.getInstance().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }
}
