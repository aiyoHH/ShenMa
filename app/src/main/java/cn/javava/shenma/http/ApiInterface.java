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
import rx.Subscriber;

/**
 * Created by aiyoRui on 2018/1/4.
 */

public interface ApiInterface {

    void apiTest(Subscriber<RoomO> s,String appId);

    void obtainBanners(Subscriber<BannerBean> subscriber);



    void obtainUserInfo(Subscriber<UserInfoBean> subscriber, String userId);


    void obtainRoomList(Subscriber<RoomsBean> subscriber);


    void obtainQRCodePay(Subscriber<PayResultBean> subscriber, String useId,int money);



    void gainConfig(Subscriber<ConfigBean> subscriber, String accessToken, String sessionId,int confirm);

    void exitUser(Subscriber<NoneDataBean>subscriber);

    void feeDeduction(Subscriber<NoneDataBean>subscriber, int fee);


    void registerGood(Subscriber<NoneDataBean>subscriber, String goodId);

}
