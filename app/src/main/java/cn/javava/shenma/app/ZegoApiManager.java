package cn.javava.shenma.app;


import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.zego.zegoliveroom.ZegoLiveRoom;
import com.zego.zegoliveroom.constants.ZegoAvConfig;

import cn.javava.shenma.utils.PreferenceUtil;


/**
 * des: zego api管理器.
 */
public class ZegoApiManager {

    private static ZegoApiManager sInstance = null;

    private ZegoLiveRoom mZegoLiveRoom = null;

    private ZegoAvConfig mZegoAvConfig = null;

    private long mAppID = 0;
    private byte[] mSignKey = null;

    private String mUserID = null;

    private ZegoApiManager() {
        mZegoLiveRoom = new ZegoLiveRoom();
    }

    public static ZegoApiManager getInstance() {
        if (sInstance == null) {
            synchronized (ZegoApiManager.class) {
                if (sInstance == null) {
                    sInstance = new ZegoApiManager();
                }
            }
        }
        return sInstance;
    }

    private void initUserInfo() {
        // 初始化用户信息
        mUserID = PreferenceUtil.getInstance().getUserID();
        String userName = PreferenceUtil.getInstance().getUserName();

        if (TextUtils.isEmpty(mUserID) || TextUtils.isEmpty(userName)) {
            long ms = System.currentTimeMillis();
            mUserID = "wawaji_android_" + ms + "_" + (int) (Math.random() * 100);
            userName = mUserID;

            // 保存用户信息
            PreferenceUtil.getInstance().setUserID(mUserID);
            PreferenceUtil.getInstance().setUserName(userName);
        }
        // 必须设置用户信息
        ZegoLiveRoom.setUser(mUserID, userName);

    }


    private void init(long appID, byte[] signKey) {

        initUserInfo();

        mAppID = appID;
        mSignKey = signKey;


        // 初始化sdk
        boolean ret = mZegoLiveRoom.initSDK(appID, signKey,App.getInstance());
        if (!ret) {
            // sdk初始化失败
            Toast.makeText(App.getInstance(), "Zego SDK初始化失败!", Toast.LENGTH_LONG).show();
        } else {

            Toast.makeText(App.getInstance(), "Zego SDK初始化成功!", Toast.LENGTH_LONG).show();
            // 初始化设置级别为"High"
            mZegoAvConfig = new ZegoAvConfig(ZegoAvConfig.Level.Generic);
            mZegoLiveRoom.setAVConfig(mZegoAvConfig);
        }
    }

    /**
     * 初始化sdk.
     */
    public void initSDK() {
        long appID = 2772127404L;
        byte[] signKey = {
                (byte) 0x53, (byte) 0xc8, (byte) 0x4a, (byte) 0xd1, (byte) 0x5b, (byte) 0xdd, (byte) 0xd7, (byte) 0x0d,
                (byte) 0x10, (byte) 0x80, (byte) 0xd3, (byte) 0x17, (byte) 0x23, (byte) 0xa0, (byte) 0xf9, (byte) 0x2d,
                (byte) 0x88, (byte) 0x36, (byte) 0x8b, (byte) 0xb7, (byte) 0x92, (byte) 0x87, (byte) 0xe9, (byte) 0x75,
                (byte) 0x19, (byte) 0x24, (byte) 0x97, (byte) 0x26, (byte) 0xe7, (byte) 0x57, (byte) 0x35, (byte) 0x2d
        };

        //测试appId  signKey
//        long appID = 3671502238L;
//        byte[] signKey = {
//                (byte)0x30,(byte)0x3a,(byte)0x83,(byte)0x1b,(byte)0xae,(byte)0x23,(byte)0xc6,(byte)0x6e,
//                (byte)0x73,(byte)0xba,(byte)0x23,(byte)0xfc,(byte)0x69,(byte)0xa2,(byte)0x7f,(byte)0xe4,
//                (byte)0x9f,(byte)0xca,(byte)0x1c,(byte)0x03,(byte)0x9f,(byte)0x93,(byte)0x5b,(byte)0x47,
//                (byte)0xf0,(byte)0x6b,(byte)0xa6,(byte)0xf2,(byte)0x81,(byte)0x21,(byte)0x5b,(byte)0xa5
//        };


        init(appID, signKey);
    }

    public void releaseSDK() {
        mZegoLiveRoom.unInitSDK();
    }

    public ZegoLiveRoom getZegoLiveRoom() {
        return mZegoLiveRoom;
    }

    public long getAppID() {
        return mAppID;
    }
}
