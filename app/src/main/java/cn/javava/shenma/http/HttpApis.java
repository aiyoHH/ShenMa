package cn.javava.shenma.http;

import cn.javava.shenma.bean.RoomO;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;
import rx.Subscriber;

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

    @GET("/users")
    Observable<ResponseBody> obtainUserList();

    @GET("/users/{userid}")
    Observable<ResponseBody> obtainUserInfo(@Path("userid") String userId);

    @GET("/users/me")
    Observable<ResponseBody> obtainUserMe();

    @GET("/lives")
    Observable<ResponseBody> obtainLiveList();

    @GET("/lives/rooms")
    Observable<ResponseBody> obtainLiveRoomList();

    @POST("/pay/trades/generateQRCode")
    Observable<ResponseBody> obtainQRCodePay();

    @GET("/pay/trades/{tradeNo}")
    Observable<ResponseBody> checkResultPay(@Path("tradeNo") String tradeNo);

    @GET("/authc/scanQRCodeComplete")
    Observable<ResponseBody> scanQRCodeLogin();
}
