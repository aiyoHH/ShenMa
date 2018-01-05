package cn.javava.shenma.interf;

import java.io.File;

import cn.javava.shenma.app.App;

/**
 * Created by Li'O on 2017/6/28.
 * Description {TODO}.
 */

public interface Key {
    String BASE_URL = "https://6320app.siy8.com/";
    String PATH_DATA = App.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";
    String PATH_CACHE = PATH_DATA + File.separator + "NetCache";

    interface Type{
        int video=0;
        int content=1;
    }

}
