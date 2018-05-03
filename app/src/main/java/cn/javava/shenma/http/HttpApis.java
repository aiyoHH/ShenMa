package cn.javava.shenma.http;

import cn.javava.shenma.bean.BannerBean;
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


    @FormUrlEncoded
    @POST()
    Observable<TokenBean> gainToken(
            @Url String url,
            @Field("grant_type")String type,
                                    @Field("client_id")String id,
                                    @Field("client_secret")String secret );

    @FormUrlEncoded
    @POST("/jiayi/index.php/index/Api/openmachine")
    Observable<UserInfoBean> obtainUserInfo(@Field("machinenumber") String deviceId);

    /**
     * 获取轮播图
     * @param deviceId
     * @param openId
     * @param memberId
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("/jiayi/index.php/index/Api/bannerlist")
    Observable<BannerBean> obtainBannerList(@Field("machinenumber")String deviceId,
                                            @Field("open_id")String openId,
                                            @Field("member_id")int memberId,
                                            @Field("token")String token);


    /**
     * 获取房间列表
     * @param deviceId
     * @return
     */
    @FormUrlEncoded
    @POST("/jiayi/index.php/index/Api/machinegoodslist")
    Observable<RoomsBean> obtainRoomList(@Field("machinenumber")String deviceId);



    @FormUrlEncoded
    @POST("/trades/generateQRCode")
    Observable<PayResultBean> obtainQRCodePay(@Query("access_token") String accessToken,@Field("userId")String useId,@Field("money")int money);




    @FormUrlEncoded
    @POST()
    Observable<ConfigBean> gainConfig(@Url String url,
                                      @Field("session_id")String sessionId,
                                      @Field("confirm")int id,
                                      @Field("level")String level);

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

    @FormUrlEncoded
    @POST("/jiayi/index.php/index/Api/receivelog")
    Observable<NoneDataBean> registerGood(@Field("machinenumber")String deviceId,
                                          @Field("open_id")String openId,
                                          @Field("member_id")int memberId,
                                          @Field("token")String token,
                                          @Field("goods_id")String goodsId);


    @FormUrlEncoded
    @POST("/jiayi/index.php/index/Api/princecount")
    Observable<NoneDataBean> goodCount(@Field("machinenumber")String deviceId,
                                          @Field("open_id")String openId,
                                          @Field("member_id")int memberId,
                                          @Field("token")String token);




    @FormUrlEncoded
    @POST("/jiayi/index.php/index/Api/clertlogin")
    Observable<ResponseBody> clearLogin(@Field("machinenumber")String deviceId);
}

