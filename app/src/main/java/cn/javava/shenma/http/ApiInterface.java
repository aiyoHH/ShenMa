package cn.javava.shenma.http;

import cn.javava.shenma.bean.LiveRoomsBean;
import cn.javava.shenma.bean.LivesBean;
import cn.javava.shenma.bean.RoomO;
import cn.javava.shenma.bean.UserInfoBean;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * Created by aiyoRui on 2018/1/4.
 */

public interface ApiInterface {

    void apiTest(Subscriber<RoomO> s,String appId);

    void getEntrptedConfig(Subscriber<ResponseBody> subscriber,String url);

    void obtainUserList(Subscriber<ResponseBody> subscriber);

    void obtainUserInfo(Subscriber<UserInfoBean> subscriber, String userId);

    void obtainUserMe(Subscriber<ResponseBody> subscriber);

    void obtainLiveList(Subscriber<LivesBean> subscriber);

    void obtainLiveRoomList(Subscriber<LiveRoomsBean> subscriber);

    void obtainQRCodePay(Subscriber<ResponseBody> subscriber);

    void checkResultPay(Subscriber<ResponseBody> subscriber,String tradeNo);

    void scanQRCodeLogin(Subscriber<ResponseBody> subscriber);

}
