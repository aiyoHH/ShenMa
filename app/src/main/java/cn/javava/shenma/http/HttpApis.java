package cn.javava.shenma.http;

import cn.javava.shenma.bean.LiveRoomsBean;
import cn.javava.shenma.bean.LivesBean;
import cn.javava.shenma.bean.PayResultBean;
import cn.javava.shenma.bean.RoomO;
import cn.javava.shenma.bean.UserInfoBean;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
     * @param
     * @return
     */
    @GET()
    Observable<RoomO> apiTest(@Url String url);

    @GET("/users")
    Observable<ResponseBody> obtainUserList();

    @GET("/users/{userid}")
    Observable<UserInfoBean> obtainUserInfo(@Path("userid") String userId);

    @GET("/users/me")
    Observable<ResponseBody> obtainUserMe();

    @GET("/lives")
    Observable<LivesBean> obtainLiveList();

    @GET("/lives/rooms")
    Observable<LiveRoomsBean> obtainLiveRoomList(@Query("pager")int pager);

    @FormUrlEncoded
    @POST("/pay/trades/generateQRCode")
    Observable<PayResultBean> obtainQRCodePay(@Field("userId")String useId,@Field("money")int money);

    @GET("/pay/trades/{tradeNo}")
    Observable<ResponseBody> checkResultPay(@Path("tradeNo") String tradeNo);

    @GET("/authc/scanQRCodeComplete")
    Observable<ResponseBody> scanQRCodeLogin();

    @GET
    Observable<ResponseBody> getEntrptedConfig(@Url String url);
}
