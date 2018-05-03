package cn.javava.shenma.timer;

import cn.javava.shenma.utils.SoundPoolUtil;
import cn.javava.shenma.utils.UIUtils;

/**
 * Created by aiyoRui on 2018/5/2.
 */

public class LeiSureTimerTask implements Runnable {

    private final static int KEY_TIME = 1000 * 30;

    @Override
    public void run() {
        SoundPoolUtil.getInstance().soundLeiSure();
        UIUtils.postDelayed(this, KEY_TIME);
    }

    public void start() {
        stop();
        UIUtils.postDelayed(this, KEY_TIME);

    }

    public void stop() {
        UIUtils.removeCallbacks(this);
    }

}
