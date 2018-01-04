package cn.javava.shenma.http;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by aiyoRui on 2016/12/27.
 */

public interface HttpApis {


    /**
     * api实例
     *
     * @param url 测试服务器
     * @return
     */
    @GET("/v1.3/siy8/homepage?")
    Observable<ResponseBody> apiTest(@Url String url);


}
