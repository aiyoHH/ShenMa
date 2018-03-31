package cn.javava.shenma.http;

import cn.javava.shenma.bean.ConfigBean;
import cn.javava.shenma.bean.NoneDataBean;
import cn.javava.shenma.bean.RoomsBean;
import cn.javava.shenma.bean.LivesBean;
import cn.javava.shenma.bean.PayResultBean;
import cn.javava.shenma.bean.RoomO;
import cn.javava.shenma.bean.TokenBean;
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

    @FormUrlEncoded
    @POST("/oauth/token")
    Observable<TokenBean> gainToken(@Field("grant_type")String type,
                                    @Field("client_id")String id,
                                    @Field("client_secret")String secret );

    @FormUrlEncoded
    @POST("/jiayi/index.php/index/Api/openmachine")
    Observable<UserInfoBean> obtainUserInfo(@Field("machinenumber") String deviceId);

    @GET("/users/me")
    Observable<ResponseBody> obtainUserMe();

    /**
     * 获取房间列表
     * @param accessToken
     * @param state
     * @return
     */
    @GET("/rooms")
    Observable<RoomsBean> obtainRoomList(@Query("access_token")String accessToken,@Query("state")String state);


    @GET("/lives/rooms")
    Observable<RoomsBean> obtainLiveRoomList(@Query("pager")int pager);

    @FormUrlEncoded
    @POST("/trades/generateQRCode")
    Observable<PayResultBean> obtainQRCodePay(@Query("access_token") String accessToken,@Field("userId")String useId,@Field("money")int money);

    @GET("/pay/trades/{tradeNo}")
    Observable<ResponseBody> checkResultPay(@Path("tradeNo") String tradeNo);

    @GET("/authc/scanQRCodeComplete")
    Observable<ResponseBody> scanQRCodeLogin();

    @GET
    Observable<ResponseBody> getEntrptedConfig(@Url String url);


    @FormUrlEncoded
    @POST("/games")
    Observable<ConfigBean> gainConfig(@Field("access_token")String accessToken,
                                      @Field("session_id")String sessionId,
                                      @Field("confirm")int id);

    @FormUrlEncoded
    @POST("/jiayi/index.php/index/Api/closemachine")
    Observable<NoneDataBean> exitUser(@Field("machinenumber")String deviceId,
                                      @Field("open_id")String openId,
                                      @Field("member_id")int memberId,
                                      @Field("token")String token);

    @FormUrlEncoded
    @POST("/jiayi/index.php/index/Api/updatebalance")
    Observable<NoneDataBean> feeDeduction(@Field("machinenumber")String deviceId,
                                      @Field("balance")int fee,
                                      @Field("open_id")String openId,
                                      @Field("member_id")int memberId,
                                      @Field("token")String token);
}

