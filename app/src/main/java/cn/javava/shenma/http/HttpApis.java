package cn.javava.shenma.http;

import cn.javava.shenma.bean.RoomO;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by aiyoRui on 2016/12/27.
 */

public interface HttpApis {


    /**
     * api实例
     *
     * @param appId 测试服务器
     * @return
     */
    @GET("roomlist")
    Observable<RoomO> apiTest(@Query("appid")String appId);


}
