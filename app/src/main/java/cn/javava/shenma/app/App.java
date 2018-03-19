package cn.javava.shenma.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.zego.zegoliveroom.ZegoLiveRoom;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by aiyoRui on 2017/6/12.
 */

public class App extends Application  {

    public volatile static App instance = null;
    private Handler handler;
    private Set<Activity> allActivities;

    public static App getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {

        super.onCreate();

        instance = this;

        handler = new Handler();

        ZegoLiveRoom.setTestEnv(true);

        // 初始化sdk
        ZegoApiManager.getInstance().initSDK();
    }



    public Handler getHandler() {
        return this.handler;
    }

    public Context getContext() {
        return getApplicationContext();
    }


    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<>();
        }
        allActivities.add(act);
    }

    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }


    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

}
