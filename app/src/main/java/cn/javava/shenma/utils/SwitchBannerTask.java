package cn.javava.shenma.utils;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by aiyoRui on 2018/4/18.
 */

public class SwitchBannerTask implements Runnable {
    private ViewPager mViewPager;

    @Override
    public void run() {
        int item = mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(++item);
        UIUtils.postDelayed(this, 5000);
    }

    public void start(ViewPager viewPager) {
        this.mViewPager=viewPager;
        stop();
        UIUtils.postDelayed(this, 5000);
    }

    public void stop() {
        UIUtils.removeCallbacks(this);
    }
}
