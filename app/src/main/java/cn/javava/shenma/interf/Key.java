package cn.javava.shenma.interf;

import java.io.File;

import cn.javava.shenma.app.App;

/**
 * Created by Li'O on 2017/6/28.
 * Description {TODO}.
 */

public interface Key {
//    String BASE_URL = "https://liveroom3671502238-api.zego.im/demo/";
    String BASE_URL = "http://weixin.javava.cn";
    String PATH_DATA = App.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";
    String PATH_CACHE = PATH_DATA + File.separator + "NetCache";
    String DATA="data";
    int ACTION_PLAY=1001;
    String GTANT_TYPE="client_credentials";
    String CLIENT_ID="a";
    String CLIENT_SECRET="b";
    String CLIENT_STATE="DeviceOnline";


    interface Type{
        int banner=0;
        int video=1;
        int userInfo=2;
        int content=3;
    }

}
